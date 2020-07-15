package org.UnrealSpace.Engine;

import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.EffectScene;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectBufferSystem;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import java.util.List;
import static org.lwjgl.opengl.GL46.*;

import org.UnrealSpace.Engine.Graph.Light.DirectionalLight;
import org.UnrealSpace.Engine.Graph.Light.LightScene;
import org.UnrealSpace.Engine.Graph.Light.PointLight;
import org.UnrealSpace.Engine.Graph.Light.SpotLight;
import org.UnrealSpace.Engine.Graph.Model.GameItem;
import org.UnrealSpace.Engine.Graph.Model.GameScene;
import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;
import org.UnrealSpace.Engine.Shader.SceneShaderProgram;
import org.UnrealSpace.Engine.Shader.HudShaderProgram;
import org.UnrealSpace.Engine.Graph.Hud.HudScene;
import org.UnrealSpace.Engine.Graph.Model.Object3D;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.Vector;

public class Renderer {

    private SceneShaderProgram sceneShaderProgram;
    private HudShaderProgram hudShaderProgram;

    public Renderer() {
    }

    public void init() throws Exception {
        sceneShaderProgram = new SceneShaderProgram("/shaders/vertex.vs", "/shaders/fragment.fs");
        hudShaderProgram = new HudShaderProgram("/shaders/hud_vertex.vs", "/shaders/hud_fragment.fs");
    }

    public void render(Window window, PersonCamera personCamera, GameScene gameScene, EffectScene effectScene, LightScene lightScene, HudScene hudScene) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderScene(window, personCamera, gameScene, effectScene, lightScene);

        renderHudScene(window, hudScene);
    }

    private void renderScene(Window window, PersonCamera personCamera, GameScene gameScene, EffectScene effectScene, LightScene lightScene) {
        sceneShaderProgram.bind();

        sceneShaderProgram.setUniform("texture_sampler", 0);
        sceneShaderProgram.setUniform("isPerson", 0);

        renderEffects(effectScene.getEffectSystem(), personCamera);
        renderLightScene(personCamera, lightScene);
        renderGameScene(window, personCamera, gameScene);

        sceneShaderProgram.setUniform("isPerson", 1);
        renderGameItem(personCamera.getPersonModel());

        sceneShaderProgram.unBind();
    }

    private void renderEffects(EffectBufferSystem effectSystem, PersonCamera personCamera) {
        try {
            sceneShaderProgram.setEffectUniformBuffer(effectSystem, personCamera.getCamera().getLocationWB());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void renderGameScene(Window window, PersonCamera personCamera, GameScene gameScene) {
        // Update projection Matrix
        Matrix4f projectionMatrix = personCamera.getCamera().getProjectionMatrix(new Float(window.getWidth()), new Float(window.getHeight()));
        sceneShaderProgram.setMatricesUniformBuffer(64, projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = personCamera.getCamera().getViewMatrix();
        sceneShaderProgram.setMatricesUniformBuffer(0, viewMatrix);

        // Render each gameItem
        for (GameItem gameItem : gameScene.getGameItems())
            renderGameItem(gameItem);
    }

    private void renderGameItem(GameItem gameItem) {
        if (gameItem == null) return;
        // Set model matrix for this item
        sceneShaderProgram.setUniform("model", gameItem.getModelMatrix());
        sceneShaderProgram.setUniform("material", gameItem.getObject3D().getMaterial());
        // Render the mesh for this game item
        gameItem.getObject3D().render();
    }

    private void renderLightScene(PersonCamera personCamera, LightScene lightScene) {
        sceneShaderProgram.setLightsUniformBuffer(0, lightScene.getAmbientLight());
        sceneShaderProgram.setLightsUniformBuffer(12, lightScene.getSpecularPower());
        Matrix4f viewMatrix = personCamera.getCamera().getViewMatrix();

        // Process Point Lights
        PointLight[] pointLightList = lightScene.getPointLightList();
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector lightPos = currPointLight.getLocationWT();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            try {
                sceneShaderProgram.setLightsUniformBufferArrays(i, currPointLight);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Process Spot Ligths
        SpotLight[] spotLightList = lightScene.getSpotLightList();
        numLights = spotLightList != null ? spotLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirectionWT(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirectionWT(new Vector(dir.x, dir.y, dir.z));
            Vector lightPos = currSpotLight.getPointLight().getLocationWT();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            try {
                sceneShaderProgram.setLightsUniformBufferArrays(i, currSpotLight);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(lightScene.getSun().getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirectionWT(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirectionWT(new Vector(dir.x, dir.y, dir.z));
        sceneShaderProgram.setLightsUniformBuffer(16, currDirLight);
    }

    private void renderHudScene(Window window, HudScene hudScene) {
        hudShaderProgram.bind();

        //получаем ортогональную матрицу вида
        Matrix4f ortho = new Matrix4f().identity().setOrtho2D(0, window.getWidth(), window.getHeight(), 0);

        for (GameItem gameItem : hudScene.getGameItems())
            renderHud(ortho, gameItem);
        for (GameItem gameItem : hudScene.getStatusTextItems())
            renderHud(ortho, gameItem);

        hudShaderProgram.unBind();
    }

    private void renderHud(Matrix4f ortho, GameItem hud) {
        Object3D mesh = hud.getObject3D();
        // Set ortohtaphic and model matrix for this HUD item
        Matrix4f projModelMatrix = HudScene.getOrtoProjModelMatrix(hud, ortho);
        hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
        hudShaderProgram.setUniform("colour", hud.getObject3D().getMaterial().getAmbientColour());
        hudShaderProgram.setUniform("hasTexture", hud.getObject3D().getMaterial().isTextured() ? 1 : 0);

        // Render the mesh for this HUD item
        mesh.render();
    }

    public void cleanup() {
        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
        if (hudShaderProgram != null) {
            hudShaderProgram.cleanup();
        }
    }
}
