package org.UnrealSpace.Engine;

import org.UnrealSpace.Helpers.Timer;
import org.UnrealSpace.Console.Console;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 100;

    private final Window window;

    private final Timer timer;

    private final IGameLogic gameLogic;

    private final MouseInput mouseInput;
    private final KeyboardInput keyboardInput;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = new Timer();
        mouseInput = new MouseInput();
        keyboardInput = new KeyboardInput();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
        mouseInput.init(window);
    }

    protected void gameLoop() {
        float elapsedTime; //время, прошедшее за один игровой цикл
        float accumulator = 0f; //переменная - аккумулятор прошедшего времени
        float interval = 1f / TARGET_UPS; //интервал обновления логики игры

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync(); //синхронизация
            }
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    protected void input() {
        gameLogic.input(window, mouseInput, keyboardInput);
        Console console = Console.getInstance(true, this);
        if (console.isLoadGame())
            gameLogic.load(console.loadGame());
    }

    protected void update(float interval) {
        gameLogic.update(interval, mouseInput, keyboardInput);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }

    protected void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
    }

    public IGameLogic getGameLogic() {
        return gameLogic;
    }
    public Window getWindow() { return window; }
}