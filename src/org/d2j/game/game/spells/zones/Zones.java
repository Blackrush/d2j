package org.d2j.game.game.spells.zones;

import org.d2j.common.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 20:24
 */
public class Zones {
    public static Zone parseZone(String string){
        int size = StringUtils.HASH.indexOf(string.charAt(1)) - 1;
        if (size <= 0){
            return new SingleCell();
        }

        switch (string.charAt(0)){
            case 'X':
                return new Cross(size);

            case 'L':
                return new Line(size);

            case 'C':
                return new Circle(size);

            default:
                return new SingleCell();
        }
    }
}
