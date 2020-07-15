package org.UnrealSpace.Helpers;

public class HelperMath {
    static public float ValueToDiapazon(float value, float left, float right)
    {
        if (left > right) { float temp = right; right = left; left = temp; }
        while (value >= right)
            value -= (right - left);
        while (value < left)
            value += (right - left);
        return value;
    }
    static public int getMin(int[] array) {
        if (array == null || array.length == 0)
            return 0;
        int min = array[0];
        for (int i = 0; i * 3 < array.length; i++)
            if (min > array[i])
                min = array[i];
        return min;
    }
}
