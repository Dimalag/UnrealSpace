package org.UnrealSpace.Engine.Graph.Model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.UnrealSpace.Database.Database;

import java.util.ArrayList;
import java.util.List;

import org.UnrealSpace.Helpers.ISaveLoadable;
import org.UnrealSpace.Helpers.Region;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL46.*;

/**
 * Класс объекта в пространстве,
 * который может быть непосредственно создан либо загружен
 */
public class Object3D implements ISaveLoadable {

    private final int indexInModel;
    private int idDB = -1;
    private Model model;

    private final int vaoId;
    private final List<Integer> vboIdList;
    private final int vertexCount;
    private final Region region;
    private Material material;
    private String name;

    /**
     * @param positions координаты точек
     * @param textCoords текстурные координаты
     * @param normals нормали
     * @param indices индексы для составления граней
     * @throws Exception Region исключение
     */
    public Object3D(int indexInModel, float[] positions, float[] textCoords, float[] normals, int[] indices, String name) throws Exception {
        this.indexInModel = indexInModel;
        this.name = name;
        region = new Region(positions);
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public int save() {
        Database database = Database.getInstance();
        return idDB = database.save("save_object", null, indexInModel);
    }

    public void load(int id) {

    }

    public int getIdDB() {
        return idDB;
    }

    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVaoId() {
        return vaoId;
    }
    public int getVertexCount() {
        return vertexCount;
    }

    public Region getRegion() {
        return region;
    }

    public void render() {
        if (material != null && material.getTexture() != null) {
            //glEnable(GL_TEXTURE_2D);
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
