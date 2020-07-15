package org.UnrealSpace.Engine.Shader;

import static org.lwjgl.opengl.GL46.*;

public class ComputeShader extends Shader {
    public ComputeShader(String filename) throws Exception {
        super(GL_COMPUTE_SHADER, filename);
    }
}
