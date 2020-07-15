package org.UnrealSpace.Helpers;

import static java.lang.Math.*;

public class Angle implements IComparable<Angle> {
    private float angleDegrees;

    public Angle() {
        this(0.0f);
    }
    public Angle(float angleDegree) {
        setAngleDegrees(angleDegree);
    }
    public Angle(float angle, AngleType type) {
        if (type == AngleType.Degrees)
            setAngleDegrees(angle);
        else if (type == AngleType.Radians)
            setAngleRadians(angle);
    }

    public float getAngleDegrees() {
        return angleDegrees;
    }
    public void setAngleDegrees(float angleDegrees) {
        this.angleDegrees = HelperMath.ValueToDiapazon(angleDegrees, 0, 360);
    }

    public float getAngleRadians() {
        return (float) toRadians(angleDegrees);
    }
    public void setAngleRadians(float angleRadians) {
        this.angleDegrees = (float) toDegrees(angleRadians);
    }

    public void addAngle(Angle ang) {
        setAngleDegrees(angleDegrees + ang.getAngleDegrees());
    }
    public void subAngle(Angle ang) {
        setAngleDegrees(angleDegrees - ang.getAngleDegrees());
    }
    public void addAngle(float angDegree) {
        setAngleDegrees(angleDegrees + angDegree);
    }
    public void subAngle(float angDegree) {
        setAngleDegrees(angleDegrees - angDegree);
    }
    public void mulAngle(float scale) {
        setAngleDegrees(angleDegrees * scale);
    }

    @Override
    public boolean equal(Angle ang) {
        return Approximate.equal(getAngleDegrees(), ang.getAngleDegrees());
    }
    @Override
    public boolean greater(Angle ang) {
        return Approximate.greater(getAngleDegrees(), ang.getAngleDegrees());
    }
    @Override
    public boolean less(Angle ang) {
        return Approximate.less(getAngleDegrees(), ang.getAngleDegrees());
    }
    @Override
    public boolean greaterEquals(Angle ang) {
        return Approximate.greaterEqual(getAngleDegrees(), ang.getAngleDegrees());
    }
    @Override
    public boolean lessEquals(Angle ang) {
        return Approximate.lessEqual(getAngleDegrees(), ang.getAngleDegrees());
    }
}
