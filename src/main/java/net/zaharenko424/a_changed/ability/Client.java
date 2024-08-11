package net.zaharenko424.a_changed.ability;

import java.util.function.Supplier;

public class Client {

    static  <T> T get(Supplier<T> supplier){
        return supplier.get();
    }
}
