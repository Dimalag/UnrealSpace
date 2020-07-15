package org.UnrealSpace.Engine.Graph.Camera;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Engine.Graph.Model.GameItem;
import org.UnrealSpace.Engine.Graph.Model.GameScene;
import org.UnrealSpace.Engine.KeyboardInput;
import org.UnrealSpace.Engine.MouseInput;
import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.Approximate;
import org.UnrealSpace.Helpers.ISaveLoadable;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Vector2f;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonCamera implements ISaveLoadable {

    protected GameScene gameScene;

    private float velocity;
    private float hitPointsMax, hitPoints;
    private GameItem personModel;
    private Camera camera;

    public PersonCamera() {
        personModel = null;
        camera = new Camera();
        velocity = 2000.0f;
        hitPointsMax = 10.0f;
        hitPoints = hitPointsMax;
    }
    public PersonCamera(GameScene gameScene, int id) {
        this();
        load(id);
    }

    public void init (GameItem personModel, float velocity, float hitPoints, float hitPointsMax) {
        this.personModel = personModel;
        this.velocity = velocity;
        this.hitPoints = hitPoints;
        this.hitPointsMax = hitPointsMax;
    }

    public void moveRotate(float interval, MouseInput mouseInput, KeyboardInput keyboardInput) {
        // moving camera and Person
        Vector2f cameraMoveSide = keyboardInput.getCameraMoveSide();
        if (!Approximate.equal(cameraMoveSide.length(), 0.0f)) { cameraMoveSide.normalize(); }
        Vector cameraForwardDirection = new Vector(camera.getDirection());
        cameraForwardDirection.z = 0; cameraForwardDirection.normalize();
        Vector cameraLeftDirection = camera.getRight().negate();
        cameraLeftDirection.z = 0; cameraLeftDirection.normalize();

        float pathForward = velocity * interval * cameraMoveSide.x();
        Vector pathForwardVector = cameraForwardDirection.mul(pathForward).div(camera.getLocationScale());
        camera.moveLocation(pathForwardVector);

        float pathLeft = velocity * interval * cameraMoveSide.y();
        Vector pathLeftVector = cameraLeftDirection.mul(pathLeft).div(camera.getLocationScale());
        camera.moveLocation(pathLeftVector);

        // rotate camera
        Angle deltaYaw = new Angle(), deltaPitch = new Angle();
        mouseInput.getOffsetAngles(deltaYaw, deltaPitch);
        camera.rotate(deltaYaw, deltaPitch);
    }

    @Override
    public int save() {
        int cameraId = camera.save();
        int personModelId = (personModel != null ? personModel.save() : -1);
        if (cameraId == -1) return -1;
        Database database = Database.getInstance();
        int personCameraId = database.save("save_person_camera", null, cameraId, (personModelId != -1) ? personModelId : null,
                velocity, hitPoints, hitPointsMax);
        database.executeUpdate("UPDATE cameras SET id_person_camera = " + personCameraId + " WHERE id_camera = " + cameraId);
        return cameraId;
    }

    @Override
    public void load(int id) {
        personModel = null;
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM person_cameras WHERE id_person_camera = " + id);
            rs.next();
            int cameraId = rs.getInt("id_camera");
            camera.load(cameraId);
            try {
                int gamePersonItemId = rs.getInt("id_game_item_person");
                if (gamePersonItemId != 0)
                    personModel = new GameItem(gameScene, gamePersonItemId);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            velocity = rs.getFloat("velocity");
            hitPoints = rs.getFloat("hit_points");
            hitPointsMax = rs.getFloat("hit_points_max");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Camera getCamera() {
        return camera;
    }
    public GameItem getPersonModel() {
        return personModel;
    }

    public float getVelocity() {
        return velocity;
    }
    public void setVelocity(float velocity) throws Exception {
        if (velocity > 0)
            this.velocity = velocity;
        else
            throw new Exception("wrong PersonCamera setVelocity: "+velocity);
    }
    public float getHitPoints() {
        return hitPoints;
    }
    public void setHitPoints(float hitPoints) {
        this.hitPoints = hitPoints;
    }
    public float getHitPointsMax() {
        return hitPointsMax;
    }
    public void setHitPointsMax(float hitPointsMax) {
        this.hitPointsMax = hitPointsMax;
    }
}
