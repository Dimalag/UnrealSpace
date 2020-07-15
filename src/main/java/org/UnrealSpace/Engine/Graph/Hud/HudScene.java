package org.UnrealSpace.Engine.Graph.Hud;

import org.UnrealSpace.Engine.Graph.Hud.Stock.KeyStockItem;
import org.UnrealSpace.Engine.Graph.Hud.Stock.Stock;
import org.UnrealSpace.Engine.Graph.Model.*;
import org.UnrealSpace.Engine.Window;
import org.UnrealSpace.Helpers.Vector;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HudScene {
    private static final Font FONT = new Font("Arial", Font.PLAIN, 30);

    private static final String CHARSET = "CP1251";

    private final List<GameItem> gameItems;
    private final List<TextItem> textItems;
    private TextItem personLocationTextItem;
    private TextItem statusTextItem;
    private Stock stock;
    private KeyStockItem key;

    public HudScene() {
        // Create list that holds the items that compose the HUD
        gameItems = new ArrayList<>();
        textItems = new ArrayList<>();
    }

    public void init(String statusText, Window window) throws Exception {
        loadTextItems(window);
        loadHudGameItems(window);
    }

    public void update(Vector personCameraLocation) throws Exception {
        setText(1,personCameraLocation.toString());
    }

    private void loadTextItems(Window window) throws Exception {
        //приветственная надпись
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        statusTextItem = new TextItem("Добро пожаловать!!!", fontTexture);
        statusTextItem.getObject3D().getMaterial().setAmbientColour(new Vector4f(0.7f, 0.5f, 0.9f, 1));
        statusTextItem.setLocation(10f, window.getHeight()-50f,0);
        textItems.add(statusTextItem);

        //положение персонажа
        personLocationTextItem = new TextItem(" ", fontTexture);
        personLocationTextItem.getObject3D().getMaterial().setAmbientColour(new Vector4f(0.5f, 0.9f, 0.6f, 1));
        personLocationTextItem.setLocation(10f, window.getHeight()-90f,0);
        textItems.add(personLocationTextItem);
    }

    private void loadHudGameItems(Window window) throws Exception {
        //создание кружочка посередине экрана
        Object3D mesh1 = ModelLoader.loadModel("/models/","circle.obj").getObject3DS().get(0);
        GameItem circleCenter = new GameItem(mesh1);
        circleCenter.setScale(new Vector(5.0f));
        circleCenter.setLocation(window.getWidth()/2.0f, window.getHeight()/2.0f,0);
        gameItems.add(circleCenter);

        //создание инвентаря
        stock = new Stock("/models/","stock.obj", window);

        gameItems.add(stock);
    }

    public List<TextItem> getStatusTextItems() {
        return textItems;
    }
    public List<GameItem> getGameItems() {
        return gameItems;
    }
    public void setKey(String folder, String filename, int index) throws Exception {
        if (key == null) {
            //создание ключа
            key = new KeyStockItem(folder, filename, stock, 0);
            gameItems.add(key);
        }
    }

    public void setText(int index, String text) throws Exception {
        if (index >= textItems.size())
            throw new Exception("wrong HudScene setStatusText: out of range");
        else
            this.textItems.get(index).setText(text);
    }

    public static Matrix4f getOrtoProjModelMatrix(GameItem gameItem, Matrix4f orthoMatrix) {
        Matrix4f modelMatrix = gameItem.getModelMatrix();
        Matrix4f orthoMatrixCurr = new Matrix4f(orthoMatrix);
        orthoMatrixCurr.mul(modelMatrix);
        return orthoMatrixCurr;
    }
}
