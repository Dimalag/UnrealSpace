package org.UnrealSpace.Engine.Graph.Matrix;

import org.UnrealSpace.Helpers.Cache.ICached3;
import org.UnrealSpace.Helpers.Region;
import org.UnrealSpace.Helpers.Vector;
import org.UnrealSpace.Helpers.Quaternion;

/**
 * выигрыш кэширование дает примерно в два раза, но его эффективно использовать, когда объект совсем не меняется
 */
public class ModelMatrix extends Matrix implements ICached3 {
    private final boolean useCache;
    private final Cache3<Vector, Quaternion, Vector> cache;

    public ModelMatrix(boolean useCache) {
        this.useCache = useCache;
        if (useCache)
            cache = new Cache3<>();
        else
            cache = null;
    }

    public void set(Vector location, Quaternion rotation, Vector scale, Region region) {
        if (useCache) {
            assert cache != null;
            if (!cache.equal(new Cache3<>(location, rotation, scale))) {
                matrix.identity().
                        translate(location).
                        translate(region.getCenterVector().mul(scale)).
                        rotate(rotation.getQuaternion()).
                        translate(region.getCenterVector().mul(scale).negate()).
                        scale(scale);
                cache.set(location, rotation, scale);
            }
        } else {
            matrix.identity().translate(location).
                    rotate(rotation.getQuaternion()).
                    scale(scale);
        }
    }
}