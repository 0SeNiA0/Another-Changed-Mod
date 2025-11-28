package net.zaharenko424.cmrs.api;

import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Node {

    Vector3f translation();

    Vector3f rotation();

    Vector3f scale();

    void resetPose();

    boolean hasNode(String name);

    Node getNode(String name);

}
