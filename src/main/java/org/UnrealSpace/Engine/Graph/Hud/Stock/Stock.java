package org.UnrealSpace.Engine.Graph.Hud.Stock;

import org.UnrealSpace.Engine.Graph.Model.GameItem;
import org.UnrealSpace.Engine.Graph.Model.ModelLoader;
import org.UnrealSpace.Engine.Window;
import org.UnrealSpace.Helpers.Vector;

import java.util.ArrayList;
import java.util.List;

//инвентарь
public class Stock extends GameItem {
    public static int STOCK_MAX = 8;

    private final List<StockItem> stockItems;
    private final float width, height;

    public Stock(String folder, String filename, Window window) throws Exception {
        super(ModelLoader.loadModel(folder,filename).getObject3DS().get(0));
        stockItems = new ArrayList<>();
        height = window.getHeight()/7f;
        width = 8*height;
        setScale(new Vector(height/2.5f));
        setLocation(window.getWidth()/2.0f, window.getHeight()*0.95f,0);
    }

    public void setStockItem(int index, StockItem item) {

    }

    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
}
