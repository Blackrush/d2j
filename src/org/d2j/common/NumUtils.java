package org.d2j.common;

import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.Collection;
import java.util.Comparator;

/**
 * User: Blackrush
 * Date: 13/11/11
 * Time: 13:08
 * IDE : IntelliJ IDEA
 */
public class NumUtils {
    public static final byte BYTE_ZERO = (byte)0;
    public static final short SHORT_ZERO = (short)0;

    public static final short ONE_SHORT = (short)1;

    public static <T> T max(Collection<? extends T> collection, Comparator<T> comparator){
        T match = null;

        for (T obj : collection){
            if (match == null){
                match = obj;
            }
            else{
                if (comparator.compare(obj, match) > 0){
                    match = obj;
                }
            }
        }

        return match;
    }

    public static long secondsBetween(Instant i1, Instant i2){
        return new Duration(i1, i2).getStandardSeconds();
    }

    public static long hoursBetween(Instant i1, Instant i2){
        return new Duration(i1, i2).getStandardHours();
    }
}
