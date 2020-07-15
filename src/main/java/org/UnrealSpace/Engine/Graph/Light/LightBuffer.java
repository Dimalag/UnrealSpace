package org.UnrealSpace.Engine.Graph.Light;

import org.UnrealSpace.Helpers.UniformBuffer;

public class LightBuffer extends UniformBuffer {
    /**
     * отступ для дополнительных данных(количества структур) перед двумя массивами структур
     */
    private final int offsetToAdditionalData;
    /**
     * размер одного точечного источника света
     */
    private final int sizeOnePointLight;
    /**
     * размер одного конусообразного источника света
     */
    private final int sizeOneSpotLight;
    /**
     * количество точечных источников света, под которых выделена память
     */
    private int allocatedPointLightsCount;
    /**
     * количество конусообразных источников света, под которых выделена память
     */
    private int allocatedSpotLightsCount;

    /**
     * @param indexShaderBinding индекс для привязки к интерфейсу шейдера(binding = ...)
     * @param offsetToAdditionalData отступ для дополнительных данных(количества структур) перед массивами структур
     * @param sizeOnePointLight размер одного точечного источника света
     * @param sizeOneSpotLight размер одного конусообразного источника света
     * @param allocatedPointLightsCount количество точечных источников света, под которых выделена память
     * @param allocatedSpotLightsCount количество конусообразных источников света, под которых выделена память
     */
    public LightBuffer(int indexShaderBinding, int offsetToAdditionalData,
                       int sizeOnePointLight, int sizeOneSpotLight,
                       int allocatedPointLightsCount, int allocatedSpotLightsCount) {
        super(indexShaderBinding,
                offsetToAdditionalData + sizeOnePointLight * allocatedPointLightsCount + sizeOneSpotLight * allocatedSpotLightsCount);
        this.offsetToAdditionalData = offsetToAdditionalData;
        this.sizeOnePointLight = sizeOnePointLight;
        this.sizeOneSpotLight = sizeOneSpotLight;
        this.allocatedPointLightsCount = allocatedPointLightsCount;
        this.allocatedSpotLightsCount = allocatedSpotLightsCount;
    }

    public void setSubData(int offset, DirectionalLight directionalLight) {
        super.setSubData(offset, directionalLight.getColor());
        super.setSubData(offset + 16, directionalLight.getDirectionWT());
        super.setSubData(offset + 28, directionalLight.getIntensity());
    }

    public void setSubData(int offset, PointLight.Attenuation attenuation) {
        super.setSubData(offset, attenuation.getConstant());
        super.setSubData(offset + 4, attenuation.getLinear());
        super.setSubData(offset + 8, attenuation.getExponent());
    }

    public void setSubData(int offset, PointLight pointLight) {
        super.setSubData(offset, pointLight.getColor());
        super.setSubData(offset + 16, pointLight.getLocationWT());
        super.setSubData(offset + 28, pointLight.getIntensity());
        setSubData(offset + 32, pointLight.getAttenuation());
    }

    public void setSubData(int offset, SpotLight spotLight) {
        super.setSubData(offset, spotLight.getConeDirectionWT());
        super.setSubData(offset + 12, spotLight.getCutOffCos());
        setSubData(offset + 16, spotLight.getPointLight());
    }

    /**
     * Установить данные точечного источника
     * @param index индекс точечного источника
     * @param pointLight устанавливаемый точечный источник
     * @throws Exception если индекс точечного источника больше количества точечных источников, под которых выделена память
     */
    public void setSubDataArrays(int index, PointLight pointLight) throws Exception {
        if (index < allocatedPointLightsCount)
            setSubData(offsetToAdditionalData + index * sizeOnePointLight, pointLight);
        else
            throw new Exception(String.format("EffectBuffer setSubData: received %d, allocated: %d", index, allocatedPointLightsCount));
    }

    /**
     * Установить данные конусообразного источника
     * @param index индекс конусообразного источника
     * @param spotLight устанавливаемый конусообразный источник
     * @throws Exception если индекс конусообразного источника больше количества конусообразных источников, под которых выделена память
     */
    public void setSubDataArrays(int index, SpotLight spotLight) throws Exception {
        if (index < allocatedPointLightsCount)
            setSubData(offsetToAdditionalData + allocatedPointLightsCount * sizeOnePointLight + index * sizeOneSpotLight, spotLight);
        else
            throw new Exception(String.format("EffectBuffer setSubData: received %d, allocated: %d", index, allocatedSpotLightsCount));
    }
}
