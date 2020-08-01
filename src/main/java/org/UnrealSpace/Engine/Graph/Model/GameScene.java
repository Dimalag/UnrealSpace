package org.UnrealSpace.Engine.Graph.Model;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Helpers.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameScene implements ISaveLoadable {
    private final List<Model> models;
    protected final List<Object3D> objects3D;
    private final List<GameItem> gameItems;
    /**
     * Сначала на карте(сцене) все эффекты не действуют, первоначально они существуют в unactualEffects
     * по ходу движения карты персонаж проходит сквозь переходные EffectRefion'ы эффектов, и
     * эффекты из unactualEffects переходят в actualEffects.
     * Это сделано для того, чтобы пушить их в шейдеры в нужном порядке.
     * в unactualEffects они могут быть в любой последовательности.
     */

    public GameScene() {
        gameItems = new ArrayList<>();
        models = new ArrayList<>();
        objects3D = new ArrayList<>();
    }

    public List<GameItem> getGameItems() {
        return gameItems;
    }

    public void init() throws Exception {
        loadModels();
    }

    private void loadModels() throws Exception {
        float mainOffset = 0;
        /*Model model = ModelLoader.loadModel("/models/", "cube_and_sphere.obj");
        models.add(model);
        List<Object3D> object3DS = model.getObject3DS();
        GameItem gameItem1 = new GameItem(object3DS.get(0));
        gameItem1.setLocationScaleToDrawInRegion(new Region(new Vector(106,-1,-2), new Vector(107,0,-1)));
        gameItem1.setRotation(new Quaternion(new Angle(90), new Vector(1,-1,0)));
        gameItems.add(gameItem1);
        GameItem gameItem2 = new GameItem(object3DS.get(1));
        gameItem2.setLocationScaleToDrawInRegion(new Region(new Vector(107, -1, -1), new Vector(108,0,0)));
        gameItem2.setRotation(new Quaternion(new Angle(60), new Vector(2,0.5f,-3)));
        gameItems.add(gameItem2);*/

        /*Model model2 = ModelLoader.loadModel("/models/", "cube.obj");
        models.add(model2);
        Object3D object3D1 = model2.getObject3DS().get(0);
        objects3D.add(object3D1);
        GameItem gameItem3 = new GameItem(object3D1);
        gameItem3.setLocationScaleToDrawInRegion(new Region(new Vector(mainOffset-1, -1, -1), new Vector(mainOffset,0,0)));
        //gameItem3.setRotation(new Quaternion(new Angle(90), 1,0,0));
        gameItems.add(gameItem3);
        GameItem gameItem4 = new GameItem(object3D1);
        gameItem4.setLocationScaleToDrawInRegion(new Region(new Vector(mainOffset-1, -1, -2), new Vector(mainOffset,0,-1)));
        //gameItem4.setRotation(new Quaternion(new Angle(90), 1,2,-1));
        gameItems.add(gameItem4);
        GameItem gameItem5 = new GameItem(object3D1);
        gameItem5.setLocationScaleToDrawInRegion(new Region(new Vector(mainOffset, -3, -2), new Vector(mainOffset+1,-2,-1)));
        //gameItem5.setRotation(new Quaternion(new Angle(90), 1,0,0));
        gameItems.add(gameItem5);
        /*GameItem gameItem6 = new GameItem(object3D1);
        gameItem6.setLocationScaleToDrawInRegion(new Region(new Vector(mainOffset, 0, 0), new Vector(mainOffset+100,100,100)));
        gameItem6.setRotation(new Quaternion(new Angle(90), 1,0,0));
        gameItems.add(gameItem6);*/

        /*Model model3 = ModelLoader.loadModel("/models/", "bunny.obj");
        models.add(model3);
        Object3D object3D2 = model3.getObject3DS().get(0);
        GameItem gameItem7 = new GameItem(object3D2);
        gameItem7.setLocationScaleToDrawInRegion(new Region(new Vector(98, -3, -2), new Vector(100,-1,0)));
        gameItem7.setRotation(new Quaternion(new Angle(90), 1,0,0));
        gameItems.add(gameItem7);*/

        Model model4 = ModelLoader.loadModel("/models/", "test.obj");
        models.add(model4);
        for (Object3D obj: model4.getObject3DS()) {
            GameItem gameItemI = new GameItem(obj);
            //gameItemI.setRotation(new Quaternion(new Angle(270), 1,0,0));
            gameItemI.setLocation(mainOffset,0,0);
            //gameItemI.setScale(new Vector(0.01f, 0.01f, 0.01f));
            /*Vector size = new Vector(10.15f, 5, 4.92f);
            Vector start = new Vector(size).div(2f).negate().add(mainOffset,0,0);
            Vector end = new Vector(size).div(2f).add(mainOffset,0,0);*/
            //gameItemI.setLocationScaleToDrawInRegion(new Region(start, end));
            gameItems.add(gameItemI);
            objects3D.add(obj);
        }
    }

    @Override
    public int save() {
        Database database = Database.getInstance();
        //saving gameScene
        int gameSceneId = database.save("save_game_scene", null);
        //saving models
        for (Model model : models) {
            model.save();
            database.executeUpdate("UPDATE models SET id_game_scene = " + gameSceneId + " WHERE id_model = " + model.getIdDB());
        }
        //saving objects
        for (Object3D obj : objects3D) {
            obj.save();
            Model modelObj = obj.getModel();
            int modelObjId = modelObj.getIdDB();
            if (modelObjId != -1)
                database.executeUpdate("UPDATE objects SET id_model = " + modelObjId + " WHERE id_object = " + obj.getIdDB());
            else
                System.out.println("game scene objects save fail to find model id");
        }
        //saving game items
        for (GameItem gameItem : gameItems) {
            int gameItemId = gameItem.save();
            if (gameItemId != -1)
                database.executeUpdate("UPDATE game_items SET id_game_scene = " + gameSceneId + " WHERE id_game_item = " + gameItemId);
        }
        return gameSceneId;
    }

    @Override
    public void load(int id) {
        Database database = Database.getInstance();
        models.clear();
        objects3D.clear();
        gameItems.clear();
        try {
            //loading models
            Connection conn = database.getConn();
            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT * FROM models WHERE id_game_scene = " + id);
            while (resultSet.next()) {
                int modelId = resultSet.getInt("id_model");
                Model model = new Model(modelId);
                models.add(model);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            //loading its all objects
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM objects" +
                    " INNER JOIN models ON models.id_model = objects.id_model" +
                    " WHERE models.id_game_scene = " + id);
            while (rs.next()) {
                int idModel = rs.getInt("id_model") - 1;
                int idObject = rs.getInt("index_model");
                objects3D.add(models.get(idModel).getObject3DS().get(idObject));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            //loading game items
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM game_items WHERE id_game_scene = " + id);
            while (rs.next()) {
                GameItem gameItem = new GameItem(this, rs.getInt("id_game_item"));
                gameItems.add(gameItem);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void cleanup() {
        for (Model model : models) {
            model.cleanup();
        }
    }
}
