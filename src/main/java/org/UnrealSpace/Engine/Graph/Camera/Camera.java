package org.UnrealSpace.Engine.Graph.Camera;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Engine.Graph.LocationTransformable;
import org.UnrealSpace.Engine.Graph.Matrix.ProjectionMatrix;
import org.UnrealSpace.Engine.Graph.Matrix.ViewMatrix;
import org.UnrealSpace.Engine.Graph.Model.GameItem;
import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.ISaveLoadable;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Camera implements ISaveLoadable {

    /**
     * Field of View
     */
    public static final Angle FOV = new Angle(60.0f);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000.f;
    private static final Vector WORLD_UP = new Vector(0.0f, 0.0f, 1.0f);

    private final LocationTransformable location;
    private final CameraAngle cameraAngle;

    private final ViewMatrix viewMatrix;
    private final ProjectionMatrix projectionMatrix;

    public Camera() {
        location = new LocationTransformable(3.313f, 1.4f,1.8f);
        cameraAngle = new CameraAngle(new Angle(-90), new Angle(0));
        viewMatrix = new ViewMatrix(false);
        projectionMatrix = new ProjectionMatrix(true);
    }

    public Camera(Vector location, CameraAngle cameraAngle) {
        this.location = new LocationTransformable(location);
        this.cameraAngle = cameraAngle;
        viewMatrix = new ViewMatrix(false);
        projectionMatrix = new ProjectionMatrix(true);
    }

    public void updateLocation(List<SimpleEffect> actualEffects) {
        location.setLocationWT(actualEffects, location.getWB());
    }

    @Override
    public int save() {
        int cameraAngleId = cameraAngle.save();
        if (cameraAngleId == -1) return -1;
        Database database = Database.getInstance();
        int cameraId = database.save("save_camera", null, cameraAngleId,
                location.getWB().x, location.getWB().y, location.getWB().z,
                WORLD_UP.x, WORLD_UP.y, WORLD_UP.z,
                Z_NEAR, Z_FAR, FOV.getAngleDegrees());
        database.executeUpdate("UPDATE cameras_angles SET id_camera = " + cameraId + " WHERE id_camera_angle = " + cameraAngleId);
        return cameraId;
    }

    @Override
    public void load(int id) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cameras WHERE id_camera = " + id);
            rs.next();
            int cameraAngleId = rs.getInt("id_camera_angle");
            cameraAngle.load(cameraAngleId);
            location.setWB(rs.getFloat("location_x"),
                    rs.getFloat("location_y"),
                    rs.getFloat("location_z"));
            WORLD_UP.set(rs.getFloat("world_up_x"),
                    rs.getFloat("world_up_y"),
                    rs.getFloat("world_up_z"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return масштаб преобразования положения
     * он отражает насколько отмасштабирован WT относительно WB (для масштабирования velocity)
     */
    public Vector getLocationScale() { return location.getScale(); }
    public Vector getLocationWT() {
        return location.getWT();
    }
    public Vector getLocationWB() {
        return location.getWB();
    }
    public void setLocation(float x, float y, float z) {
        location.setWB(x, y, z);
    }
    public void setLocation(Vector location) {
        setLocation(location.x, location.y, location.z);
    }
    public void moveLocation(Vector offset) {
        location.addOffsetWB(offset);
    }

    public Vector getDirection() {
        return cameraAngle.getFrontVec();
    }

    public Vector getCenterWT() { return new Vector(getLocationWT()).add(getDirection()); }
    public Vector getUp() {
        return cameraAngle.getUpVec(WORLD_UP);
    }
    public Vector getRight() { return cameraAngle.getRightVec(WORLD_UP); }

    public void rotate(Angle deltaYaw, Angle deltaPitch) {
        cameraAngle.rotate(deltaYaw, deltaPitch);

        float p89 = 89.0f, m89 = -89.0f, pitch = cameraAngle.getPitch().getAngleDegrees();
        if (pitch >= 180) pitch -= 360.0f;
        if (pitch > p89)
            cameraAngle.setPitch(new Angle(p89));
        else if (pitch < m89)
            cameraAngle.setPitch(new Angle(m89));
    }

    public Matrix4f getViewMatrix() {
        //world transformed потому что преобразования не действуют на камеру
        viewMatrix.set(getLocationWT(), getCenterWT(), getUp());
        return viewMatrix.get();
    }

    public final Matrix4f getProjectionMatrix(Float width, Float height) {
        projectionMatrix.set(FOV, width, height, new Float(Z_NEAR), new Float(Z_FAR));
        return projectionMatrix.get();
    }
}
