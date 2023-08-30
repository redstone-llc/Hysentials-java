package cc.woverflow.hysentials.util;

public class DuoVariable<E, T> {
    E first;
    T second;
    public DuoVariable(E first, T second) {
        this.first = first;
        this.second = second;
    }

    public E getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(E first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public static DuoVariable of(Object first, Object second) {
        return new DuoVariable(first, second);
    }

    @Override
    public String toString() {
        return "DuoVariable{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
