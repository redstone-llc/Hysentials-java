package cc.woverflow.hysentials.util;

public class QuadVariable<E, T, C, P> {
    E first;
    T second;
    C third;
    P fourth;
    public QuadVariable(E first, T second, C third, P fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
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
    public P getFourth() {
        return fourth;
    }
    public void setFourth(P fourth) {
        this.fourth = fourth;
    }
}
