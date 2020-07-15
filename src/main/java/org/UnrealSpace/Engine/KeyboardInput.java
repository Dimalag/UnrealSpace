package org.UnrealSpace.Engine;

import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput {
    // x -> 1  forward/ -1 back,
    private float moveForward;
    // y -> 1  left/    -1 right
    private float moveLeft;

    public void input(Window window) {
        if (window.isKeyPressed(GLFW_KEY_W)) {
            moveForward = 1.0f; // forward
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            moveForward = -1.0f; // back
        } else {
            moveForward = 0.0f;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            moveLeft = 1.0f; // left
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            moveLeft = -1.0f; // right
        } else {
            moveLeft = 0.0f;
        }
    }

    public Vector2f getCameraMoveSide() {
        return new Vector2f(moveForward, moveLeft);
    }
}
