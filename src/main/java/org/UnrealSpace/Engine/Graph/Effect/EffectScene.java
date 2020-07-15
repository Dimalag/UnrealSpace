package org.UnrealSpace.Engine.Graph.Effect;

import org.UnrealSpace.Engine.Graph.Camera.PersonCamera;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectBufferSystem;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegion;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegionConstFunc;
import org.UnrealSpace.Engine.Graph.Effect.SubEffect.EffectRegionVectorFunc;
import org.UnrealSpace.Helpers.Angle;
import org.UnrealSpace.Helpers.Quaternion;
import org.UnrealSpace.Helpers.Vector;

import java.util.List;

public class EffectScene {
    private final EffectBufferSystem effectSystem;

    public EffectScene() {
        effectSystem = new EffectBufferSystem();
    }

    public void init() {
        setupEffects();
    }

    public List<Effect> getActualEffects() { return effectSystem.getActualEffects(); }
    public List<SimpleEffect> getActualSimpleEffects() { return effectSystem.getSimpleEffects(); }
    public EffectBufferSystem getEffectSystem() { return effectSystem; }

    private void setupEffects() {
        try {
            float mainOffset = 0;
            EffectRegionConstFunc zeroFunc = new EffectRegionConstFunc(0.0f);
            EffectRegionConstFunc oneFunc = new EffectRegionConstFunc(1.0f);
            EffectRegionVectorFunc vectorFunc1 = new EffectRegionVectorFunc(new Vector(-1, 0, 0));
            /*EffectRegion effectRegion1 = new EffectRegion();
            effectRegion1.addRegionFunc(new Vector(mainOffset+2, -100, -100),
                    new Vector(mainOffset+2.5f, 100, 100), zeroFunc);
            effectRegion1.addRegionFunc(new Vector(mainOffset+1, -100, -100),
                    new Vector(mainOffset+2, 100, 100), vectorFunc1);
            effectRegion1.addRegionFunc(new Vector(mainOffset+0.5f, -100, -100),
                    new Vector(mainOffset+1, 100, 100), oneFunc);
            RotationEffect rotationEffect = new RotationEffect(effectRegion1, new Vector(mainOffset,-0.5f,2.4f),
                    new Quaternion(new Angle(180), 1,0,0), true);*/

            ScaleEffect scaleEffect = new ScaleEffect(new Vector(10f, 1f, 1f), true);
            scaleEffect.addRegionFunc(new Vector(mainOffset+3, -100, -100),
                    new Vector(mainOffset+100f, 100, 100), zeroFunc);
            scaleEffect.addRegionFunc(new Vector(mainOffset+2, -100, -100),
                    new Vector(mainOffset+3, 100, 100), vectorFunc1);
            scaleEffect.addRegionFunc(new Vector(mainOffset+1.5f, -100, -100),
                    new Vector(mainOffset+2, 100, 100), oneFunc);

            /*EffectRegion effectRegion3 = new EffectRegion();
            effectRegion3.addRegionFunc(new Vector(mainOffset+1.0f, -100, -100),
                    new Vector(mainOffset+1.5f, 100, 100), zeroFunc);
            effectRegion3.addRegionFunc(new Vector(mainOffset+0.8f, -100, -100),
                    new Vector(mainOffset+1.0f, 100, 100), vectorFunc1);
            effectRegion3.addRegionFunc(new Vector(mainOffset+0.0f, -100, -100),
                    new Vector(mainOffset+0.8f, 100, 100), oneFunc);
            TranslationEffect translateEffect = new TranslationEffect(effectRegion3, new Vector(),
                    new Vector(0f, 0f, -1.7f), false);*/

            ComplexRotationEffect complexRotationEffect = new ComplexRotationEffect(new Vector(mainOffset+1, 0, 1.8f),
                    new Vector(mainOffset-5, 0, 1.8f), new Vector(mainOffset+1, 0, 1.8f),
                    new Vector(-1f, 0f, 0f),6f);
            complexRotationEffect.addRegionFunc(new Vector(mainOffset+2, -100, -100),
                    new Vector(mainOffset+2.5f, 100, 100), zeroFunc);
            complexRotationEffect.addRegionFunc(new Vector(mainOffset+1.0f, -100, -100),
                    new Vector(mainOffset+2, 100, 100), vectorFunc1);
            complexRotationEffect.addRegionFunc(new Vector(mainOffset+0.5f, -100, -100),
                    new Vector(mainOffset+1.0f, 100, 100), oneFunc);

            effectSystem.addUnactualEffect(scaleEffect);
            //effectSystem.addUnactualEffect(rotationEffect);
            //effectSystem.addUnactualEffect(translateEffect);
            effectSystem.addUnactualEffect(complexRotationEffect);

            /*
            EffectRegionVectorFunc vectorFunc2 = new EffectRegionVectorFunc(new Vector(0, -1, 0));
            EffectRegion effectRegion2 = new EffectRegion();
            effectRegion2.addRegionFunc(new Vector(-1000, 3, -100),
                    new Vector(1000, 4, 100), vectorFunc2);
            TranslationEffect translationEffect = new TranslationEffect(effectRegion2, new Vector(1.5f, 0, 0), false);
            unactualEffects.add(translationEffect);

            EffectRegionVectorFunc vectorFunc3 = new EffectRegionVectorFunc(new Vector(0, -1, 0));
            EffectRegion effectRegion3 = new EffectRegion();
            effectRegion3.addRegionFunc(new Vector(-1000, 2, -100),
                    new Vector(1000, 3, 100), vectorFunc3);
            TranslationEffect translationEffect2 = new TranslationEffect(effectRegion3, new Vector(-1.5f, 0, 0), false);
            unactualEffects.add(translationEffect2);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update(PersonCamera personCamera) {
        //check unactual effects
        List<Effect> unactuals = effectSystem.getUnactualEffects();
        for (Effect unactual : unactuals)
            if (unactual.isInEffectRegion(personCamera.getCamera().getLocationWB())) {
                effectSystem.push(unactual);
                unactuals.remove(unactual);
                break;
            }

        // Update camera WT location
        personCamera.getCamera().updateLocation(getActualSimpleEffects());
    }
}
