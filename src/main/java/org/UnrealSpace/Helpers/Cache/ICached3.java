package org.UnrealSpace.Helpers.Cache;

import org.UnrealSpace.Helpers.IEqualable;

public interface ICached3 {
    class Cache3<T1 extends IEqualable<T1>, T2 extends IEqualable<T2>,
                          T3 extends IEqualable<T3>>
                          implements IEqualable<Cache3<T1, T2, T3>> {
        private T1 value1;
        private T2 value2;
        private T3 value3;
        public Cache3() {}
        public Cache3(T1 value1, T2 value2, T3 value3) {
            set(value1, value2, value3);
        }
        public void set(T1 value1, T2 value2, T3 value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }
        @Override
        public boolean equal(Cache3<T1, T2, T3> cache) {
            if (cache == null || value1 == null || value2 == null || value3 == null)
                return false;
            return value1.equal(cache.value1) &&
                    value2.equal(cache.value2) &&
                    value3.equal(cache.value3);
        }
    }
}
