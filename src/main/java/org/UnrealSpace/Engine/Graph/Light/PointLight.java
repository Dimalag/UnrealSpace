package org.UnrealSpace.Engine.Graph.Light;

import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Engine.Graph.LocationTransformable;
import org.UnrealSpace.Helpers.Vector;

import java.util.List;

public class PointLight {
    private Vector color;
    private LocationTransformable location;
    private float intensity;
    private Attenuation attenuation;

    public PointLight(Vector color, Vector location, float intensity) {
        this.color = color;
        this.location = new LocationTransformable(location);
        this.intensity = intensity;
        this.attenuation = new Attenuation(1, 0, 0);
    }

    public PointLight(Vector color, Vector location, float intensity, Attenuation attenuation) {
        this.color = color;
        this.location = new LocationTransformable(location);
        this.intensity = intensity;
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this.color = pointLight.color;
        this.location = new LocationTransformable(pointLight.location);
        this.intensity = pointLight.intensity;
        this.attenuation = pointLight.attenuation;
    }

    public Vector getColor() {
        return color;
    }
    public void setColor(Vector color) {
        this.color = color;
    }

    public void updateLocation(List<SimpleEffect> actualEffects, Vector personCameraLocationWB) {
        location.setLocationWT(actualEffects, personCameraLocationWB);
    }

    public Vector getLocationWT() {
        return location.getWT();
    }
    public Vector getLocationWB() {
        return location.getWB();
    }
    public void setLocationWB(Vector location) {
        this.location.setWB(location.x, location.y, location.z);
    }

    public float getIntensity() {
        return intensity;
    }
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }
    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public static class Attenuation {
        private float constant;
        private float linear;
        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }
        public void setConstant(float constant) {
            this.constant = constant;
        }

        public float getLinear() {
            return linear;
        }
        public void setLinear(float linear) {
            this.linear = linear;
        }

        public float getExponent() {
            return exponent;
        }
        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }
}
