package org.UnrealSpace.Helpers;

public class Float implements IComparable<Float> {
    public java.lang.Float value;
    public Float(Float floatValue) {
        this.value = floatValue.value;
    }
    public Float(java.lang.Float value) {
        this.value = value;
    }
    public Float(float value) {
        this.value = value;
    }

    public boolean equal(Float a) {
        return Approximate.equal(value, a.value);
    }

    public boolean greater(Float a) {
        return Approximate.greater(value, a.value);
    }
    public boolean less(Float a) {
        return Approximate.less(value, a.value);
    }

    public boolean greaterEquals(Float a) {
        return Approximate.greaterEqual(value, a.value);
    }
    public boolean lessEquals(Float a) {
        return Approximate.lessEqual(value, a.value);
    }
}
