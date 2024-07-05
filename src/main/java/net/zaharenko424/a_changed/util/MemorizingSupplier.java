package net.zaharenko424.a_changed.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MemorizingSupplier <T> implements Supplier<T> {

    private Supplier<T> supplier;
    private T cache;

    public MemorizingSupplier(Supplier<T> supplier){
        this.supplier = supplier;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull MemorizingSupplier<T> of(Supplier<T> supplier){
        return new MemorizingSupplier<>(supplier);
    }

    @Override
    public T get() {
        if(supplier != null) {
            cache = supplier.get();
            supplier = null;
        }
        return cache;
    }
}