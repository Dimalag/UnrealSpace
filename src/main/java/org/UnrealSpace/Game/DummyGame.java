package org.UnrealSpace.Game;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Engine.*;
import org.UnrealSpace.Engine.Graph.Effect.EffectScene;
import org.UnrealSpace.Engine.Graph.Hud.HudScene;
import org.UnrealSpace.Engine.Graph.Light.LightScene;
import org.UnrealSpace.Engine.Graph.Model.GameScene;
import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DummyGame implements IGameLogic {

    private final Renderer renderer;
    private final PersonCamera personCamera;
    private final GameScene gameScene;
    private final EffectScene effectScene;
    private final LightScene lightScene;
    private final HudScene hudScene;
    private String gameName;

    public DummyGame(String gameName) {
        renderer = new Renderer();
        personCamera = new PersonCamera();
        gameScene = new GameScene();
        effectScene = new EffectScene();
        lightScene = new LightScene();
        hudScene = new HudScene();
        this.gameName = gameName;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();
        personCamera.init(null, 3.0f, 9.0f, 10.0f);
        gameScene.init();
        effectScene.init();
        lightScene.init();
        hudScene.init("DEMO", window);
    }

    @Override
    public void input(Window window, MouseInput mouseInput, KeyboardInput keyboardInput) {
        mouseInput.input(window);
        keyboardInput.input(window);
    }

    @Override
    public void update(float interval, int fps, MouseInput mouseInput, KeyboardInput keyboardInput) {
        personCamera.moveRotate(interval, mouseInput, keyboardInput);
        lightScene.update(interval, effectScene.getActualSimpleEffects(), personCamera);
        effectScene.update(personCamera);
        try {
            if (mouseInput.isRightButtonPressed())
                hudScene.setKey("/models/", "key.obj", 0);
            hudScene.update(fps, personCamera.getCamera().getLocationWB());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, personCamera, gameScene, effectScene, lightScene, hudScene);
    }

    @Override
    public int save() {
        Database database = Database.getInstance();
        int gameSceneId = gameScene.save();
        //int lightSceneId = lightScene.save();
        //int hudSceneId = hudScene.save();
        int personCameraId = personCamera.save();
        int gameId = database.save("save_game", (gameSceneId != -1 ? gameSceneId : null),
                null, null, (personCameraId != -1 ? personCameraId : null), gameName);
        if (gameSceneId != -1)
            database.executeUpdate("UPDATE game_scenes SET id_game = " + gameId + " WHERE id_game_scene = " + gameSceneId);
        if (personCameraId != -1)
            database.executeUpdate("UPDATE person_cameras SET id_game = " + gameId + " WHERE id_person_camera = " + personCameraId);
        return gameSceneId;
    }

    @Override
    public void load(int id) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM games WHERE id_game_scene = "+id);
            //ResultSet rs = database.execute("SELECT * FROM models WHERE id_game_scene = 1", false);
            while (rs.next()) {
                int gameSceneId = rs.getInt(1);
                //int lightSceneId = resultSet.getInt("id_light_scene");
                //int hudSceneId = resultSet.getInt("id_hud_scene");
                int personCameraId = rs.getInt("id_person_camera");
                this.gameName = rs.getString("game_name");

                gameScene.load(gameSceneId);
                /*lightScene.load(lightSceneId);
                hudScene.load(hudSceneId);*/
                personCamera.load(personCameraId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }

    @Override
    public GameScene getGameScene() {
        return gameScene;
    }
    @Override
    public LightScene getLightScene() {
        return lightScene;
    }
    @Override
    public PersonCamera getPersonCamera() {
        return personCamera;
    }
    @Override
    public String getGameName() {
        return gameName;
    }
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
