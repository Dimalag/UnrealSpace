package org.UnrealSpace.Engine.Shader;

import static org.lwjgl.opengl.GL46.*;
import org.UnrealSpace.Helpers.Utils;

public abstract class Shader {
    private int id;

    protected Shader(int type, String filename) throws Exception {
        compileShader(type, Utils.loadResource(filename));
    }

    protected void compileShader(int type, String code) throws Exception {
        id = glCreateShader(type);
        if (id == 0) {
            throw new Exception("Error creating shader. Type: " + code);
        }

        glShaderSource(id, code);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(id, 1024));
        }
    }

    public void attachShader(int shaderProgram) throws Exception
    {
        if (shaderProgram == 0)
            throw new Exception("Program wasn't created!");
        glAttachShader(shaderProgram, id);
    }

    public void detachShader(int shaderProgram) throws Exception {
        if (shaderProgram == 0)
            throw new Exception("Program wasn't created!");
        glDetachShader(shaderProgram, id);
    }
}
