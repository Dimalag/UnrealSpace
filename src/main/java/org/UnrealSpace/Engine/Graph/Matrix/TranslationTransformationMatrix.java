package org.UnrealSpace.Engine.Graph.Matrix;

import org.UnrealSpace.Engine.Graph.Matrix.TransformationMatrix;
import org.UnrealSpace.Helpers.Cache.ICached2;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.Vector;

public class TranslationTransformationMatrix extends TransformationMatrix implements ICached2 {
    private final boolean useCache;
    private final ICached2.Cache2<Vector, Float> cache;

    public TranslationTransformationMatrix(boolean useCache) {
        this.useCache = useCache;
        if (useCache)
            cache = new ICached2.Cache2<>();
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

    public void set(Vector translation, Float coefficient) {
        if (useCache) {
            assert cache != null;
            if (!cache.equal(new ICached2.Cache2<>(translation, coefficient))) {
                matrix.identity().translate(getScaleCoefficient(translation.x, coefficient),
                        getScaleCoefficient(translation.y, coefficient),
                        getScaleCoefficient(translation.z, coefficient));
                cache.set(translation, coefficient);
            }
        } else {
            matrix.identity().scale(new Vector(getScaleCoefficient(translation.x, coefficient),
                    getScaleCoefficient(translation.y, coefficient),
                    getScaleCoefficient(translation.z, coefficient)));
        }
    }

    private float getScaleCoefficient(float translation, Float coefficient) {
        return translation * coefficient.value;
    }
}
