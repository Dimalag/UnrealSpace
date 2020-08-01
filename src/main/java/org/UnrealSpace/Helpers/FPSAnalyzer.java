package org.UnrealSpace.Helpers;

import org.javatuples.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FPSAnalyzer {

    private final List<Pair<String, Integer>> dataFPS;
    private int realFPS;

    public FPSAnalyzer() {
        realFPS = 0;
        dataFPS = new ArrayList<>();
    }

    public int getFPS() { return realFPS; }

    public void calculateFPS(float elapsedTime) {
        realFPS = (int) (1.0f / elapsedTime);
    }

    public void printFPS() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss: ");
        Date dateobj = new Date();

        Pair<String, Integer> dataFPSPair = new Pair<>(df.format(dateobj), realFPS);
        dataFPS.add(dataFPSPair);
        System.out.println(dataFPSPair.getValue0() + dataFPSPair.getValue1());
    }

    public void printFPSAnalytics() {
        int sumDataFPS = 0;
        int minFPS = Integer.MAX_VALUE;
        int maxFPS = Integer.MIN_VALUE;

        for (Pair<String, Integer> pair : dataFPS) {
            sumDataFPS += pair.getValue1();

            if (minFPS > pair.getValue1())
                minFPS = pair.getValue1();

            if (maxFPS < pair.getValue1())
                maxFPS = pair.getValue1();
        }

        int averageFPS = sumDataFPS / dataFPS.size();
        System.out.println("Average FPS: "+averageFPS);
        System.out.println("Max FPS: "+maxFPS);
        System.out.println("Min FPS: "+minFPS);
    }
}
