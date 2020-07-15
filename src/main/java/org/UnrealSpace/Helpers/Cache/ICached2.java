package org.UnrealSpace.Helpers.Cache;

import org.UnrealSpace.Helpers.IEqualable;

public interface ICached2 {
    class Cache2<T1 extends IEqualable<T1>, T2 extends IEqualable<T2>> implements IEqualable<Cache2<T1, T2>> {
        private T1 value1;
        private T2 value2;
        public Cache2() {}
        public Cache2(T1 value1, T2 value2) {
            set(value1, value2);
        }
        public void set(T1 value1, T2 value2) {
            this.value1 = value1;
            this.value2 = value2;
        }
        @Override
        public boolean equal(Cache2<T1, T2> cache) {
            if (cache == null || value1 == null || value2 == null)
                return false;
            return value1.equal(cache.value1) &&
                    value2.equal(cache.value2);
        }
    }
}