package org.UnrealSpace.Engine.Graph.Matrix;

import org.UnrealSpace.Helpers.Cache.ICached2;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.Vector;

public class ScaleTransformationMatrix extends TransformationMatrix implements ICached2 {
    private final boolean useCache;
    private final Cache2<Vector, Float> cache;

    public ScaleTransformationMatrix(boolean useCache) {
        this.useCache = useCache;
        if (useCache)
            cache = new Cache2<>();
        else
            cache = null;
    }

    /*public ScaleTransformationMatrix(boolean useCache, Vector scale, Float coefficient) {
        this.useCache = useCache;
        set(scale, coefficient);
        if (useCache)
            cache = new Cache2<>(scale, coefficient);
        else
            cache = null;
    }*/

    public void set(Vector effectLocationOffset, Vector scale, Float coefficient) {
        if (useCache) {
            assert cache != null;
            if (!cache.equal(new Cache2<>(scale, coefficient))) {
                matrix.identity().
                        scale(getScaleCoefficient(scale.x, coefficient),
                        getScaleCoefficient(scale.y, coefficient),
                        getScaleCoefficient(scale.z, coefficient));
                cache.set(scale, coefficient);
            }
        } else {
            matrix.identity().
                    scale(getScaleCoefficient(scale.x, coefficient),
                    getScaleCoefficient(scale.y, coefficient),
                    getScaleCoefficient(scale.z, coefficient));
        }
    }

    private float getScaleCoefficient(float scale, Float coefficient) {
        return (scale - 1.0f) * coefficient.value + 1.0f;
    }
}
