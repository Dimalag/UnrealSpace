package org.UnrealSpace.Engine.Graph.Hud;

import java.util.ArrayList;
import java.util.List;

import org.UnrealSpace.Engine.Graph.Model.GameItem;
import org.UnrealSpace.Engine.Graph.Model.Material;
import org.UnrealSpace.Engine.Graph.Model.Object3D;
import org.UnrealSpace.Helpers.Utils;

public class TextItem extends GameItem {

    private static final float ZPOS = 0.0f;

    private static final int VERTICES_PER_QUAD = 4;

    private final FontTexture fontTexture;

    private String text;

    public TextItem(String text, FontTexture fontTexture) throws Exception {
        super();
        this.text = text;
        this.fontTexture = fontTexture;
        setObject3D(buildMesh());
    }

    private Object3D buildMesh() throws Exception {
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals   = new float[0];
        List<Integer> indices   = new ArrayList<>();
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0;
        for(int i=0; i<numChars; i++) {
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);

            // Build a character tile composed by two triangles

            // Left Top vertex
            positions.add(startx); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add( (float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add(startx); // x
            positions.add((float)fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add((float)fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);

            startx += charInfo.getWidth();
        }

        float[] posArr = Utils.listToArray(positions);
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Object3D mesh = new Object3D(-1, posArr, textCoordsArr, normals, indicesArr, text);
        mesh.setMaterial(new Material(fontTexture.getTexture()));
        return mesh;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) throws Exception {
        this.text = text;
        this.getObject3D().cleanup();
        this.setObject3D(buildMesh());
    }
}