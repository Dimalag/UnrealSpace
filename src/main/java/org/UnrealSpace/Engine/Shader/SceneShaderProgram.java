package org.UnrealSpace.Engine.Shader;

import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;
import org.UnrealSpace.Engine.Graph.Effect.ComplexEffect;
import org.UnrealSpace.Engine.Graph.Effect.ComplexRotationEffect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectBuffer;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectBufferSystem;
import org.UnrealSpace.Engine.Graph.Light.*;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.UniformBuffer;
import org.UnrealSpace.Helpers.Vector;
import org.javatuples.Pair;
import org.javatuples.Septet;
import org.javatuples.Triplet;
import org.joml.Matrix4f;

import java.util.List;

public class SceneShaderProgram extends ShaderProgram {
    private UniformBuffer matrices;
    private EffectBuffer effects;
    private LightBuffer lights;

    public SceneShaderProgram(String vertexFileName, String fragmentFileName) throws Exception {
        super(vertexFileName, fragmentFileName);
        createMaterialUniform("material");
    }

    private void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    @Override
    protected void createUniforms() throws Exception {
        createUniform("model");
        createUniform("isPerson");
        createUniform("texture_sampler");
        //createUniform("rotationMatrices");
    }

    @Override
    protected void createUniformBuffers() {
        matrices = new UniformBuffer(0,128);
        //effects = new EffectBuffer(1,16, 80, 16);
        effects = new EffectBuffer(1);
        lights = new LightBuffer(2,48,
                48,64,
                LightScene.MAX_POINT_LIGHTS, LightScene.MAX_SPOT_LIGHTS);
    }

    public void setMatricesUniformBuffer(int offset, Matrix4f value) {
        matrices.setSubData(offset, value);
    }
    public void setEffectUniformBuffer(EffectBufferSystem effectSystem, Vector personCameraLocation) throws Exception {
        List<Pair<Integer, Integer>> queue = effectSystem.getQueue();
        List<SimpleEffect> simpleEffects = effectSystem.getSimpleEffects();
        List<ComplexEffect> complexEffects = effectSystem.getComplexEffects();

        effects.setPersonLocationCounts(personCameraLocation, simpleEffects.size(), complexEffects.size());
        effects.setQueue(queue);

        for (int i = 0; i < simpleEffects.size(); i++)
            effects.setSubDataArrays(i, simpleEffects.get(i), personCameraLocation);
        for (int i = 0; i < complexEffects.size(); i++)
            effects.setSubDataArrays(i, complexEffects.get(i), personCameraLocation);

        /*if (complexEffects.size() > 0) {
            ComplexRotationEffect complexRotationEffect = (ComplexRotationEffect) complexEffects.get(0);
            if (!complexRotationEffect.isStaticInitializedInShader()) {
                Septet<Vector, Float, Vector, Vector, Matrix4f[], Matrix4f, Matrix4f> data = complexRotationEffect.getStaticDataAndSetShader().getValue();
                Matrix4f[] rotationMatrices = data.getValue4();
                setUniform("rotationMatrices", rotationMatrices);
            }
        }*/
    }

    public void setLightsUniformBuffer(int offset, float value) {
        lights.setSubData(offset, value);
    }
    public void setLightsUniformBuffer(int offset, Vector vector) {
        lights.setSubData(offset, vector);
    }
    public void setLightsUniformBuffer(int offset, DirectionalLight directionalLight) {
        lights.setSubData(offset, directionalLight);
    }
    public void setLightsUniformBufferArrays(int index, PointLight pointLight) throws Exception {
        lights.setSubDataArrays(index, pointLight);
    }
    public void setLightsUniformBufferArrays(int index, SpotLight spotLight) throws Exception {
        lights.setSubDataArrays(index, spotLight);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        matrices.cleanup();
        effects.cleanup();
        lights.cleanup();
    }
}
