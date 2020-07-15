package org.UnrealSpace.Engine.Shader;

public class HudShaderProgram extends ShaderProgram {

    public HudShaderProgram(String vertexFileName, String fragmentFileName) {
        super(vertexFileName, fragmentFileName);
    }

    @Override
    protected void createUniforms() throws Exception {
        createUniform("projModelMatrix");
        createUniform("colour");
        createUniform("hasTexture");
    }

    @Override
    protected void createUniformBuffers() {
    }
}
