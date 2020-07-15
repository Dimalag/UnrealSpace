package org.UnrealSpace.Engine.Graph.Light;

import org.UnrealSpace.Engine.Graph.DirectionTransformable;
import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.Vector;

import java.util.List;

public class SpotLight {
    private PointLight pointLight;
    private DirectionTransformable coneDirection;
    private Angle cutOffAngle;

    public SpotLight(PointLight pointLight, Vector coneDirection, Angle cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = new DirectionTransformable(coneDirection);
        this.cutOffAngle = cutOffAngle;
    }

    public SpotLight(SpotLight spotLight) {
        this.pointLight = spotLight.pointLight;
        this.coneDirection = new DirectionTransformable(spotLight.coneDirection);
        this.cutOffAngle = spotLight.cutOffAngle;
    }

    public void updateConeDirection(List<SimpleEffect> actualEffects, Vector personCameraLocationWB) {
        coneDirection.setDirectionWT(actualEffects, personCameraLocationWB);
    }

    public PointLight getPointLight() {
        return pointLight;
    }
    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    public Vector getConeDirectionWT() {
        return coneDirection.getWT();
    }
    public Vector getConeDirectionWB() {
        return coneDirection.getWB();
    }
    public void setConeDirectionWB(Vector coneDirection) {
        this.coneDirection.setWB(coneDirection.x, coneDirection.y, coneDirection.z);
    }
    public void setConeDirectionWT(Vector coneDirection) {
        this.coneDirection.setWT(coneDirection.x, coneDirection.y, coneDirection.z);
    }

    public float getCutOffCos() {
        return (float)Math.cos(cutOffAngle.getAngleRadians());
    }

    public Angle getCutOffAngle() {
        return cutOffAngle;
    }
    public final void setCutOffAngle(Angle cutOffAngle) {
        this.cutOffAngle.setAngleDegrees(cutOffAngle.getAngleDegrees());
    }
}
