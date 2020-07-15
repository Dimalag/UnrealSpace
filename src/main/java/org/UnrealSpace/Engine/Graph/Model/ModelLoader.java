package org.UnrealSpace.Engine.Graph.Model;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.UnrealSpace.Helpers.Utils;

public class ModelLoader {
    public static Model loadModel(String folder, String fileName) throws Exception {
        List<String> lines = Utils.readAllLines(folder + fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<TempObject3D> tempObject3DS = new ArrayList<>();
        List<Material> materials = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    // Geometric vertex
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                    break;
                case "vt":
                    // Texture coordinate
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(vec2f);
                    break;
                case "vn":
                    // Vertex normal
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vec3fNorm);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    tempObject3DS.get(tempObject3DS.size()-1).addFace(face);
                    break;
                case "g":
                    TempObject3D tempObject3D = new TempObject3D(tempObject3DS.size(), tokens[1]);
                    tempObject3DS.add(tempObject3D);
                    break;

                case "usemtl":
                    Material usingMaterial = null;
                    for (Material material : materials)
                        if (material.getName().equals(tokens[1]))
                            usingMaterial = material;
                    tempObject3DS.get(tempObject3DS.size() - 1).setMaterial(usingMaterial);
                    break;
                case "mtllib":
                    materials.addAll(loadMaterials(folder, tokens[1]));
                    break;
                default:
                    // Ignore other lines
                    break;
            }
        }
        return createModel(folder, fileName, vertices, textures, normals, tempObject3DS, materials);
    }

    private static Model createModel(String folder, String fileName, List<Vector3f> posList, List<Vector2f> textCoordList, List<Vector3f> normList,
                                     List<TempObject3D> tempObject3DS, List<Material> materials) throws Exception {
        ArrayList<Object3D> object3DS = new ArrayList<>(tempObject3DS.size());
        for (TempObject3D tempObject3D : tempObject3DS)
            object3DS.add(tempObject3D.createObject3D(posList, textCoordList, normList));

        Model model = new Model(folder, fileName, object3DS, materials);
        for (Object3D obj : object3DS)
            obj.setModel(model);
        return model;
    }

    private static List<Material> loadMaterials(String folder, String fileName) throws Exception {
        List<String> lines = Utils.readAllLines(folder + fileName);

        Material materialI = null;
        List<Material> materials = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = line.strip().split("\\s+");
            switch (tokens[0]) {
                case "newmtl":
                    if (materialI != null)
                        materials.add(materialI);
                    materialI = new Material(tokens[1]);
                    break;
                case "Ka":
                    assert materialI != null;
                    materialI.setAmbientColour(new Vector4f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]), 1.0f));
                    break;
                case "Kd":
                    assert materialI != null;
                    materialI.setDiffuseColour(new Vector4f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]), 1.0f));
                    break;
                case "Ks":
                    assert materialI != null;
                    materialI.setSpecularColour(new Vector4f(Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]), 1.0f));
                    break;
                case "map_Kd":
                    assert materialI != null;
                    if (tokens[1].contains(":\\"))//полный путь
                        materialI.setTexture(new Texture(tokens[1]));
                    else//относительный путь
                        materialI.setTexture(new Texture("src/main/resources/"+((folder.charAt(0) == '/') ? folder.substring(1) : folder) + tokens[1]));
                    break;
                default:
                    break;
            }
        }

        if (materialI != null)
            materials.add(materialI);
        return materials;
    }

    protected static class TempObject3D {
        private int indexInModel;
        private String name;
        private List<Face> faces;
        private Material material;
        public TempObject3D(int indexInModel, String name) {
            this.indexInModel = indexInModel;
            this.name = name;
            faces = new ArrayList<>();
        }

        public void addFace(Face face) {
            faces.add(face);
        }
        public void setMaterial(Material material) {
            this.material = material;
        }

        /**
         * создает объект из параметров загружаемой модели, копирует данные в собственные массивы и создает по ним Object3D
         * @param posList список координат модели
         * @param textCoordList список текстурных координат модели
         * @param normList список нормалей модели
         * @return созданный объект
         * @throws Exception Object3D конструктор
         */
        public Object3D createObject3D(List<Vector3f> posList, List<Vector2f> textCoordList, List<Vector3f> normList) throws Exception {
            //вычисление отступа, то есть минимального индекса для координат, текстур и нормалей
            int minPosIndex = faces.get(0).getFaceVertexIndices()[0].idxPos;
            int maxPosIndex = faces.get(0).getFaceVertexIndices()[0].idxPos;
            for (Face face: faces) {
                IdxGroup[] groups = face.getFaceVertexIndices();
                for (IdxGroup group: groups) {
                    if (minPosIndex > group.idxPos)
                        minPosIndex = group.idxPos;
                    if (maxPosIndex < group.idxPos)
                        maxPosIndex = group.idxPos;
                }
            }
            // размер соответствующих массивов будет равен
            int countVertices = maxPosIndex - minPosIndex + 1;
            float[] posArr = new float[countVertices * 3];
            float[] textCoordArr = new float[countVertices * 2];
            float[] normArr = new float[countVertices * 3];
            List<Integer> incisesList = new ArrayList<>();

            for (Face face : faces) {
                IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
                for (IdxGroup indValue : faceVertexIndices)
                    processFaceVertex(indValue, minPosIndex, posList, textCoordList, normList, posArr, textCoordArr, normArr, incisesList);
            }

            int[] indicesArr = incisesList.stream().mapToInt((Integer v) -> v).toArray();
            Object3D object3D = new Object3D(indexInModel, posArr, textCoordArr, normArr, indicesArr, name);
            object3D.setMaterial(material);
            return object3D;
        }

        private static void processFaceVertex(IdxGroup indexGroup, int offsetIndex,
                                              List<Vector3f> posList, List<Vector2f> textCoordList, List<Vector3f> normList,
                                              float[] posArr, float[] texCoordArr, float[] normArr, List<Integer> indicesList) {

            // Set index for vertex coordinates
            int posIndex = indexGroup.idxPos - offsetIndex;

            if (indexGroup.idxPos >= 0) {
                Vector3f posCoord = posList.get(indexGroup.idxPos);
                posArr[posIndex * 3] = posCoord.x;
                posArr[posIndex * 3 + 1] = posCoord.y;
                posArr[posIndex * 3 + 2] = posCoord.z;
            }
            // Reorder texture coordinates
            if (indexGroup.idxTextCoord >= 0) {
                Vector2f textCoord = textCoordList.get(indexGroup.idxTextCoord);
                texCoordArr[posIndex * 2] = textCoord.x;
                texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
            }
            // Reorder vector normals
            if (indexGroup.idxVecNormal >= 0) {
                Vector3f vecNorm = normList.get(indexGroup.idxVecNormal);
                normArr[posIndex * 3] = vecNorm.x;
                normArr[posIndex * 3 + 1] = vecNorm.y;
                normArr[posIndex * 3 + 2] = vecNorm.z;
            }
            indicesList.add(posIndex);
        }
    }

    protected static class Face {

        /**
         * List of idxGroup groups for a face triangle (3 vertices per face).
         */
        private IdxGroup[] idxGroups;
        //private int IdTexture;

        public Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];
            // Parse the lines
            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        private IdxGroup parseLine(String line) {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
            if (length > 1) {
                // It can be empty if the obj does not define text coords
                String textCoord = lineTokens[1];
                idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
                if (length > 2) {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return idxGroup;
        }

        public IdxGroup[] getFaceVertexIndices() {
            return idxGroups;
        }
    }

    protected static class IdxGroup {

        public static final int NO_VALUE = -1;

        public int idxPos;

        public int idxTextCoord;

        public int idxVecNormal;

        public IdxGroup() {
            idxPos = NO_VALUE;
            idxTextCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }
}
