package org.UnrealSpace.Engine.Graph.Hud.Stock;

import org.UnrealSpace.Engine.Graph.Model.GameItem;
import org.UnrealSpace.Engine.Graph.Model.ModelLoader;
import org.UnrealSpace.Engine.Graph.Model.Texture;
import org.UnrealSpace.Engine.Window;
import org.UnrealSpace.Helpers.Vector;

public class StockItem extends GameItem {
    private final float width, height;

    StockItem(String folder, String filename, Stock stock, int index) throws Exception {
        super(ModelLoader.loadModel(folder,filename).getObject3DS().get(0));
        if (index >= Stock.STOCK_MAX)
            throw new Exception("wrong StockItem StockItem(): index is more then MAX");
        height = stock.getHeight()*5f/7f;
        width = height;
        Vector center = stock.getLocation();
        float startLoc = center.x-stock.getWidth()/2f+stock.getHeight()/2f;
        setScale(new Vector(height/2f));
        setLocation(startLoc+index*stock.getHeight(), center.y, center.z+0.01f);
    }
}
