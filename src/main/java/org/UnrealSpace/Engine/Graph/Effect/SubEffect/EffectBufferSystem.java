package org.UnrealSpace.Engine.Graph.Effect.SubEffect;

import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;
import org.UnrealSpace.Engine.Graph.Effect.ComplexEffect;
import org.UnrealSpace.Engine.Graph.Effect.Effect;
import org.UnrealSpace.Engine.Graph.Effect.SimpleEffect;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

public class EffectBufferSystem {
    private final List<Pair<Integer, Integer>> queue;
    private final List<SimpleEffect> simpleEffects;
    private final List<ComplexEffect> complexEffects;
    private final List<Effect> actualEffects;
    private final List<Effect> unactualEffects;

    public EffectBufferSystem() {
        queue = new ArrayList<>();
        simpleEffects = new ArrayList<>();
        complexEffects = new ArrayList<>();
        actualEffects = new ArrayList<>();
        unactualEffects = new ArrayList<>();
    }

    public List<Effect> getUnactualEffects() { return unactualEffects; }
    public List<Effect> getActualEffects() { return actualEffects; }
    public void addUnactualEffect(Effect unactualEffect) {
        unactualEffects.add(unactualEffect);
    }
    public void addUnactualEffects(List<Effect> unactualEffect) {
        unactualEffects.addAll(unactualEffect);
    }

    public List<Pair<Integer, Integer>> getQueue() { return queue; }
    public List<SimpleEffect> getSimpleEffects() { return simpleEffects; }
    public List<ComplexEffect> getComplexEffects() { return complexEffects; }

    public void push(Effect effect) {
        actualEffects.add(effect);
        if (effect instanceof SimpleEffect) {
            simpleEffects.add((SimpleEffect) effect);
            queue.add(new Pair<>(1, simpleEffects.size() - 1));
        } else if (effect instanceof ComplexEffect) {
            complexEffects.add((ComplexEffect) effect);
            queue.add(new Pair<>(2, complexEffects.size() - 1));
        }
    }

    public void cleanup() {
        actualEffects.clear();
        unactualEffects.clear();
        simpleEffects.clear();
        complexEffects.clear();
        queue.clear();
    }
}
