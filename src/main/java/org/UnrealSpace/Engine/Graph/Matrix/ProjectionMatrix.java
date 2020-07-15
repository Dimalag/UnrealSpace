package org.UnrealSpace.Engine.Graph.Matrix;

import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.Cache.ICached5;
import org.UnrealSpace.Helpers.Float;

/**
 * выигрыш кэширование дает примерно в два раза, но его эффективно использовать, когда объект не меняется совсем
 * time calculate: 3.8179988E-4
 * time set cache: 1.2600096E-5
 * time compare: 2.0000152E-7
 */
public class ProjectionMatrix extends Matrix implements ICached5 {
    private final boolean useCache;
    private final Cache5<Angle, Float, Float, Float, Float> cache;

    public ProjectionMatrix(boolean useCache) {
        this.useCache = useCache;
        if (useCache)
            cache = new Cache5<>();
        else
            cache = null;
    }

    public void set(Angle fov, Float width, Float height, Float zNear, Float zFar) {
        /*//////////////////////////
        Timer testTimer = new Timer();
        testTimer.init();
        for (int i = 0; i < 10000; i++)
            cache.equalCaches(new CacheProjection(fov.getAngleRadians(), width, height, zNear, zFar));
        System.out.println("time compare: " + testTimer.getElapsedTime());
        for (int i = 0; i < 10000; i++)
            matrix.identity().setPerspective(fov.getAngleRadians(), width / height, zNear, zFar);
        System.out.println("time calculate: " + testTimer.getElapsedTime());
        for (int i = 0; i < 10000; i++)
            cache.set(fov.getAngleRadians(), width, height, zNear, zFar);
        System.out.println("time set cache: " + testTimer.getElapsedTime());
        //////////////////////////*/
        if (useCache) {
            assert cache != null;
            if (!cache.equal(new Cache5<>(fov, width, height, zNear, zFar))) {
                matrix.identity().setPerspective(fov.getAngleRadians(), width.value / height.value, zNear.value, zFar.value);
                cache.set(fov, width, height, zNear, zFar);
            }
        } else {
            matrix.identity().setPerspective(fov.getAngleRadians(), width.value / height.value, zNear.value, zFar.value);
        }
    }
}
