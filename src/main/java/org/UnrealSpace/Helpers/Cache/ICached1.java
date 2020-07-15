package org.UnrealSpace.Helpers.Cache;

import org.UnrealSpace.Helpers.IEqualable;

public interface ICached1 {
    class Cache1<T1 extends IEqualable<T1>> implements IEqualable<Cache1<T1>> {
        private T1 value1;
        public Cache1() {}
        public Cache1(T1 value1) {
            set(value1);
        }
        public void set(T1 value1) {
            this.value1 = value1;
        }
        @Override
        public boolean equal(Cache1<T1> cache) {
            if (cache == null || value1 == null)
                return false;
            return value1.equal(cache.value1);
        }
    }
}
