package org.UnrealSpace.Engine.Graph.Model;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Engine.Graph.Matrix.ModelMatrix;
import org.UnrealSpace.Helpers.*;
import org.joml.Matrix4f;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameItem implements ISaveLoadable {

    private GameScene gameScene;

    private Object3D object3D;
    private final Vector location;
    private final Vector scale;
    private final Quaternion rotation;
    private Region drawnRegion = null;

    private final ModelMatrix modelMatrix;

    public GameItem() {
        location = new Vector();
        scale = new Vector(1.0f, 1.0f, 1.0f);
        rotation = new Quaternion();
        modelMatrix = new ModelMatrix(true);
    }
    public GameItem(Object3D object3D) {
        this.object3D = object3D;
        location = new Vector();
        scale = new Vector(1.0f, 1.0f, 1.0f);
        rotation = new Quaternion();
        modelMatrix = new ModelMatrix(true);
    }
    public GameItem(GameScene gameScene, int id) {
        this.gameScene = gameScene;
        location = new Vector();
        scale = new Vector(1.0f, 1.0f, 1.0f);
        rotation = new Quaternion();
        modelMatrix = new ModelMatrix(true);
        load(id);
    }

    public void setLocationScaleToDrawInRegion(Region region) {
        drawnRegion = region;
        Region thisRegion = object3D.getRegion();
        scale.set(Region.getScaleRegions(thisRegion, drawnRegion));
        location.set(Region.getStartLocationRegions(thisRegion, drawnRegion));
    }

    @Override
    public int save() {
        if (object3D == null)
            return -1;
        int regionId = (drawnRegion != null ? drawnRegion.save() : -1);
        int quaternionId = rotation.save();
        Database database = Database.getInstance();
        int gameItemId = database.save("save_game_item", null, object3D.getIdDB(), (regionId != -1 ? regionId : null), (quaternionId != -1 ? quaternionId : null));
        if (regionId != -1)
            database.executeUpdate("UPDATE regions SET id_game_item = " + gameItemId + " WHERE id_region = " + regionId);
        if (quaternionId != -1)
            database.executeUpdate("UPDATE quaternions SET id_game_item = " + gameItemId + " WHERE id_quaternion = " + quaternionId);
        return gameItemId;
    }

    @Override
    public void load(int id) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM game_items WHERE id_game_item = " + id);
            rs.next();
            int objectId = rs.getInt("id_object");
            object3D = gameScene.objects3D.get(objectId - 1);
            try {
                int regionId = rs.getInt("id_region");
                if (regionId != 0) {
                    Region region = new Region(regionId);
                    setLocationScaleToDrawInRegion(region);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                int quaternionId = rs.getInt("id_quaternion");
                if (quaternionId != 0) {
                    Quaternion quat = new Quaternion(id);
                    setRotation(quat);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Vector getLocation() {
        return location;
    }
    public void setLocation(float x, float y, float z) {
        this.location.x = x;
        this.location.y = y;
        this.location.z = z;
    }

    public Vector getScale() {
        return scale;
    }
    public void setScale(Vector scale) {
        this.scale.set(scale);
    }

    public Quaternion getRotation() {
        return rotation;
    }
    public void setRotation(Angle angle, Vector vector) {
        this.rotation.set(angle, vector);
    }
    public void setRotation(Quaternion quat) {
        this.rotation.set(quat);
    }

    public Object3D getObject3D() {
        return object3D;
    }
    public void setObject3D(Object3D object3D) {
        this.object3D = object3D;
    }

    public Matrix4f getModelMatrix() {
        modelMatrix.set(getLocation(), getRotation(), getScale(), object3D.getRegion());
        return modelMatrix.get();
    }
}