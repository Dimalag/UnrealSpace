package org.UnrealSpace.Engine.Graph.Light;

import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;
import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.Vector;

import java.util.List;

public class LightScene {
    public static final int MAX_POINT_LIGHTS = 8;
    public static final int MAX_SPOT_LIGHTS = 8;

    private Vector ambientLight;
    private PointLight[] pointLightList;
    private SpotLight[] spotLightList;
    private Sun sun;
    private final float specularPower;

    public LightScene() {
        specularPower = 10f;
    }

    public void init() throws Exception {
        float mainOffset = 0;
        ambientLight = new Vector(0.3f, 0.3f, 0.3f);

        // Люстра
        Vector lightPosition = new Vector(mainOffset+0.49f, 0.16f, 4.777f);
        float lightIntensity = 0.6f;
        PointLight pointLight = new PointLight(new Vector(0.8f, 0.8f, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.2f);
        pointLight.setAttenuation(att);
        pointLightList = new PointLight[]{pointLight};

        // Большой светильник 1
        lightPosition = new Vector(mainOffset-0.183f, 1.858f, 3.665f);
        pointLight = new PointLight(new Vector(1, 0, 0), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.3f, 0.0f, 0.15f);
        pointLight.setAttenuation(att);
        Vector coneDir = new Vector(0, 0, -1);
        SpotLight spotLightBig1 = new SpotLight(pointLight, coneDir, new Angle(70));

        // Большой светильник 2
        lightPosition = new Vector(mainOffset+0.509f, 1.858f, 3.665f);
        pointLight = new PointLight(new Vector(0, 1, 0), lightPosition, lightIntensity);
        pointLight.setAttenuation(att);
        SpotLight spotLightBig2 = new SpotLight(pointLight, coneDir, new Angle(70));

        // Большой светильник 3
        lightPosition = new Vector(mainOffset+1.110f, 1.858f, 3.665f);
        pointLight = new PointLight(new Vector(0, 0, 1), lightPosition, lightIntensity);
        pointLight.setAttenuation(att);
        SpotLight spotLightBig3 = new SpotLight(pointLight, coneDir, new Angle(70));

        // Маленький светильник 1
        lightPosition = new Vector(mainOffset+4.364f, -0.349f, 4.606f);
        pointLight = new PointLight(new Vector(1, 1, 1), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.4f, 0.0f, 0.3f);
        pointLight.setAttenuation(att);
        SpotLight spotLightSmall1 = new SpotLight(pointLight, coneDir, new Angle(70));

        // Маленький светильник 2
        lightPosition = new Vector(mainOffset+4.364f, 0.188f, 4.606f);
        pointLight = new PointLight(new Vector(1, 1, 1), lightPosition, lightIntensity);
        pointLight.setAttenuation(att);
        SpotLight spotLightSmall2 = new SpotLight(pointLight, coneDir, new Angle(70));

        // Маленький светильник 3
        lightPosition = new Vector(mainOffset+4.364f, 0.751f, 4.606f);
        pointLight = new PointLight(new Vector(1, 1, 1), lightPosition, lightIntensity);
        pointLight.setAttenuation(att);
        SpotLight spotLightSmall3 = new SpotLight(pointLight, coneDir, new Angle(70));

        // Маленький светильник 4
        lightPosition = new Vector(mainOffset+4.364f, 1.298f, 4.606f);
        pointLight = new PointLight(new Vector(1, 1, 1), lightPosition, lightIntensity);
        pointLight.setAttenuation(att);
        SpotLight spotLightSmall4 = new SpotLight(pointLight, coneDir, new Angle(70));

        spotLightList = new SpotLight[]{spotLightBig1, spotLightBig2, spotLightBig3,
                spotLightSmall1, spotLightSmall2, spotLightSmall3, spotLightSmall4};

        lightPosition = new Vector(-1, 0, 0);
        sun = new Sun (new DirectionalLight(new Vector(1, 1, 1), lightPosition, lightIntensity), new Angle(90), 3000000);
    }

    public void update(float interval, List<SimpleEffect> actualEffects, PersonCamera personCamera) {
        sun.update(interval);
        sun.getDirectionalLight().updateDirection(actualEffects, personCamera.getCamera().getLocationWB());

        for (PointLight pointLight : pointLightList)
            pointLight.updateLocation(actualEffects, personCamera.getCamera().getLocationWB());
        for (SpotLight spotLight : spotLightList) {
            spotLight.getPointLight().updateLocation(actualEffects, personCamera.getCamera().getLocationWB());
            spotLight.updateConeDirection(actualEffects, personCamera.getCamera().getLocationWB());
        }
    }

    public Vector getAmbientLight() {
        return ambientLight;
    }
    public float getSpecularPower() {
        return specularPower;
    }

    public PointLight[] getPointLightList() {
        return pointLightList;
    }

    public SpotLight[] getSpotLightList() {
        return spotLightList;
    }

    public Sun getSun() {
        return sun;
    }
}
