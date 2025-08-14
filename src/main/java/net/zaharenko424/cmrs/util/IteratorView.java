package net.zaharenko424.cmrs.util;

import java.util.Iterator;

class IteratorView<E> implements Iterator<E> {

    private final Iterator<E> iterator;

    public IteratorView(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
