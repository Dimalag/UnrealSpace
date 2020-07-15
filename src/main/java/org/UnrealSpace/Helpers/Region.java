package org.UnrealSpace.Helpers;

import org.UnrealSpace.Database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс области. Образует двумя весторами куб с началом в startPos и концом в endPos
 */
public class Region implements ISaveLoadable {
    protected final Vector startPos = new Vector();
    protected final Vector endPos = new Vector();

    public Region (Vector startPos, Vector endPos) {
        set(startPos, endPos);
        try {
            if (!check()) {
                throw new Exception("Wrong Region: cannot create region with 0 volume");
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            endPos.add(Approximate.EPSILON, Approximate.EPSILON, Approximate.EPSILON);
        }
    }

    public Region (float[] positions) {
        set(new Vector(positions[0], positions[1], positions[2]),
             new Vector(positions[0], positions[1], positions[2]));
        for (int i = 0; i < positions.length; i+=3) {
            startPos.x = Math.min(startPos.x, positions[i]);
            endPos.x = Math.max(endPos.x, positions[i]);
            startPos.y = Math.min(startPos.y, positions[i+1]);
            endPos.y = Math.max(endPos.y, positions[i+1]);
            startPos.z = Math.min(startPos.z, positions[i+2]);
            endPos.z = Math.max(endPos.z, positions[i+2]);
        }
        try {
            if (!check()) {
                throw new Exception("Wrong Region: cannot create region with 0 volume");
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            endPos.add(Approximate.EPSILON, Approximate.EPSILON, Approximate.EPSILON);
        }
    }

    public Region(int id) {
        load(id);
    }

    public Vector getDeltaVector() { return new Vector(endPos).sub(startPos); }
    public Vector getCenterVector() {return new Vector(startPos).add(getDeltaVector().mul(0.5f)); }
    /**
     * @param source регион, из которого происходит преобразование
     * @param dest регион, в который происходит преобразование
     * @return вектор масштабов преобразования из региона source в регион dest
     */
    public static Vector getScaleRegions(Region source, Region dest) {
        Vector deltaDest = new Vector(dest.endPos).sub(dest.startPos),
                deltaSource = new Vector(source.endPos).sub(source.startPos);
        return new Vector(deltaDest.x / deltaSource.x,
                            deltaDest.y / deltaSource.y,
                            deltaDest.z / deltaSource.z);
    }

    /**
     * @param source регион, из которого происходит преобразование
     * @param dest регион, в который происходит преобразование
     * @return вектор смещения преобразования из региона source в регион dest
     */
    public static Vector getStartLocationRegions(Region source, Region dest) {
        Vector scale = getScaleRegions(source, dest);
        return new Vector(dest.startPos).sub(new Vector(source.startPos).mul(scale.x, scale.y, scale.z));
        //Vector deltaDestHalf = new Vector(dest.endPos).sub(dest.startPos).mul(0.5f);
        //return new Vector(dest.startPos).sub(deltaDestHalf);
    }

    /**
     * @return whether region is not empty
     */
    private boolean check() {
        return !(startPos.x == endPos.x || startPos.y == endPos.y || startPos.z == endPos.z);
    }

    private void set(Vector startPos, Vector endPos) {
        this.startPos.set(Math.min(startPos.x, endPos.x),
                Math.min(startPos.y, endPos.y),
                Math.min(startPos.z, endPos.z));
        this.endPos.set(Math.max(startPos.x, endPos.x),
                Math.max(startPos.y, endPos.y),
                Math.max(startPos.z, endPos.z));
    }

    @Override
    public int save() {
        Database database = Database.getInstance();
        return database.save("save_region", null,
                startPos.x, startPos.y, startPos.z,
                endPos.x, endPos.y, endPos.z);
    }

    @Override
    public void load(int id) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM regions WHERE id_region = " + id);
            rs.next();
            startPos.set(rs.getFloat("startx"),
                    rs.getFloat("starty"),
                    rs.getFloat("startz"));
            endPos.set(rs.getFloat("endx"),
                    rs.getFloat("endy"),
                    rs.getFloat("endz"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
