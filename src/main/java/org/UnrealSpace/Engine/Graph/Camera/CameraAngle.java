package org.UnrealSpace.Engine.Graph.Camera;

import org.UnrealSpace.Database.Database;
import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.ISaveLoadable;
import org.UnrealSpace.Helpers.Vector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CameraAngle implements ISaveLoadable {
    // Потядок вращений для углов Эйлера:
    //
    // вращает ССК вокруг изначальной x на 'roll' (крен)
    // вращает ССК вокруг изначальной y на 'pitch' (тангаж)
    // вращает ССК вокруг изначальной z на 'yaw' (рыскание).
    //               .
    //              /|\ yaw axis
    //               |     __.
    //  ._        ___|      /| pitch axis
    // _||\       \\ |-.   /
    // \|| \_______\_|__\_/_______
    //  | _ _   o o o_o_o_o o   /_\__  ________\ roll axis
    //  //  /_______/    /__________/          /
    // /_,-'       //   /
    //            /__,-'
    //

    //X тангаж(pitch)   +вверх/-вниз
    //Y рыскание(yaw)   +left/-right
    //Z крен(roll)      +right/-left
    private final Angle yaw; // рыскание - по горизонтали
    private final Angle pitch; // тангаж - по вертикали
    private final Angle roll; // крен - вдоль продольной оси

    public CameraAngle() {
        yaw = new Angle(-90.0f);
        pitch = new Angle(0.0f);
        roll = new Angle(0.0f);
    }
    public CameraAngle(Angle yaw, Angle pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = new Angle(0.0f);
    }
    public CameraAngle(Angle yaw, Angle pitch, Angle roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public Vector getFrontVec() {
        float pitchRad = pitch.getAngleRadians(), yawRad = yaw.getAngleRadians();
        return new Vector(
                (float) (Math.cos(pitchRad) * Math.cos(yawRad)),
                (float) (Math.cos(pitchRad) * Math.sin(yawRad)),
                (float) (Math.sin(pitchRad)));
    }
    public void setFromFrontVec(Vector vec) {
        float pitchRad = (float) Math.asin(vec.y);
        float yawRad = (float) Math.atan2(vec.x, vec.z);
        yaw.setAngleRadians(yawRad);
        pitch.setAngleRadians(pitchRad);
    }

    public Vector getRightVec(Vector worldUp) {
        return getFrontVec().cross(worldUp);
    }

    public Vector getUpVec(Vector worldUp) {
        Vector front = getFrontVec();
        Vector right = new Vector(front).cross(worldUp);
        return right.cross(front);
    }

    public void rotate (Angle deltaYaw, Angle deltaPitch) {
        yaw.addAngle(deltaYaw);
        pitch.addAngle(deltaPitch);
    }

    @Override
    public int save() {
        Database database = Database.getInstance();
        return database.save("save_camera_angle", null, yaw.getAngleDegrees(), pitch.getAngleDegrees(), roll.getAngleDegrees());
    }

    @Override
    public void load(int id) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cameras_angles WHERE id_camera_angle = " + id);
            rs.next();
            yaw.setAngleDegrees(rs.getFloat("yaw"));
            pitch.setAngleDegrees(rs.getFloat("pitch"));
            roll.setAngleDegrees(rs.getFloat("roll"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Angle getYaw() {
        return yaw;
    }
    public void setYaw(Angle yaw) {
        this.yaw.setAngleDegrees(yaw.getAngleDegrees());
    }

    public Angle getPitch() {
        return pitch;
    }
    public void setPitch(Angle pitch) {
        this.pitch.setAngleDegrees(pitch.getAngleDegrees());
    }

    public Angle getRoll() {
        return roll;
    }
    public void setRoll(Angle roll) {
        this.roll.setAngleDegrees(roll.getAngleDegrees());
    }
}
