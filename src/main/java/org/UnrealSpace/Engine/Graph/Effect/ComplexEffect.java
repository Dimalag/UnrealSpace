package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegion;
import org.UnrealSpace.Engine.Graph.Matrix.TransformationMatrix;
import org.UnrealSpace.Helpers.Vector;
import org.UnrealSpace.Helpers.Wrapper;

public abstract class ComplexEffect extends Effect {
    protected ComplexEffect(Vector effectLocation, boolean manuallyCalculated) {
        super(effectLocation, manuallyCalculated);
    }

    //public abstract <T> Wrapper<T> getData(Vector location);
}
