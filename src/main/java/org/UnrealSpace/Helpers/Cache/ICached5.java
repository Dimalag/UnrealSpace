package org.UnrealSpace.Helpers.Cache;

import org.UnrealSpace.Helpers.IEqualable;

public interface ICached5 {
    class Cache5<T1 extends IEqualable<T1>, T2 extends IEqualable<T2>,
                          T3 extends IEqualable<T3>, T4 extends IEqualable<T4>,
                          T5 extends IEqualable<T5>>
                          implements IEqualable<Cache5<T1, T2, T3, T4, T5>>{
        private T1 value1;
        private T2 value2;
        private T3 value3;
        private T4 value4;
        private T5 value5;
        public Cache5() {}
        public Cache5(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
            set(value1, value2, value3, value4, value5);
        }
        public void set(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
            this.value5 = value5;
        }
        @Override
        public boolean equal(Cache5<T1, T2, T3, T4, T5> cache) {
            if (cache == null || value1 == null || value2 == null ||
                    value3 == null || value4 == null || value5 == null)
                return false;
            return value1.equal(cache.value1) &&
                    value2.equal(cache.value2) &&
                    value3.equal(cache.value3) &&
                    value4.equal(cache.value4) &&
                    value5.equal(cache.value5);
        }
    }
}
