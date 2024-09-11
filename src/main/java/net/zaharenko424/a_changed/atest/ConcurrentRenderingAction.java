package net.zaharenko424.a_changed.atest;

import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.RecursiveAction;

public class ConcurrentRenderingAction extends RecursiveAction {

    private final Spliterator<Integer> spliterator;
    private final List<ModelPart.Quad> quads;

    public ConcurrentRenderingAction(Spliterator<Integer> spliterator, List<ModelPart.Quad> quads){
        this.spliterator = spliterator;
        this.quads = quads;
    }

    @Override
    protected void compute() {
        Spliterator<Integer> newSplit = spliterator.trySplit();

        if(newSplit != null){
            RecursiveAction action = new ConcurrentRenderingAction(newSplit, quads);
            action.fork();
            doRender();
            action.join();
            return;
        }
        doRender();
    }

    private void doRender(){
        spliterator.forEachRemaining(i -> {
            ModelPart.Quad quad = quads.get(i);
            //quad.compile(); precompute everything? but why use multithreading just for writing? check out embeddium's poseStack caching
            //     have another map with poseStack state, etc. mapped to modelParts since only modelParts can be accessed outside package
        });
    }
}