package org.UnrealSpace.Engine.Graph;

import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.RotationEffect;
import org.UnrealSpace.Engine.Graph.Effect.ScaleEffect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.List;

public class LocationTransformable implements ITransformable {
    /**
     * масштаб преобразования положения
     * он отражает насколько отмасштабирован WT относительно WB (для масштабирования velocity)
     */
    private final Vector scale;
    /**
     * положение в World Base пространстве
     */
    private final Vector locationWB;
    /**
     * положение в World Transformed пространстве
     */
    private final Vector locationWT;
    public LocationTransformable(Vector locationWB) {
        this.locationWB = locationWB;
        this.locationWT = new Vector(locationWB);
        this.scale = new Vector(1.0f, 1.0f, 1.0f);
    }
    public LocationTransformable(float x, float y, float z) {
        this.locationWB = new Vector(x, y, z);
        this.locationWT = new Vector(x, y, z);
        this.scale = new Vector(1.0f, 1.0f, 1.0f);
    }
    public LocationTransformable(LocationTransformable locationTransformable) {
        this.locationWB = new Vector(locationTransformable.locationWB);
        this.locationWT = new Vector(locationTransformable.locationWT);
        this.scale = new Vector(locationTransformable.scale);
    }

    @Override
    public Vector getWB() {
        return locationWB;
    }
    @Override
    public void setWB(float x, float y, float z) {
        locationWB.set(x, y, z);
    }
    @Override
    public void addOffsetWB(Vector offset) {
        locationWB.add(offset);
    }
    @Override
    public Vector getWT() {
        return locationWT;
    }
    /**
     * @return масштаб преобразования положения
     * он отражает насколько отмасштабирован WT относительно WB (для масштабирования velocity)
     */
    public Vector getScale() { return scale; }

    /**
     * @param actualEffects актуальные эффекты
     * @param personCameraLocationWB положение камеры
     */
    public void setLocationWT(List<SimpleEffect> actualEffects, Vector personCameraLocationWB) {
        Vector4f scale = new Vector4f(1f);
        Matrix4f resultTransformation = new Matrix4f().identity();
        for(SimpleEffect effect: actualEffects)
            if (effect.isManuallyCalculated()) {
                Matrix4f effectMatrix = effect.getEffectMatrix(personCameraLocationWB);
                resultTransformation = resultTransformation.mul(effectMatrix);
                //setting scale
                if (effect instanceof ScaleEffect)
                    scale.mul(effectMatrix);
                else if (effect instanceof RotationEffect) {
                    scale.mul(new Vector4f(
                            effectMatrix.get(0,0) >= 0 ? 1 : -1,
                            effectMatrix.get(1,1) >= 0 ? 1 : -1,
                            effectMatrix.get(2,2) >= 0 ? 1 : -1,
                            effectMatrix.get(3,3) >= 0 ? 1 : -1));
                }
            }
        Vector4f resultWT = new Vector4f(locationWB, 1.0f).mul(resultTransformation);
        this.scale.set(scale.x, scale.y, 1.0f);
        locationWT.set(resultWT.x, resultWT.y, resultWT.z);
    }
}
