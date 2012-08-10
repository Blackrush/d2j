package org.d2j.common;

import org.d2j.utils.Comparator;
import org.d2j.utils.Predicate;
import org.d2j.utils.Selector;

import java.util.*;

/**
 * User: Blackrush
 * Date: 22/11/11
 * Time: 19:28
 * IDE : IntelliJ IDEA
 */
public class CollectionUtils {
    public static <TIn, TOut> Collection<TOut> select(Collection<TIn> objs, Selector<TIn, TOut> selector){
        List<TOut> result = new ArrayList<>(objs.size());
        for (TIn obj : objs){
            TOut out = selector.select(obj);
            if (out != null){
                result.add(out);
            }
        }
        return result;
    }

    public static <T> List<T> concat(Collection<T> first, Collection<T> second){
        List<T> objs = new ArrayList<>();
        objs.addAll(first);
        objs.addAll(second);

        return objs;
    }

    public static <T> T last(List<T> list){
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public static <T> T last(Collection<T> collection){
        T last = null;
        for (T obj : collection){
            last = obj;
        }
        return last;
    }

    public static <T> T first(Collection<T> collection){
        return collection.isEmpty() ? null : collection.iterator().next();
    }

    public static <T> boolean compare(Collection<T> c1, Collection<T> c2, Comparator<T> comparator){
        if (c1.size() != c2.size()){
            return false;
        }

        Iterator<T> it1 = c1.iterator(),
                    it2 = c2.iterator();
        while (it1.hasNext() && it2.hasNext()){
            if (!comparator.compare(it1.next(), it2.next())){
                return false;
            }
        }
        return true;
    }

    public static <T> boolean any(Collection<T> c, Predicate<T> predicate){
        for (T o : c){
            if (predicate.test(o))
                return true;
        }
        return false;
    }

    public static <T, U> Collection<T> regroup(Collection<U> collection, Selector<U, T> selector){
        List<T> result = new ArrayList<>(collection.size());

        for (U u : collection){
            if (u == null) continue;
            T t = selector.select(u);
            if (t == null) continue;
            if (!result.contains(t)){
                result.add(t);
            }
        }

        return result;
    }

    public static <T> List<T> toList(T[] array){
        List<T> result = new ArrayList<>(array.length);
        for (T o : array){
            result.add(o);
        }
        return result;
    }

    @SafeVarargs
    public static <T> List<T> newList(T... objs){
        List<T> result = new ArrayList<>(objs.length);
        Collections.addAll(result, objs);
        return result;
    }
}
