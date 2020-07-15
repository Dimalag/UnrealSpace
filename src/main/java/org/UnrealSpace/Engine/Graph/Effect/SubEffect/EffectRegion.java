package org.UnrealSpace.Engine.Graph.Effect.SubEffect;

import java.util.ArrayList;
import java.util.List;

import org.UnrealSpace.Helpers.Approximate;
import org.UnrealSpace.Helpers.Region;
import org.UnrealSpace.Helpers.Vector;

/**
 * Класс области эффекта.
 * Состоит из коэффициента действия эффекта типа float (от 0 до 1).
 * Состоит из нескольких(>1) непересекающихся регионов-функций,
 * которые образуются регионами и при попадании в них изменяют коэффициент действия.
 * Служит для определения коэффициента действия целого эффекта
 */
public abstract class EffectRegion {
    /**
     * Класс функции-региона, которая представляет собой регион, дополненный функцией действия эффекта
     */
    public class RegionFunc extends Region {
        private final IEffectRegionFunc func;
        public RegionFunc(Vector startPos, Vector endPos, IEffectRegionFunc func) {
            super(startPos, endPos);
            this.func = func;
        }

        /**
         * Рассчитывает коэффициент действия эффекта в зависимости от положения объекта при попадании в данную регион-функцию
         * @param location положения объекта
         */
        private void setCoefficient(Vector location) {
            try {
                Vector normalizedLocation = getNormalizedLocation(location);
                float result = func.getCoefficient(normalizedLocation);
                if (0 <= result && result <= 1) {
                    if (!Approximate.equal(result, coefficient))
                        isUpdatedShader = false;
                    coefficient = result;
                }
                else //!!! only for debug. в перспективе это возможно, просто такой результат не учитывается. сделано для проверки func.getCoeff
                    throw new Exception("EffectRegion getCoeff: func.getCoeff isn't between 0 and 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private boolean isInRegion(Vector location) {
            return (startPos.x <= location.x && location.x <= endPos.x) &&
                    (startPos.y <= location.y && location.y <= endPos.y) &&
                    (startPos.z <= location.z && location.z <= endPos.z);
        }

        private Vector getNormalizedLocation(Vector location) {
            return new Vector((location.x - startPos.x) / (endPos.x - startPos.x),
                    (location.y - startPos.y) / (endPos.y - startPos.y),
                    (location.z - startPos.z) / (endPos.z - startPos.z));
        }
    }

    /**
     * параметр, который определяет, обновлены ли данные эффекта в шейдере (true, когда данные эффекта не изменялись, обновлять не нужно)
     */
    protected boolean isUpdatedShader;
    /**
     * коэффициент действия эффекта от 0 до 1.
     * общий для всех дочерних функций-региона RegionFunc.
     */
    private float coefficient;
    /**
     * дочерние функции-регионы
     */
    private final List<RegionFunc> regionFuncs;
    public EffectRegion() {
        this.regionFuncs = new ArrayList<>();
        this.coefficient = 0.0f;
    }

    /**
     * добавить функцию-регион
     * @param startPos начальное положение региона(у функции-региона), которое задает область действия функции-региона
     * @param endPos конечное положение региона(у функции-региона), которое задает область действия функции-региона
     * @param func собственно функция, в функции-регионе
     */
    public void addRegionFunc(Vector startPos, Vector endPos, IEffectRegionFunc func) {
        regionFuncs.add(new RegionFunc(startPos, endPos, func));
    }

    /**
     * вернуть обновленное значение коэффициента действия эффекта в зависимости от положения персонажа
     * @param location положение персонажа в базовом мировом пространстве
     * @return обновленный коэффициент
     */
    public float getCoefficient(Vector location) {
        for (RegionFunc regionFunc : regionFuncs)
            if (regionFunc.isInRegion(location)) {
                regionFunc.setCoefficient(location);
                break;
            }
        return coefficient;
    }

    /**
     * @param location положение персонажа в базовом мировом пространстве
     * @return true, если положение персонажа находится в области действия любого функции-региона
     */
    public boolean isInEffectRegion(Vector location) {
        for (RegionFunc regionFunc : regionFuncs)
            if (regionFunc.isInRegion(location))
                return true;
        return false;
    }
}
