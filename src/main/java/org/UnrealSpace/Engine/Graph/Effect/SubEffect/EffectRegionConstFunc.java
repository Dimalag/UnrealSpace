package org.UnrealSpace.Engine.Graph.Effect.SubEffect;

import org.UnrealSpace.Helpers.Vector;

/**
 * Класс константной функции действия эффекта
 * представляет собой константу от 0.0f до 1.0f, равную коэффициенту действия
 */
public class EffectRegionConstFunc implements IEffectRegionFunc {
    private final float constant;
    public EffectRegionConstFunc(float constant) throws Exception {
        if (constant >= 0 && constant <= 1)
            this.constant = constant;
        else
            throw new Exception("Wrong EffectRegionConstFunc: const isn't between 0 and 1");
    }

    public float getCoefficient(Vector location) {
        return constant;
    }
}
