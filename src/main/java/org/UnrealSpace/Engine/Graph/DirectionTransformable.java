package org.UnrealSpace.Engine.Graph;

import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.RotationEffect;
import org.UnrealSpace.Engine.Graph.Effect.ScaleEffect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.List;

public class DirectionTransformable implements ITransformable {
    /**
     * положение в World Base пространстве
     */
    private final Vector directionWB;
    /**
     * положение в World Transformed пространстве
     */
    private final Vector directionWT;
    public DirectionTransformable(Vector locationWB) {
        this.directionWB = locationWB;
        this.directionWT = new Vector(locationWB);
    }
    public DirectionTransformable(float x, float y, float z) {
        this.directionWB = new Vector(x, y, z);
        this.directionWT = new Vector(x, y, z);
    }
    public DirectionTransformable(DirectionTransformable locationTransformable) {
        this.directionWB = new Vector(locationTransformable.directionWB);
        this.directionWT = new Vector(locationTransformable.directionWT);
    }

    @Override
    public Vector getWB() {
        return directionWB;
    }
    @Override
    public void setWB(float x, float y, float z) {
        directionWB.set(x, y, z);
    }
    @Override
    public void addOffsetWB(Vector offset) {
        directionWB.add(offset);
    }
    @Override
    public Vector getWT() {
        return directionWT;
    }
    public void setWT(float x, float y, float z) {
        directionWT.set(x, y, z);
    }

    /**
     * @param actualEffects актуальные эффекты
     * @param personCameraLocationWB положение камеры
     */
    public void setDirectionWT(List<SimpleEffect> actualEffects, Vector personCameraLocationWB) {
        Vector4f scale = new Vector4f(1f);
        Matrix4f resultTransformation = new Matrix4f().identity();
        for(SimpleEffect effect: actualEffects)
            if (effect.isManuallyCalculated() && effect instanceof RotationEffect) {
                resultTransformation = resultTransformation.mul(effect.getEffectMatrix(personCameraLocationWB));
            }
        Vector4f resultWT = new Vector4f(directionWB, 0.0f).mul(resultTransformation);
        directionWT.set(resultWT.x, resultWT.y, resultWT.z);
    }
}
