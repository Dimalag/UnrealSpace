package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegion;
import org.UnrealSpace.Helpers.Wrapper;
import org.joml.Matrix4f;
import org.UnrealSpace.Helpers.Vector;
import org.UnrealSpace.Engine.Graph.Matrix.TransformationMatrix;

/**
 * Абстрактный класс эффекта
 * содержит матрицу преобразования и регион эффекта
 * служит для получения матрицы преобразования
 */
public abstract class Effect extends EffectRegion {
    /**
     * параметр, который определяет, были ли заданы статичные данные эффекта в шейдере
     */
    private boolean isInitializedInShader = false;

    /**
     * параметр, который определяет, применяется ли эффект при преобразовании вручную(камеры)
     */
    protected final boolean manuallyCalculated;
    protected final Vector effectLocation;
    protected Effect(Vector effectLocation, boolean manuallyCalculated) {
        this.manuallyCalculated = manuallyCalculated;
        this.effectLocation = effectLocation;
    }

    public final boolean isInEffectRegion(Vector location) {
        return super.isInEffectRegion(location);
    }
    public final boolean isManuallyCalculated() { return manuallyCalculated; }

    /**
     * @return true, если были заданы статичные данные эффекта в шейдере
     */
    public final boolean isStaticInitializedInShader() { return isInitializedInShader; }
    /**
     * возвращает статичные данные эффекта, подразумевая, что они будут заданы в шейдер, задает isInitializedInShader true
     * @return null
     */
    public <T> Wrapper<T> getStaticDataAndSetShader() {
        isInitializedInShader = true;
        return null;
    }

    /**
     * отменяет статичные данные эффекта заданными в шейдер, устанавливает isInitializedInShader в false
     */
    public void unsetStaticData() {
        isInitializedInShader = false;
    }


    /**
     * @return true, динамические данные эффекта были обновлены в шейдере (нет изменений)
     */
    public final boolean isDynamicUpdatedInShader() { return isUpdatedShader; }
    /**
     * возвращает динамические данные эффекта, подразумевая, что они будут обновлены в шейдере, задает isUpdatedShader true
     * @param personCameraLocation положение персонажа в базовом мировом пространстве
     * @return null
     */
    public <T> Wrapper<T> getDynamicDataAndUpdateShader(Vector personCameraLocation) {
        isUpdatedShader = true;
        return null;
    }
    ///** !!! удалена из-за того, что данные эффектов могут быть изменены только вследствие изменения коэффициента, а этот механизм инкапсулирован в EffectRegion !!!
    // * динамические данные эффекта следует обновить в шейдер (были изменения), задает isUpdatedShader false
    // */
    //public final void shouldUpdateDynamicDataInShader() { isUpdatedShader = false; }
}