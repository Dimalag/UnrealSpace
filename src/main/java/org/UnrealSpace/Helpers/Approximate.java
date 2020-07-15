package org.UnrealSpace.Helpers;

public class Approximate {
    public final static float EPSILON = 0.001f;

    public static boolean equal(float a, float b) {
        return (java.lang.Math.abs(a - b) <= EPSILON);
    }

    public static boolean greater(float a, float b) {
        return (a - b > EPSILON);
    }
    public static boolean less(float a, float b) {
        return (b - a > EPSILON);
    }

    public static boolean greaterEqual(float a, float b) {
        return (a - b >= -EPSILON);
    }
    public static boolean lessEqual(float a, float b) {
        return (b - a >= -EPSILON);
    }
}

