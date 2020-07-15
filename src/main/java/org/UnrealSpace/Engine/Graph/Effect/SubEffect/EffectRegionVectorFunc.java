package org.UnrealSpace.Engine.Graph.Effect.SubEffect;

import org.UnrealSpace.Helpers.Vector;

/**
 * Класс векторной функции действия эффекта
 * представляет собой изменяющийся вектор, возвращающий плавный переход от 0.0f до 1.0f, равный коэффициенту действия
 */
public class EffectRegionVectorFunc implements IEffectRegionFunc {
    private final Vector direction;
    public EffectRegionVectorFunc(Vector direction) throws Exception {
        this.direction = new Vector(direction);
        if (this.direction.length() == 0)
            throw new Exception("Wrong EffectRegionVectorFunc: 0 length vector");
        if (this.direction.length() < 1.0f)
            this.direction.normalize();
    }

    public float getCoefficient(Vector normalizedLocation) {
        if (this.direction.x < 0) normalizedLocation.x = 1 - normalizedLocation.x;
        if (this.direction.y < 0) normalizedLocation.y = 1 - normalizedLocation.y;
        if (this.direction.z < 0) normalizedLocation.z = 1 - normalizedLocation.z;
        return Math.abs(this.direction.dot(normalizedLocation) / this.direction.length());
    }
}
