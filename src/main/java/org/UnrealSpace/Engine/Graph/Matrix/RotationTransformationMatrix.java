package org.UnrealSpace.Engine.Graph.Matrix;

import org.UnrealSpace.Helpers.Cache.ICached2;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.Quaternion;
import org.UnrealSpace.Helpers.Vector;

public class RotationTransformationMatrix extends TransformationMatrix implements ICached2 {
    private final boolean useCache;
    private final Cache2<Quaternion, Float> cache;

    public RotationTransformationMatrix(boolean useCache) {
        this.useCache = useCache;
        if (useCache)
            cache = new Cache2<>();
        else
            cache = null;
    }

    /*public RotationTransformationMatrix(boolean useCache, Angle angleX, Angle angleY, Angle angleZ, Float coefficient) {
        this.useCache = useCache;
        set(angleX, angleY, angleZ, coefficient);
        if (useCache)
            cache = new Cache4<>(angleX, angleY, angleZ, coefficient);
        else
            cache = null;
    }*/

    public void set(Vector effectLocationOffset, Quaternion rotation, Float coefficient) {
        if (useCache) {
            assert cache != null;
            if (!cache.equal(new Cache2<>(rotation, coefficient))) {
                matrix.identity().translate(effectLocationOffset).
                        rotate(rotation.getQuaternionCoeffucuented(coefficient)).
                        translate(new Vector(effectLocationOffset).negate());
                cache.set(rotation, coefficient);
            }
        } else {
            matrix.identity().translate(effectLocationOffset).
                    rotate(rotation.getQuaternionCoeffucuented(coefficient)).
                    translate(new Vector(effectLocationOffset).negate());
        }
    }
}
