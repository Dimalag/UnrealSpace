package org.UnrealSpace.Helpers;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Vector extends Vector3f implements IEqualable<Vector> {
    public Vector() { super(); }
    public Vector(float d) { super(d); }
    public Vector(float x, float y, float z) { super(x, y, z); }
    public Vector(Vector3fc v) { super(v); }
    @Override
    public boolean equal(Vector a) {
        if (a == null)
            return false;
        return Approximate.equal(x, a.x) &&
                Approximate.equal(y, a.y) &&
                Approximate.equal(z, a.z);
    }

    @Override
    public Vector add(Vector3fc v) {
        this.x = this.x + v.x();
        this.y = this.y + v.y();
        this.z = this.z + v.z();
        return this;
    }
    @Override
    public Vector add(float x, float y, float z) {
        this.x = this.x + x;
        this.y = this.y + y;
        this.z = this.z + z;
        return this;
    }
    @Override
    public Vector sub(Vector3fc v) {
        this.x = x - v.x();
        this.y = y - v.y();
        this.z = z - v.z();
        return this;
    }

    @Override
    public Vector cross(Vector3fc v) {
        float rx = Math.fma(y, v.z(), -z * v.y());
        float ry = Math.fma(z, v.x(), -x * v.z());
        float rz = Math.fma(x, v.y(), -y * v.x());
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    @Override
    public Vector mul(float scalar) {
        this.x = this.x * scalar;
        this.y = this.y * scalar;
        this.z = this.z * scalar;
        return this;
    }
    @Override
    public Vector mul(float x, float y, float z) {
        this.x = this.x * x;
        this.y = this.y * y;
        this.z = this.z * z;
        return this;
    }
    @Override
    public Vector div(float x, float y, float z) {
        this.x = this.x / x;
        this.y = this.y / y;
        this.z = this.z / z;
        return this;
    }
    @Override
    public Vector div(Vector3fc v) {
        this.x = this.x / v.x();
        this.y = this.y / v.y();
        this.z = this.z / v.z();
        return this;
    }
    @Override
    public Vector div(float scalar) {
        float inv = 1.0f / scalar;
        this.x = this.x * inv;
        this.y = this.y * inv;
        this.z = this.z * inv;
        return this;
    }

    @Override
    public Vector negate() {
        this.x = -x;
        this.y = -y;
        this.z = -z;
        return this;
    }
}
