package org.UnrealSpace.Engine.Graph.Model;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Helpers.ISaveLoadable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Model implements ISaveLoadable {
    private int idDB = -1;
    private String folder;
    private String modelName;

    private final List<Object3D> object3DS;
    private final List<Material> materials;

    protected Model(String folder, String modelName, List<Object3D> object3DS, List<Material> materials) {
        this.folder = folder;
        this.modelName = modelName;
        this.object3DS = object3DS;
        this.materials = materials;
    }
    public Model(int id) {
        object3DS = new ArrayList<>();
        materials = new ArrayList<>();
        load(id);
    }

    public void render() {
        for (Object3D object3D : object3DS)
            object3D.render();
    }

    public int save() {
        Database database = Database.getInstance();
        idDB = database.save("save_model", null, folder, modelName);
        return idDB;
    }

    public void load(int id) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM models WHERE id_model = " + id);
            while (rs.next()) {
                folder = rs.getString("folder");
                modelName = rs.getString("model_name");
                Model modelI = ModelLoader.loadModel(folder, modelName);
                object3DS.addAll(modelI.object3DS);
                materials.addAll(modelI.materials);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getIdDB() {
        return idDB;
    }

    public List<Object3D> getObject3DS() {
        return object3DS;
    }

    /**
     * Очищает материалы и объекты
     */
    public void cleanup() {
        if (materials != null)
            for(Material material : materials)
                material.cleanup();
        for (Object3D object3D : object3DS) {
            object3D.cleanup();
        }
    }
}
