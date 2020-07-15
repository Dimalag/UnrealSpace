package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectBuffer;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegion;
import org.UnrealSpace.Helpers.Float;
import org.UnrealSpace.Helpers.Vector;
import org.UnrealSpace.Helpers.Wrapper;
import org.javatuples.Octet;
import org.javatuples.Septet;
import org.joml.Matrix4f;

public class ComplexRotationEffect extends ComplexEffect {
    private final Vector direction;
    private final float distance;
    private final Vector startLocation;
    private final Vector endLocation;

    private final Matrix4f offsetTranslate;
    private final Matrix4f offsetTranslateNegative;

    private Matrix4f[] rotationMatrices = null;
    /**
     * Создает эффект сложного вращения
     * @param effectLocation местоположение эффекта (для установления оси вращения)
     * @param startLocation начальное положение, с которого начинает действовать эффект (для шейдеров)
     * @param endLocation конечное положение, до которого действует эффект (для шейдеров)
     * @param direction направление оси вращения
     * @param distance расстояние до полного оборота
     */
    public ComplexRotationEffect(Vector effectLocation,
                                 Vector startLocation, Vector endLocation,
                                 Vector direction, float distance) {
        super(effectLocation, false); //сложное вращение не действует на персонажа и т.д.
        this.direction = direction;
        this.distance = distance;

        this.startLocation = startLocation;
        this.endLocation = endLocation;

        offsetTranslate = new Matrix4f().identity().translate(effectLocation);
        offsetTranslateNegative = new Matrix4f().identity().translate(new Vector(effectLocation).negate());

        init_rotation_matrices();
    }

    private void init_rotation_matrices() {
        if (rotationMatrices == null) {
            int count = EffectBuffer.COMPLEX_TRANSFORMATION_MATRICES_COUNT;
            rotationMatrices = new Matrix4f[count];
            for (int i = 0; i < count; i++)
                rotationMatrices[i] = new Matrix4f().identity().rotate(2*(float)Math.PI*i/count, direction);
        }
    }

    /**
     * возвращает статичные данные эффекта, подразумевая, что они будут заданы в шейдер, задает isInitializedInShader true
     * а именно: направление вращения, расстояние до полного оборота, стартовое и конечное положение действия эффекта
     * массив матриц поворота
     * матрицы смещения вращения в прямую и обратную сторону
     * @return null
     */
    @Override
    @SuppressWarnings("unchecked")
    public Wrapper<Septet<Vector, Float, Vector, Vector, Matrix4f[], Matrix4f, Matrix4f>> getStaticDataAndSetShader() {
        super.getStaticDataAndSetShader();
        return new Wrapper<>(
                new Septet<>(
                        direction, new Float(distance),
                        startLocation,
                        endLocation,
                        rotationMatrices,
                        offsetTranslate, offsetTranslateNegative
                ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Wrapper<Float> getDynamicDataAndUpdateShader(Vector location) {
        return new Wrapper<>(new Float(super.getCoefficient(location)));
    }
}
