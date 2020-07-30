package org.UnrealSpace.Game;

import org.UnrealSpace.Console.Console;
import org.UnrealSpace.Engine.GameEngine;
import org.UnrealSpace.Engine.IGameLogic;

public class Main {
    public static void main(String[] args) {
        try {
            boolean vSync = false;
            IGameLogic gameLogic = new DummyGame("Игра1");
            GameEngine gameEng = new GameEngine("Unreal Space",
                    600, 480, vSync, gameLogic);
            Thread t1 = new Thread(gameEng);
            t1.start();
            Console console = Console.getInstance(true, gameEng);
            Thread t2 = new Thread(console);
            t2.start();
            t1.join();
            System.exit(0);
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}