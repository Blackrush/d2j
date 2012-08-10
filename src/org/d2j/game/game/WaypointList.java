package org.d2j.game.game;

import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.repository.IEntityRepository;
import org.d2j.utils.tuples.Tuple2;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 22/02/12
 * Time: 19:33
 */
public class WaypointList implements Iterable<WaypointRecord> {
    private final Character owner;
    private final IEntityRepository<WaypointRecord, Long> repository;

    private HashMap<Short, WaypointRecord> waypoints = new HashMap<>();

    public WaypointList(Character owner, IEntityRepository<WaypointRecord, Long> repository) {
        this.owner = owner;
        this.repository = repository;
    }

    public void add(Waypoint waypoint){
        WaypointRecord record = new WaypointRecord(owner, waypoint);
        repository.create(record);

        waypoints.put(waypoint.getId(), record);
    }

    public void addEntity(WaypointRecord record){
        waypoints.put(record.getWaypoint().getId(), record);
    }

    public boolean contains(Short waypointId){
        return waypoints.containsKey(waypointId);
    }

    public boolean contains(Waypoint waypoint){
        return waypoints.containsKey(waypoint.getId());
    }

    public Collection<Tuple2<Integer, Long>> toTuples(){
        List<Tuple2<Integer, Long>> result = new ArrayList<>(waypoints.size());
        for (WaypointRecord record : waypoints.values()){
            result.add(new Tuple2<>(
                    record.getWaypoint().getMap().getId(),
                    record.getWaypoint().getCost(owner.getCurrentMap())
            ));
        }
        return result;
    }

    @Override
    public Iterator<WaypointRecord> iterator() {
        return waypoints.values().iterator();
    }
}
