package org.UnrealSpace.Engine.Graph.Effect.SubEffect;

import org.UnrealSpace.Engine.Graph.Effect.ComplexEffect;
import org.UnrealSpace.Engine.Graph.Effect.ComplexRotationEffect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.UnrealSpace.Helpers.UniformBuffer;
import org.UnrealSpace.Helpers.Vector;
import org.UnrealSpace.Helpers.Float;
import org.javatuples.*;
import org.joml.Matrix4f;

import java.util.List;

public class EffectBuffer extends UniformBuffer {
    /**
     * размер памяти количеств эффектов (3*int=3*4=12) перед массивом структур
     */
    private static final int SIZE_TO_PERSON_LOCATION_AND_COUNTS = 32;

    /**
     * количество простых эффектов, под которых выделена память
     */
    private static final int SIMPLE_EFFECTS_COUNT = 16;
    /**
     * размер одного простого эффекта
     */
    private static final int SIZE_SIMPLE_EFFECT = 64;

    /**
     * количество сложных эффектов, под которых выделена память
     */
    private static final int COMPLEX_EFFECTS_COUNT = 16;
    /**
     * количество матриц поворота сложных эффектов, под которых выделена память
     */
    public static final int COMPLEX_TRANSFORMATION_MATRICES_COUNT = 30;
    /**
     * размер одного сложного эффекта
     */
    private static final int SIZE_COMPLEX_EFFECT = 176 + COMPLEX_TRANSFORMATION_MATRICES_COUNT * 64;

    /**
     * размер памяти для массива типов или очереди индексов всех эффектов
     */
    private static final int SIZE_TRANSFORMATIONS_QUEUE = 16 * (SIMPLE_EFFECTS_COUNT + COMPLEX_EFFECTS_COUNT);

    /**
     * @param indexShaderBinding индекс для привязки к интерфейсу шейдера(binding = ...)
     */
    public EffectBuffer(int indexShaderBinding) {
        super(indexShaderBinding,
                SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE
                + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + COMPLEX_EFFECTS_COUNT * SIZE_COMPLEX_EFFECT);
    }

    /**
     * @param personLocation положение персонажа в мировом пространстве
     * @param simpleCount кол-во простых эффектов
     * @param complexCount кол-во сложных эффектов
     */
    public void setPersonLocationCounts(Vector personLocation, int simpleCount, int complexCount) {
        super.setSubData(0, personLocation); //положение персонажа в мировом пространстве
        super.setSubData(12, simpleCount); //кол-во простых эффектов countSimpleTransformations
        super.setSubData(16, complexCount); //кол-во сложных эффектов countComplexTransformations
        super.setSubData(20, simpleCount + complexCount); //кол-во всех эффектов countTransformations
    }

    /**
     * @param queue очередь эффектов
     */
    public void setQueue(List<Pair<Integer, Integer>> queue) {
        for (int i = 0; i < queue.size(); i++) {
            super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 16 * i, queue.get(i).getValue0());
            super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + SIZE_TRANSFORMATIONS_QUEUE + 16 * i, queue.get(i).getValue1());
        }
    }

    /**
     * Установить данные действующих простых эффектов
     * @param index индекс простого эффекта
     * @param value устанавливаемый простой эффект
     * @param personCameraLocation расположение камеры
     * @throws Exception если индекс простого эффекта больше количества простых эффектов, под которых выделена память
     */
    public void setSubDataArrays(int index, SimpleEffect value, Vector personCameraLocation) throws Exception {
        if (index < SIMPLE_EFFECTS_COUNT)
            super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + index * SIZE_SIMPLE_EFFECT,
                    value.getEffectMatrix(personCameraLocation));
        else
            throw new Exception(String.format("EffectBuffer setSubData: received %d, allocated: %d", index, SIMPLE_EFFECTS_COUNT));
    }

    /**
     * Установить данные действующих сложных эффектов
     * @param index индекс сложного эффекта
     * @param value устанавливаемый сложный эффект
     * @param personCameraLocation расположение камеры
     * @throws Exception если индекс сложного эффекта больше количества сложных эффектов, под которых выделена память
     */
    public void setSubDataArrays(int index, ComplexEffect value, Vector personCameraLocation) throws Exception {
        if (index < COMPLEX_EFFECTS_COUNT) {
            if (value instanceof ComplexRotationEffect) {
                ComplexRotationEffect complexRotationEffect = (ComplexRotationEffect)value;
                if (!complexRotationEffect.isStaticInitializedInShader()) {
                    //set static data
                    Septet<Vector, Float, Vector, Vector, Matrix4f[], Matrix4f, Matrix4f> data = complexRotationEffect.getStaticDataAndSetShader().getValue();

                    super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT,
                            data.getValue0());
                    super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT + 12,
                            data.getValue1().value);
                    super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT + 16,
                            data.getValue2());
                    super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT + 32,
                            data.getValue3());

                    Matrix4f[] rotationMatrices = data.getValue4();
                    for (int i=0; i < rotationMatrices.length; i++)
                        super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT + 48 + 64*i,
                                rotationMatrices[i]);

                    super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT + 48 + 64*COMPLEX_TRANSFORMATION_MATRICES_COUNT,
                            data.getValue5());
                    super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT + 112 + 64*COMPLEX_TRANSFORMATION_MATRICES_COUNT,
                            data.getValue6());
                }

                if (!complexRotationEffect.isDynamicUpdatedInShader()) {
                    //set dynamic data
                    Float coefficient = complexRotationEffect.getDynamicDataAndUpdateShader(personCameraLocation).getValue();
                    super.setSubData(SIZE_TO_PERSON_LOCATION_AND_COUNTS + 2 * SIZE_TRANSFORMATIONS_QUEUE + SIMPLE_EFFECTS_COUNT * SIZE_SIMPLE_EFFECT + index * SIZE_COMPLEX_EFFECT + 28,
                            coefficient.value);
                }
            }
        } else
            throw new Exception(String.format("EffectBuffer setSubData: received %d, allocated: %d", index, COMPLEX_EFFECTS_COUNT));
    }
}
