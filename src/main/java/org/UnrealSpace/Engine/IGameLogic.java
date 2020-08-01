package org.UnrealSpace.Engine;

import org.UnrealSpace.Engine.Graph.Light.LightScene;
import org.UnrealSpace.Engine.Graph.Model.GameScene;
import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;
import org.UnrealSpace.Helpers.ISaveLoadable;

public interface IGameLogic extends ISaveLoadable {
    void init(Window window) throws Exception;
    void input(Window window, MouseInput mouseInput, KeyboardInput keyboardInput);
    void update(float interval, int fps, MouseInput mouseInput, KeyboardInput keyboardInput);
    void render(Window window);
    void cleanup();

    GameScene getGameScene();
    LightScene getLightScene();
    PersonCamera getPersonCamera();
    String getGameName();
    void setGameName(String gameName);
}
