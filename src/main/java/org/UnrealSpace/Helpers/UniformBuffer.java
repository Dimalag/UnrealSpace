package org.UnrealSpace.Helpers;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL46.*;

public class UniformBuffer {
    public final int SIMPLE_MATRIX_TYPE = 1;
    public final int COMPLEX_ROTATION_TYPE = 2;

    protected final int uboId;
    protected final int indexShaderBinding;

    //private final FloatBuffer buffer;

    /**
     * @param indexShaderBinding индекс для привязки к интерфейсу шейдера(binding = ...)
     * @param size размер буфера
     */
    public UniformBuffer(int indexShaderBinding, int size) {
        this.indexShaderBinding = indexShaderBinding;
        FloatBuffer uboBuffer = null;
        try {
            uboId = glGenBuffers();
            glBindBuffer(GL_UNIFORM_BUFFER, uboId);
            uboBuffer = MemoryUtil.memAllocFloat(size);
            glBufferData(GL_UNIFORM_BUFFER, uboBuffer, GL_DYNAMIC_DRAW); // выделяем size байт памяти
            glBindBuffer(GL_UNIFORM_BUFFER, 0);

            glBindBufferRange(GL_UNIFORM_BUFFER, indexShaderBinding, uboId, 0, size);
        } finally {
            MemoryUtil.memFree(uboBuffer);
        }
    }

    public void setSubData(int offset, int value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glBindBuffer(GL_UNIFORM_BUFFER, uboId);
            glBufferSubData(GL_UNIFORM_BUFFER, offset, stack.mallocInt(1).put(value).flip());
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }
    }

    public void setSubData(int offset, float value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glBindBuffer(GL_UNIFORM_BUFFER, uboId);
            glBufferSubData(GL_UNIFORM_BUFFER, offset, stack.mallocFloat(1).put(value).flip());
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }
    }

    public void setSubData(int offset, Vector vector) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glBindBuffer(GL_UNIFORM_BUFFER, uboId);
            glBufferSubData(GL_UNIFORM_BUFFER, offset, vector.get(stack.mallocFloat(3)));
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }
    }

    public void setSubData(int offset, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glBindBuffer(GL_UNIFORM_BUFFER, uboId);
            glBufferSubData(GL_UNIFORM_BUFFER, offset, value.get(stack.mallocFloat(16)));
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }
    }

    public void cleanup() {
        glDeleteBuffers(uboId);
    }
}
