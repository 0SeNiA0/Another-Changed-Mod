package net.zaharenko424.a_changed.util;

import java.util.Spliterator;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class ConcurrentAction<T> extends RecursiveAction {

    protected final Spliterator<T> spliterator;
    protected final int splitThreshold;
    protected final Consumer<T> action;

    public ConcurrentAction(Spliterator<T> spliterator, int splitThreshold, Consumer<T> action) {
        this.spliterator = spliterator;
        this.splitThreshold = splitThreshold;
        this.action = action;
    }

    @Override
    protected void compute() {
        long size = spliterator.estimateSize();
        if(size < splitThreshold) {
            performAction();
            return;
        }

        Spliterator<T> newSplit = spliterator.trySplit();

        if(newSplit == null){
            performAction();
            return;
        }

        RecursiveAction action = new ConcurrentAction<>(newSplit, splitThreshold, this.action);
        action.fork();

        performAction();

        action.join();
    }

    protected void performAction() {
        spliterator.forEachRemaining(action);
    }
}
