package org.UnrealSpace.Engine.Shader;

import org.UnrealSpace.Engine.Graph.Model.Material;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import static org.lwjgl.opengl.GL46.*;

public abstract class ShaderProgram {

    private final int programId;
    VertexShader vertexShader;
    FragmentShader fragmentShader;
    private final Map<String, Integer> uniforms;

    public ShaderProgram(String vertexFileName, String fragmentFileName) {
        this.uniforms = new HashMap<>();
        programId = glCreateProgram();
        try {
            if (programId == 0) {
                throw new Exception("Could not create Shader");
            }

            loadShaders(vertexFileName, fragmentFileName);
            link();
            createUniforms();
            createUniformBuffers();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadShaders(String vertexFileName, String fragmentFileName) throws Exception {
        vertexShader = new VertexShader(vertexFileName);
        fragmentShader = new FragmentShader(fragmentFileName);
    }

    private void link() throws Exception {
        vertexShader.attachShader(programId);
        fragmentShader.attachShader(programId);

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        vertexShader.detachShader(programId);
        fragmentShader.detachShader(programId);

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    protected abstract void createUniforms() throws Exception;

    protected abstract void createUniformBuffers();

    protected void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }
    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }
    public void setUniform(String uniformName, Vector value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }
    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColour());
        setUniform(uniformName + ".diffuse", material.getDiffuseColour());
        setUniform(uniformName + ".specular", material.getSpecularColour());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void setUniform(String uniformName, Matrix4f[] matrices) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = BufferUtils.createFloatBuffer(16 * matrices.length);
            for (int i=0; i < matrices.length; i++)
                matrices[i].get(16*i, fb);

            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unBind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unBind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
