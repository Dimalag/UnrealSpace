package org.UnrealSpace.Engine.Graph.Light;

import org.UnrealSpace.Helpers.Angle;

public class Sun {
    private DirectionalLight directionalLight;
    private Angle lightAngle;
    /**
     * Скорость вращения солнца в дне.
     * Равен углу поворота в одну миллисекунду
     */
    private float speedRotation;

    /**
     * Создает солнце, которое движется по небосводу с определенной скоростью
     * @param directionalLight глобальный направленный свет
     * @param lightAngle угол, под которым расположено солнце. Рассвет -90, Полдень 0, Закат 90, Полночь 180.
     * @param secForFullDay количество секунд для полного цикла дня
     */
    public Sun(DirectionalLight directionalLight, Angle lightAngle, float secForFullDay) throws Exception {
        this.directionalLight = directionalLight;
        this.lightAngle = lightAngle;
        setSecForFullDay(secForFullDay);
    }

    /**
     * Создает солнце, которое движется по небосводу с определенной скоростью
     * @param directionalLight глобальный направленный свет
     * @param timeOfDay угол, под которым расположено солнце. Рассвет -90, Полдень 0, Закат 90, Полночь 180.
     * @param secForFullDay количество секунд для полного цикла дня
     */
    public Sun(DirectionalLight directionalLight, TimeOfDay timeOfDay, float secForFullDay) throws Exception {
        this(directionalLight, new Angle(), secForFullDay);
        setTimeOfDay(timeOfDay);
    }

    public void update(float interval) {
        // Update directional light direction, intensity and colour
        lightAngle.setAngleDegrees(lightAngle.getAngleDegrees() + interval * speedRotation);
        if (lightAngle.getAngleDegrees() >= 90 && lightAngle.getAngleDegrees() <= 270) {
            directionalLight.setIntensity(0);
        } else if ((lightAngle.getAngleDegrees() <= 280 && lightAngle.getAngleDegrees() > 270) ||
                (lightAngle.getAngleDegrees() >= 80 && lightAngle.getAngleDegrees() < 90)) {
            float negPosAngleDegrees = lightAngle.getAngleDegrees() > 270 ?
                    lightAngle.getAngleDegrees() - 360 :
                    lightAngle.getAngleDegrees();
            float factor = 1 - (Math.abs(negPosAngleDegrees) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.8f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = lightAngle.getAngleRadians();
        directionalLight.getDirectionWB().x = (float) Math.sin(angRad);
        directionalLight.getDirectionWB().z = (float) Math.cos(angRad);
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setTimeOfDay(TimeOfDay timeOfDay) {
        switch (timeOfDay) {
            case Dawn:
                lightAngle.setAngleDegrees(-90);
                break;
            case Midday:
                lightAngle.setAngleDegrees(0);
                break;
            case Dusk:
                lightAngle.setAngleDegrees(90);
                break;
            case Midnight:
                lightAngle.setAngleDegrees(180);
                break;
        }
    }

    public void setSecForFullDay(float secForFullDay) throws Exception {
        if (secForFullDay > 0)
            this.speedRotation = 360.0f/secForFullDay;
        else
            throw new Exception("wrong Sun setSecForFullDay: "+secForFullDay);
    }
}
