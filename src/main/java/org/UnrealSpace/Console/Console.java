package org.UnrealSpace.Console;

import java.util.Scanner;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Engine.GameEngine;
import org.UnrealSpace.Engine.Graph.Light.TimeOfDay;
import org.UnrealSpace.Engine.IGameLogic;
import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;
import org.UnrealSpace.Helpers.Vector;


public class Console implements Runnable {
    private static Console CONSOLE_SINGLE_INSTANCE = null;

    private int loadedGameId;
    private boolean loadGame;
    private boolean readConsole;
    private final GameEngine gameEngine;
    private final IGameLogic gameLogic;
    private final PersonCamera personCamera;
    private final Scanner scanner;

    public static Console getInstance(boolean readConsole, GameEngine gameEngine) {
        if (CONSOLE_SINGLE_INSTANCE == null)
            CONSOLE_SINGLE_INSTANCE = new Console(readConsole, gameEngine);
        return CONSOLE_SINGLE_INSTANCE;
    }

    private Console(boolean readConsole, GameEngine gameEngine) {
        this.readConsole = readConsole;
        this.gameEngine = gameEngine;
        this.gameLogic = gameEngine.getGameLogic();
        this.personCamera = gameLogic.getPersonCamera();
        this.scanner = new Scanner(System.in);
        this.loadedGameId = -1;
        this.loadGame = false;
    }

    @Override
    public void run() {
        while (this.readConsole)
            try {
                parseConsole();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("console input error");
            }
    }

    private void parseConsole() {
        Vector locationWB = personCamera.getCamera().getLocationWB();
        Vector locationWT = personCamera.getCamera().getLocationWT();

        System.out.print('>');
        String line = scanner.nextLine();
        String[] tokens = line.strip().split("\\s+");

        Database db = Database.getInstance();
        int temp;
        switch (tokens[0].toLowerCase()) {
            case "getposition": //напечатать положение персонажа
                System.out.printf("location in world base:\t\t\t\t(%f,\t%f,\t%f)\n",locationWB.x, locationWB.y, locationWB.z);
                System.out.printf("location in world transformed base:\t(%f,\t%f,\t%f)\n",locationWT.x, locationWT.y, locationWT.z);
                break;
            case "setvelocity": //установить скорость персонажа
                try {
                    float velocity = Float.parseFloat(tokens[1]);
                    personCamera.setVelocity(velocity);
                } catch (Exception ex) {
                    System.out.println("invalid velocity");
                }
                break;
            case "getvelocity": //напечатать скорость персонажа
                System.out.printf("person velocity = %f\n", personCamera.getVelocity());
                break;
            case "setdaylength":
                try {
                    float daylength = Float.parseFloat(tokens[1]);
                    gameLogic.getLightScene().getSun().setSecForFullDay(daylength);
                } catch (Exception ex) {
                    System.out.println("invalid day length");
                }
                break;
            case "settimeofday":
                switch (tokens[1]) {
                    case "dawn":
                        gameLogic.getLightScene().getSun().setTimeOfDay(TimeOfDay.Dawn);
                        break;
                    case "midday":
                        gameLogic.getLightScene().getSun().setTimeOfDay(TimeOfDay.Midday);
                        break;
                    case "dusk":
                        gameLogic.getLightScene().getSun().setTimeOfDay(TimeOfDay.Dusk);
                        break;
                    case "midnight":
                        gameLogic.getLightScene().getSun().setTimeOfDay(TimeOfDay.Midnight);
                        break;
                    default:
                        System.out.println("invalid time of day");
                        break;
                }
                break;
            case "load":
                setLoadGameRequest();
                break;
            case "save":
                saveGame();
                break;
            case "clear":
                db.clearAll();
                System.out.println("databases cleared");
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("invalid comand");
                break;
        }
        if (readConsole)
            parseConsole();
    }

    public void setReadConsole(boolean readConsole) {
        this.readConsole = readConsole;
    }

    public void setLoadGameRequest() {
        System.out.println("Input number game");
        String loadedGameStrId = scanner.nextLine();
        loadedGameId = Integer.parseInt(loadedGameStrId);
        loadGame = true;
        System.out.println("Loading game");
    }
    public int loadGame() {
        loadGame = false;
        int temp = loadedGameId;
        loadedGameId = -1;
        return temp;
    }
    public boolean isLoadGame() {
        return loadGame;
    }

    private void saveGame() {
        String gameName = scanner.nextLine();
        gameLogic.setGameName(gameName);
        gameLogic.save();
        System.out.println("game '" + gameName + "' saved");
    }
}
