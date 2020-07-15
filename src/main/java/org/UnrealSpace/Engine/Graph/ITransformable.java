package org.UnrealSpace.Engine.Graph;

import org.UnrealSpace.Helpers.Vector;

public interface ITransformable {
    /**
     * @return point or vector in world base space
     */
    Vector getWB();
    /**
     * @return point or vector in world transformed space
     */
    Vector getWT();
    void setWB(float x, float y, float z);
    void addOffsetWB(Vector offset);
}
