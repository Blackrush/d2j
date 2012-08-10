package org.d2j.common.client.protocol;

import org.d2j.utils.tuples.Tuple2;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class WaypointGameMessageFormatter {
    public static String closePanelMessage(){
        return "WV";
    }

    public static String listMessage(int currentMapId, Collection<Tuple2<Integer, Long>> waypoints){
        StringBuilder sb = new StringBuilder().append("WC");

        sb.append(currentMapId);

        for (Tuple2<Integer, Long> waypoint : waypoints){
            sb.append('|');

            sb.append(waypoint.getFirst()).append(';'); // target map id
            sb.append(waypoint.getSecond());            // cost
        }

        return sb.toString();
    }
}
