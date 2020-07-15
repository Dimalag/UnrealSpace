package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegion;
import org.UnrealSpace.Engine.Graph.Matrix.TransformationMatrix;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;

public abstract class SimpleEffect extends Effect {
    protected final TransformationMatrix transformationMatrix;
    protected SimpleEffect(TransformationMatrix transformationMatrix, Vector effectLocation, boolean manuallyCalculated) {
        super(effectLocation, manuallyCalculated);
        this.transformationMatrix = transformationMatrix;
    }

    public abstract Matrix4f getEffectMatrix(Vector location);
}
