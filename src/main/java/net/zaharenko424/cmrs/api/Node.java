package net.zaharenko424.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@ParametersAreNonnullByDefault
public interface Node {

    default String name(){
        return "";
    }

    Vector3f translation();

    Vector3f rotation();

    Vector3f scale();

    void applyTransformation(PoseStack poseStack);

    void resetPose();

    Set<? extends Mesh> getMesh();//TODO merge meshes into 1? -> Mesh getMesh(); but will need separate nodes for separate materials

    Node getNode(String name);

    Set<? extends Node> getChildren();
}
