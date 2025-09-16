package net.zaharenko424.a_changed.util;

public class Thing<A, B, C, D> {

    private A first;
    private B second;
    private C third;
    private D fourth;

    public Thing(A first, B second, C third, D fourth){
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public static <A, B, C, D> Thing<A, B, C, D> ofFirst(A first){
        return new Thing<>(first, null, null, null);
    }

    public static <A, B, C, D> Thing<A, B, C, D> ofSecond(B second){
        return new Thing<>(null, second, null, null);
    }

    public static <A, B, C, D> Thing<A, B, C, D> ofThird(C third){
        return new Thing<>(null, null, third, null);
    }

    public static <A, B, C, D> Thing<A, B, C, D> ofFourth(D fourth){
        return new Thing<>(null, null, null, fourth);
    }

    public A first() {
        return first;
    }

    public Thing<A, B, C, D> first(A first) {
        this.first = first;
        return this;
    }

    public B second() {
        return second;
    }

    public Thing<A, B, C, D> second(B second) {
        this.second = second;
        return this;
    }

    public C third() {
        return third;
    }

    public Thing<A, B, C, D> third(C third) {
        this.third = third;
        return this;
    }

    public D fourth() {
        return fourth;
    }

    public Thing<A, B, C, D> fourth(D fourth) {
        this.fourth = fourth;
        return this;
    }
}
