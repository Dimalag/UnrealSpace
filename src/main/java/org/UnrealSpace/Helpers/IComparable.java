package org.UnrealSpace.Helpers;

public interface IComparable<T> extends IEqualable<T> {
    boolean greater(T a);
    boolean less(T a);

    boolean greaterEquals(T a);
    boolean lessEquals(T a);
}
