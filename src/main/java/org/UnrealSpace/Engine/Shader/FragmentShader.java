package org.UnrealSpace.Engine.Shader;

import static org.lwjgl.opengl.GL46.*;

public class FragmentShader extends Shader {
    public FragmentShader(String filename) throws Exception {
        super(GL_FRAGMENT_SHADER, filename);
    }
}
