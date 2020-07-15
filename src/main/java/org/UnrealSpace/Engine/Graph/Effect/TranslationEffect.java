package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Matrix.TranslationTransformationMatrix;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;

public class TranslationEffect extends SimpleEffect {
    private final Vector translation;
    public TranslationEffect(Vector effectLocation, Vector translation,  boolean manuallyCalculated) {
        super(new TranslationTransformationMatrix(true), effectLocation, manuallyCalculated);
        this.translation = translation;
    }

    @Override
    public Matrix4f getEffectMatrix(Vector location) {
        ((TranslationTransformationMatrix)transformationMatrix).set(translation, new Float(super.getCoefficient(location)));
        return transformationMatrix.get();
    }
}
