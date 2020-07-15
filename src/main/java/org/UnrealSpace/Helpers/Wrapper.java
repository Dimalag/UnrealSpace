package org.UnrealSpace.Helpers;

public class Wrapper<T> {
    T value;
    public Wrapper(T value) {
        setValue(value);
    }
    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }
}
