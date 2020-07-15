package org.UnrealSpace.Engine.Graph.Light;

import org.UnrealSpace.Engine.Graph.DirectionTransformable;
import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Helpers.Vector;

import java.util.List;

public class DirectionalLight {
    private Vector color;
    private DirectionTransformable direction;
    private float intensity;

    public DirectionalLight(Vector color, Vector direction, float intensity) {
        this.color = color;
        this.direction = new DirectionTransformable(direction);
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight light) {
        this.color = light.color;
        this.direction = new DirectionTransformable(light.direction);
        this.intensity = light.intensity;
    }

    public void updateDirection(List<SimpleEffect> actualEffects, Vector personCameraLocationWB) {
        direction.setDirectionWT(actualEffects, personCameraLocationWB);
    }

    public Vector getColor() {
        return color;
    }
    public void setColor(Vector color) {
        this.color = color;
    }

    public Vector getDirectionWT() {
        return direction.getWT();
    }
    public Vector getDirectionWB() {
        return direction.getWB();
    }
    public void setDirectionWB(Vector direction) {
        this.direction.setWB(direction.x, direction.y, direction.z);
    }
    public void setDirectionWT(Vector direction) {
        this.direction.setWT(direction.x, direction.y, direction.z);
    }

    public float getIntensity() {
        return intensity;
    }
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
