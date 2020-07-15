package org.UnrealSpace.Engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.Vector;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private static final float MOUSE_SENSITIVITY = 0.1f;

    private final Vector2d currentPos;
    private final Vector2f offsetVec;

    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;
    private boolean mouseInversion = true;

    public MouseInput() {
        //previousPos = new Vector2d(0, 0);
        currentPos = new Vector2d(0, 0);
        offsetVec = new Vector2f();
    }

    public void init(Window window) {
        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> inWindow = entered);
        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
        Vector2i location = window.getLocation();
        currentPos.set(location.x + window.getWidth() / 2.0,
                location.y + window.getHeight() / 2.0);
        glfwSetCursorPos(window.getWindowHandle(), currentPos.x, currentPos.y);
    }

    public void input(Window window) {
        Vector2i location = window.getLocation();
        Vector2i centerWindow = new Vector2i(location.x + window.getWidth() / 2,
                location.y + window.getHeight() / 2);
        offsetVec.x = (float)(centerWindow.y - currentPos.y);
        offsetVec.y = (float)(centerWindow.x - currentPos.x);
        glfwSetCursorPos(window.getWindowHandle(), centerWindow.x, centerWindow.y);
    }

    public void getOffsetAngles(Angle deltaYawOut, Angle deltaPitchOut) {
        if (mouseInversion) {
            deltaYawOut.setAngleDegrees(-offsetVec.y * MOUSE_SENSITIVITY);
            deltaPitchOut.setAngleDegrees(-offsetVec.x * MOUSE_SENSITIVITY);
        } else {
            deltaYawOut.setAngleDegrees(offsetVec.y * MOUSE_SENSITIVITY);
            deltaPitchOut.setAngleDegrees(offsetVec.x * MOUSE_SENSITIVITY);
        }
    }

    public Vector getDirectionVec() {
        Angle pitch = new Angle(0), yaw = new Angle(0);
        float pitchRad = pitch.getAngleRadians(), yawRad = yaw.getAngleRadians();
        return new Vector(
                (float) (Math.cos(pitchRad) * Math.cos(yawRad)),
                (float) (Math.sin(pitchRad)),
                (float) (Math.cos(pitchRad) * Math.sin(yawRad)));
    }
    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }
    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
