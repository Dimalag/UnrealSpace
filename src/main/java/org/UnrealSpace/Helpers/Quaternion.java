package org.UnrealSpace.Helpers;

import org.UnrealSpace.Database.Database;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Quaternion implements IEqualable<org.UnrealSpace.Helpers.Quaternion>, ISaveLoadable {
    private final Angle angle = new Angle();
    private final Vector vector = new Vector();
    public Quaternion() {
    }
    public Quaternion(Angle angle, Vector vector) {
        //проверка вектора на длину 0, чтобы определить можно ли его нормализовать
        set(angle, vector);
    }
    public Quaternion(Angle angle, float x, float y, float z) {
        this(angle, new Vector(x, y, z));
    }
    public Quaternion(org.UnrealSpace.Helpers.Quaternion rotation) {
        this(rotation.angle, rotation.vector);
    }
    public Quaternion(int id) {
        load(id);
    }

    public boolean equal(org.UnrealSpace.Helpers.Quaternion quat) {
        return (vector.equal(quat.vector) && angle.equal(quat.angle));
    }

    public Quaternionf getQuaternion() {
        return new Quaternionf(new AxisAngle4f(angle.getAngleRadians(), vector));
    }
    public Quaternionf getQuaternionCoeffucuented(Float coefficient) {
        return new Quaternionf(new AxisAngle4f(angle.getAngleRadians() * coefficient.value, vector));
    }

    public void set(Quaternion rotation) {
        set(rotation.angle, rotation.vector);
    }

    public void set(Angle angle, Vector vector) {
        this.angle.setAngleDegrees(angle.getAngleDegrees());
        this.vector.set(vector);
    }

    @Override
    public int save() {
        Database database = Database.getInstance();
        return database.save("save_quaternion", null,
                angle.getAngleDegrees(), vector.x, vector.y, vector.z);
    }

    @Override
    public void load(int id) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quaternions WHERE id_quaternion = " + id);
            rs.next();
            angle.setAngleDegrees(rs.getFloat("angle"));
            vector.set(rs.getFloat("xrot"),
                    rs.getFloat("yrot"),
                    rs.getFloat("zrot"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
