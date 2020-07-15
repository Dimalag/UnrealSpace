package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegion;
import org.UnrealSpace.Engine.Graph.Matrix.RotationTransformationMatrix;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.Quaternion;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;

public class RotationEffect extends SimpleEffect {
    private final Quaternion rotation;
    public RotationEffect(Vector effectLocation, Quaternion rotation, boolean manuallyCalculated) {
        super(new RotationTransformationMatrix(true), effectLocation, manuallyCalculated);
        this.rotation = rotation;
    }

    @Override
    public Matrix4f getEffectMatrix(Vector location) {
        ((RotationTransformationMatrix)transformationMatrix).set(effectLocation, rotation, new Float(super.getCoefficient(location)));
        return transformationMatrix.get();
    }
}
