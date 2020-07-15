package org.UnrealSpace.Engine.Graph.Matrix;

import org.joml.Matrix4f;

public abstract class Matrix {
    protected final Matrix4f matrix;
    protected Matrix() {
        matrix = new Matrix4f();
    }

    public Matrix4f get() {
        return matrix;
    }
}
