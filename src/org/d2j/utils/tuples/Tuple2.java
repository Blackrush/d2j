package org.d2j.utils.tuples;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public class Tuple2<A, B> {
    private A first;
    private B second;

    public Tuple2() {
    }

    public Tuple2(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}
