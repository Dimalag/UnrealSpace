package org.UnrealSpace.Engine.Shader;

import static org.lwjgl.opengl.GL46.*;

public class VertexShader extends Shader {
    public VertexShader(String filename) throws Exception {
        super(GL_VERTEX_SHADER, filename);
    }
}
