package net.zaharenko424.cmrs.api;

import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ForwardingNode implements Node {

    protected final Node delegate;

    public ForwardingNode(Node delegate){
        this.delegate = delegate;
    }

    @Override
    public Vector3f translation() {
        return delegate.translation();
    }

    @Override
    public Vector3f rotation() {
        return delegate.rotation();
    }

    @Override
    public Vector3f scale() {
        return delegate.scale();
    }

    @Override
    public void resetPose() {
        delegate.resetPose();
    }

    @Override
    public boolean hasNode(String name) {
        return delegate.hasNode(name);
    }

    @Override
    public Node getNode(String name) {
        return delegate.getNode(name);
    }
}
