package org.d2j.game.model;

import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.client.protocol.type.*;
import org.d2j.game.game.CharacterExperience;
import org.d2j.game.game.RolePlayActor;
import org.d2j.game.game.WaypointList;
import org.d2j.game.game.guilds.GuildData;
import org.d2j.game.game.items.PersistentBag;
import org.d2j.game.game.items.StoreBag;
import org.d2j.game.game.statistics.CharacterStatistics;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.utils.database.entity.IEntity;

import java.util.*;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 18:33
 * IDE : IntelliJ IDEA
 */
public class Character extends Observable implements IEntity<Long>, RolePlayActor {
    public static Collection<BaseCharacterType> toBaseCharacterType(Collection<Character> characters){
        List<BaseCharacterType> t = new ArrayList<>(characters.size());
        for (Character c : characters)
            t.add(c.toBaseCharacterType());
        return t;
    }

    public static Collection<BasePartyMemberType> toBasePartyMemberType(Collection<Character> characters){
        List<BasePartyMemberType> t = new ArrayList<>(characters.size());
        for (Character c : characters){
            t.add(c.toBasePartyMemberType());
        }
        return t;
    }

    private long id;
    private GameAccount owner;
    private String name;
    private BreedTemplate breed;
    private boolean gender;
    private int color1, color2, color3;
    private short skin, size;
    private CharacterExperience experience;
    private CharacterStatistics statistics;
    private short energy;
    private short statsPoints, spellsPoints;
    private Map currentMap;
    private short currentCell;
    private OrientationEnum currentOrientation;
    private Map memorizedMap;
    private short memorizedCell;
    private HashMap<Integer, Spell> spells = new HashMap<>();
    private PersistentBag bag;
    private GuildData guildData;
    private WaypointList waypoints;
    private StoreBag store;

    public Character() {

    }

    public Character(long id, GameAccount owner, String name, BreedTemplate breed, boolean gender, int color1, int color2, int color3, short skin, short size, short energy, short statsPoints, short spellsPoints, Map currentMap, short currentCell, OrientationEnum currentOrientation, Map memorizedMap, short memorizedCell) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.skin = skin;
        this.size = size;
        this.energy = energy;
        this.statsPoints = statsPoints;
        this.spellsPoints = spellsPoints;
        this.currentMap = currentMap;
        this.currentCell = currentCell;
        this.currentOrientation = currentOrientation;
        this.memorizedMap = memorizedMap;
        this.memorizedCell = memorizedCell;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public long getPublicId() {
        return id;
    }

    public GameAccount getOwner() {
        return owner;
    }

    public void setOwner(GameAccount owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BreedTemplate getBreed() {
        return breed;
    }

    public void setBreed(BreedTemplate breed) {
        this.breed = breed;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor3() {
        return color3;
    }

    public void setColor3(int color3) {
        this.color3 = color3;
    }

    public short getSkin() {
        return skin;
    }

    public void setSkin(short skin) {
        this.skin = skin;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public CharacterExperience getExperience() {
        return experience;
    }

    public void setExperience(CharacterExperience experience) {
        this.experience = experience;
    }

    public CharacterStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(CharacterStatistics statistics) {
        this.statistics = statistics;
    }

    public short getEnergy() {
        return energy;
    }

    public void setEnergy(short energy) {
        this.energy = energy;
    }

    public short getStatsPoints() {
        return statsPoints;
    }

    public void setStatsPoints(short statsPoints) {
        this.statsPoints = statsPoints;
    }

    public void addStatsPoints(short statsPoints) {
        this.statsPoints += statsPoints;
    }

    public short getSpellsPoints() {
        return spellsPoints;
    }

    public void setSpellsPoints(short spellsPoints) {
        this.spellsPoints = spellsPoints;
    }

    public void addSpellsPoints(short spellsPoints) {
        this.spellsPoints += spellsPoints;
    }

    public int[] getAccessories() {
        Item[] accessories = bag.getAccessories();
        int[] results = new int[accessories.length];
        for (int i = 0; i < accessories.length; ++i){
            results[i] = accessories[i] != null ? accessories[i].getTemplate().getId() : -1;
        }
        return results;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map currentMap) {
        this.currentMap = currentMap;
    }

    @Override
    public short getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(short currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(OrientationEnum currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

    public Map getMemorizedMap() {
        return memorizedMap;
    }

    public void setMemorizedMap(Map memorizedMap) {
        this.memorizedMap = memorizedMap;
    }

    public short getMemorizedCell() {
        return memorizedCell;
    }

    public void setMemorizedCell(short memorizedCell) {
        this.memorizedCell = memorizedCell;
    }

    public HashMap<Integer, Spell> getSpells() {
        return spells;
    }

    public PersistentBag getBag() {
        return bag;
    }

    public void setBag(PersistentBag bag) {
        this.bag = bag;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public void setGuildData(GuildData guildData) {
        this.guildData = guildData;
    }

    public WaypointList getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(WaypointList waypoints) {
        this.waypoints = waypoints;
    }

    public StoreBag getStore() {
        return store;
    }

    public void setStore(StoreBag store) {
        this.store = store;
    }

    public String urlize(){
        return InfoGameMessageFormatter.urlize(name);
    }

    public BaseCharacterType toBaseCharacterType(){
        return new BaseCharacterType(
                id,
                name,
                experience.getLevel(),
                skin,
                color1, color2, color3,
                getAccessories(),
                store.isActive()
        );
    }

    @Override
    public BaseRolePlayActorType toBaseRolePlayActorType() {
        boolean hasGuild = guildData != null && guildData.getGuild().isValid();
        if (store.isActive()){
            return new RolePlaySellerType(
                    id,
                    currentCell,
                    currentOrientation,
                    name,
                    skin,
                    size,
                    color1, color2, color3,
                    getAccessories(),
                    hasGuild,
                    hasGuild ? guildData.getGuild().getName() : null,
                    hasGuild ? guildData.getGuild().getEmblem() : null
            );
        }
        else{
            return new RolePlayCharacterType(
                    id,
                    name,
                    breed.getId(),
                    skin,
                    size,
                    gender,
                    experience.getLevel(),
                    color1, color2, color3,
                    getAccessories(),
                    currentCell,
                    currentOrientation,
                    hasGuild,
                    hasGuild ? guildData.getGuild().getName() : null,
                    hasGuild ? guildData.getGuild().getEmblem() : null
            );
        }
    }

    public BasePartyMemberType toBasePartyMemberType(){
        return new BasePartyMemberType(
                id,
                name,
                skin,
                color1,
                color2,
                color3,
                getAccessories(),
                statistics.getLife(),
                experience.getLevel(),
                statistics.get(CharacteristicType.Initiative).getSafeTotal(),
                statistics.get(CharacteristicType.Prospection).getSafeTotal()
        );
    }

    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }

    public void beforeSave() {
    }

    public void onSaved() {
    }

    @Override
    public String toString() {
        return id + " " +
               name + " " +
               breed.toShortString() + " " +
               (gender ? "F" : "M") + " " +
               experience.getLevel() + " " +
               currentMap.getId();
    }
}
