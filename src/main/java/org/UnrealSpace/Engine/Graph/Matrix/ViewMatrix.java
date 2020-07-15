package org.UnrealSpace.Engine.Graph.Matrix;

import org.UnrealSpace.Helpers.Cache.ICached3;
import org.UnrealSpace.Helpers.Vector;

/**
 * выигрыш кэширование дает примерно в два раза, но его эффективно использовать, когда объект не меняется совсем
 */
public class ViewMatrix extends Matrix implements ICached3 {
    private final boolean useCache;
    private final Cache3<Vector, Vector, Vector> cache;

    public ViewMatrix(boolean useCache) {
        this.useCache = useCache;
        if (useCache)
            cache = new Cache3<>();
        else
            cache = null;
    }

    public void set(Vector cameraLocation, Vector cameraCenter, Vector cameraUp) {
        /*//////////////////////////
        Timer testTimer = new Timer();
        testTimer.init();
        for (int i = 0; i < 10000; i++)
            cache.equalCaches(new CacheView(cameraLocation, cameraCenter, cameraUp));
        System.out.println("time compare: " + testTimer.getElapsedTime());
        for (int i = 0; i < 10000; i++)
            matrix.identity().lookAt(cameraLocation, cameraCenter, cameraUp);
        System.out.println("time calculate: " + testTimer.getElapsedTime());
        for (int i = 0; i < 10000; i++)
            cache.set(cameraLocation, cameraCenter, cameraUp);
        System.out.println("time set cache: " + testTimer.getElapsedTime());
        //////////////////////////*/
        if (useCache) {
            assert cache != null;
            if (!cache.equal(new Cache3<>(cameraLocation, cameraCenter, cameraUp))) {
                matrix.identity().lookAt(cameraLocation, cameraCenter, cameraUp);
                cache.set(cameraLocation, cameraCenter, cameraUp);
            }
        } else {
            matrix.identity().lookAt(cameraLocation, cameraCenter, cameraUp);
        }
    }
}