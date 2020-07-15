package org.UnrealSpace.Engine.Graph.Effect.SubEffect;

import org.UnrealSpace.Helpers.Vector;

/**
 * Интерфейс региона функции, которая возвращает коэффициент действия эффекта от 0.0f до 1.0f
 * в зависимости от нормализованной координаты персонажа (от 0.0f до 1.0f)
 */
public interface IEffectRegionFunc {
    float getCoefficient(Vector normalizedLocation);
}
