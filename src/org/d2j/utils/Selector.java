package org.d2j.utils;

/**
 * User: Blackrush
 * Date: 22/11/11
 * Time: 19:28
 * IDE : IntelliJ IDEA
 */
public interface Selector<TIn, TOut> {
    TOut select(TIn in);
}
