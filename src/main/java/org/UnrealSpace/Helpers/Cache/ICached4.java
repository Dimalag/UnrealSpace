package org.UnrealSpace.Helpers.Cache;

import org.UnrealSpace.Helpers.IEqualable;

public interface ICached4 {
    class Cache4<T1 extends IEqualable<T1>, T2 extends IEqualable<T2>,
                          T3 extends IEqualable<T3>, T4 extends IEqualable<T4>>
                          implements IEqualable<Cache4<T1, T2, T3, T4>> {
        private T1 value1;
        private T2 value2;
        private T3 value3;
        private T4 value4;
        public Cache4() {}
        public Cache4(T1 value1, T2 value2, T3 value3, T4 value4) {
            set(value1, value2, value3, value4);
        }
        public void set(T1 value1, T2 value2, T3 value3, T4 value4) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
        }
        @Override
        public boolean equal(Cache4<T1, T2, T3, T4> cache) {
            if (cache == null || value1 == null || value2 == null ||
                    value3 == null || value4 == null)
                return false;
            return value1.equal(cache.value1) &&
                    value2.equal(cache.value2) &&
                    value3.equal(cache.value3) &&
                    value4.equal(cache.value4);
        }
    }
}
