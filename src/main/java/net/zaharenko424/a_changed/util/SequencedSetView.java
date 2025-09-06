package net.zaharenko424.a_changed.util;

import com.google.common.collect.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SequencedSetView <E> implements SequencedSet<E> {

    public static final SequencedSet<?> EMPTY = new SequencedSetView<>(List.of());

    private final List<E> list;
    private SequencedSetView<E> reversed;

    public static <E> SequencedSet<E> of(){
        return (SequencedSet<E>) EMPTY;
    }

    public static <E> SequencedSet<E> of(E element){
        return new SequencedSetView<>(List.of(element));
    }

    public static <E> int indexOf(SequencedSet<E> set, E element){
        int i = 0;
        for(E e : set){
            if(e == element) return i;
            i++;
        }
        return -1;
    }

    public static <E> E byIndex(SequencedSet<E> set, int index){
        int i = 0;
        for(E obj : set){
            if(i == index) return obj;
            i++;
        }

        return null;
    }

    public SequencedSetView(List<E> list){
        this.list = list;
    }

    @Override
    public SequencedSet<E> reversed() {
        if(reversed == null) reversed = new SequencedSetView<>(list.reversed());
        return reversed;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return Iterators.unmodifiableIterator(list.iterator());
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return list.toArray();
    }

    @Override
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
