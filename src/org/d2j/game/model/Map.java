package org.d2j.game.model;

import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.common.client.protocol.enums.FightAttributeType;
import org.d2j.game.game.Cell;
import org.d2j.game.game.RolePlayActor;
import org.d2j.game.game.actions.RolePlayMovement;
import org.d2j.game.game.events.*;
import org.d2j.game.game.fights.Fight;
import org.d2j.game.game.fights.IFighter;
import org.d2j.utils.database.entity.IBaseEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 16:53
 * IDE : IntelliJ IDEA
 */
public class Map extends Observable implements IBaseEntity<Integer> {
    private Integer id;
    private byte abscissa, ordinate;
    private byte width, height;
    private short subarea;
    private String key;
    private String date;
    private boolean subscriberArea;
    private String places;
    private byte maxStores;

    private final Cell[] cells;
    private final HashMap<Short, MapTrigger> triggers = new HashMap<>();
    private Waypoint waypoint;
    private MarketPlace marketPlace;

    private long nextPublicId, nextFightId;
    private byte nbSellers;

    private java.util.Map<Long, Fight> fights = new ConcurrentHashMap<>();
    private java.util.Map<Long, RolePlayActor> actors = new ConcurrentHashMap<>();

    public Map(Integer id, byte abscissa, byte ordinate, byte width, byte height, short subarea, String key, String date, boolean subscriberArea, String places, byte maxStores, Cell[] cells) {
        this.id = id;
        this.abscissa = abscissa;
        this.ordinate = ordinate;
        this.width = width;
        this.height = height;
        this.subarea = subarea;
        this.key = key;
        this.date = date;
        this.subscriberArea = subscriberArea;
        this.places = places;
        this.maxStores = maxStores;
        this.cells = cells;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public byte getAbscissa() {
        return abscissa;
    }

    public byte getOrdinate() {
        return ordinate;
    }

    public byte getWidth() {
        return width;
    }

    public byte getHeight() {
        return height;
    }

    public short getSubarea() {
        return subarea;
    }

    public String getKey() {
        return key;
    }

    public String getDate() {
        return date;
    }

    public boolean isSubscriberArea() {
        return subscriberArea;
    }

    public String getPlaces() {
        return places;
    }

    public byte getMaxStores() {
        return maxStores;
    }

    public Cell[] getCells() {
        return cells;
    }

    public HashMap<Short, MapTrigger> getTriggers() {
        return triggers;
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
    }

    public MarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    public long getNextPublicId() {
        return --nextPublicId;
    }

    public long getNextFightId() {
        return ++nextFightId;
    }

    public Collection<RolePlayActor> getActors(){
        return actors.values();
    }

    public boolean containsActor(long actorPublicId){
        return actors.containsKey(actorPublicId);
    }

    public boolean containsActor(RolePlayActor actor){
        return actors.containsKey(actor.getPublicId());
    }

    public RolePlayActor getActor(long actorPublicId){
        return actors.get(actorPublicId);
    }

    public <T> T getActor(long actorPublicId, Class<T> clazz){
        RolePlayActor actor = actors.get(actorPublicId);
        if (actor != null && clazz.isInstance(actor)){
            return clazz.cast(actor);
        }
        return null;
    }

    public void addActor(RolePlayActor actor){
        if (!actors.containsKey(actor.getPublicId())){
            actors.put(actor.getPublicId(), actor);

            setChanged();
            notifyObservers(new MapUpdateActorEvent(actor, MapUpdateActorEvent.MapUpdateType.ADD));
        }
    }

    public void addSeller(Character seller){
        MapUpdateActorEvent.MapUpdateType updateType = MapUpdateActorEvent.MapUpdateType.UPDATE;
        if (!actors.containsKey(seller.getPublicId())){
            actors.put(seller.getPublicId(), seller);
            updateType = MapUpdateActorEvent.MapUpdateType.ADD;
        }

        ++nbSellers;

        setChanged();
        notifyObservers(new MapUpdateActorEvent(seller, updateType));
    }

    public void removeActor(RolePlayActor actor){
        if (actors.containsKey(actor.getPublicId())){
            actors.remove(actor.getPublicId());

            setChanged();
            notifyObservers(new MapUpdateActorEvent(actor, MapUpdateActorEvent.MapUpdateType.REMOVE));
        }
    }

    public void removeSeller(Character seller){
        if (actors.containsKey(seller.getPublicId())){
            actors.remove(seller.getPublicId());
            --nbSellers;

            setChanged();
            notifyObservers(new MapUpdateActorEvent(seller, MapUpdateActorEvent.MapUpdateType.REMOVE));
        }
    }

    public void move(RolePlayMovement movement){
        if (actors.containsKey(movement.getActor().getId())){
            setChanged();
            notifyObservers(movement);
        }
    }

    public void updateActor(RolePlayActor actor){
        if (actors.containsKey(actor.getPublicId())){
            setChanged();
            notifyObservers(new MapUpdateActorEvent(actor, MapUpdateActorEvent.MapUpdateType.UPDATE));
        }
    }

    public void updateActorAccessories(Character actor) {
        if (actors.containsKey(actor.getPublicId())){
            setChanged();
            notifyObservers(new MapUpdateActorEvent(actor, MapUpdateActorEvent.MapUpdateType.UPDATE_ACCESSORIES));
        }
    }

    public void speak(Character actor, ChannelEnum channel, String message){
        if (actors.containsKey(actor.getId())){
            setChanged();
            notifyObservers(new MessageEvent(actor.getId(), actor.getName(), channel, message));
        }
    }

    public void announce(String message){
        setChanged();
        notifyObservers(new SystemMessageEvent(message));
    }

    public Collection<Fight> getFights(){
        return fights.values();
    }

    public int getNbFights(){
        return fights.size();
    }

    public boolean canFight(){
        return places != null && places.length() > 0 && !places.equals("|");
    }

    public Fight getFight(long fightId){
        return fights.get(fightId);
    }

    public void addFight(Fight fight){
        fights.put(fight.getId(), fight);

        setChanged();
        notifyObservers(new FightsUpdateEvent(this));
    }

    public void removeFight(long fightId){
        fights.remove(fightId);

        setChanged();
        notifyObservers(new FightsUpdateEvent(this));
    }

    public void removeFight(Fight fight){
        fights.remove(fight.getId());

        setChanged();
        notifyObservers(new FightsUpdateEvent(this));
    }

    public void addFightFlag(Fight fight){
        setChanged();
        notifyObservers(new FlagUpdateEvent(fight, FlagUpdateEvent.UpdateType.ADD));
    }

    public void removeFightFlag(Fight fight){
        setChanged();
        notifyObservers(new FlagUpdateEvent(fight, FlagUpdateEvent.UpdateType.REMOVE));
    }

    public void updateFightFlag(IFighter leader, FightAttributeType attribute, boolean active){
        setChanged();
        notifyObservers(new FlagAttributeUpdateEvent(leader, attribute, active));
    }

    public boolean availableStorePlaces() {
        return nbSellers < maxStores;
    }
}
