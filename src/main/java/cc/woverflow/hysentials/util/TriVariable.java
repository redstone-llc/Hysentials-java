package cc.woverflow.hysentials.util;

public class TriVariable<E, T, C> {
    E first;
    T second;
    C third;
    public TriVariable(E first, T second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public E getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    public void setFirst(E first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public void setThird(C third) {
        this.third = third;
    }
}
