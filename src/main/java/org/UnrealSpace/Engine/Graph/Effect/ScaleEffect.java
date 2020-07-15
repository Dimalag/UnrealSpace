package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegion;
import org.UnrealSpace.Engine.Graph.Matrix.ScaleTransformationMatrix;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;
import org.UnrealSpace.Helpers.Float;

/**
 * Класс масштабирования-эффекта
 * возвращает матрицу масштабирования в зависимости от положения камеры
 */
public class ScaleEffect extends SimpleEffect {
    private final Vector scale;
    public ScaleEffect(Vector scale, boolean manuallyCalculated) {
        super(new ScaleTransformationMatrix(true), new Vector(), manuallyCalculated);
        this.scale = scale;
    }

    @Override
    public Matrix4f getEffectMatrix(Vector location) {
        ((ScaleTransformationMatrix)transformationMatrix).set(effectLocation, scale, new Float(super.getCoefficient(location)));
        return transformationMatrix.get();
    }
}
