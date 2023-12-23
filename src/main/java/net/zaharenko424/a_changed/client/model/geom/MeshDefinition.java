package net.zaharenko424.a_changed.client.model.geom;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MeshDefinition {
    private final GroupDefinition root = new GroupDefinition();

    public GroupDefinition getRoot(){
        return root;
    }
}