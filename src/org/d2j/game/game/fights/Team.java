package org.d2j.game.game.fights;

import org.d2j.common.client.protocol.enums.FightAttributeType;
import org.d2j.common.client.protocol.enums.FightStateEnum;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 16/11/11
 * Time: 09:08
 * IDE : IntelliJ IDEA
 */
public class Team implements Iterable<IFighter> {
    private final Fight fight;
    private final FightTeamEnum teamType;
    private final String places;
    private IFighter leader;
    private Map<FightAttributeType, Boolean> attributes = FightAttributeType.emptyMap(false);

    private HashMap<Long, IFighter> fighters = new HashMap<>();
    private int availablePlaces;

    public Team(Fight fight, FightTeamEnum teamType, String places) {
        this.fight = fight;
        this.teamType = teamType;
        this.places = places;

        for (int i = 0; i < fight.getCells().length; ++i){
            if (fight.getCells()[i].getStartCell() == this.teamType) {
                this.availablePlaces++;
            }
        }
    }

    public FightTeamEnum getTeamType() {
        return teamType;
    }

    public int getNbFighters() {
        return fighters.size();
    }

    public String getPlaces() {
        return places;
    }

    public void addFighter(IFighter fighter) throws IndexOutOfBoundsException, FightException {
        if (fight.getState() != FightStateEnum.INIT && fight.getState() != FightStateEnum.PLACE){
            throw new FightException("You can't join a fight that started.");
        }
        if (availablePlaces <= 0){
            throw new IndexOutOfBoundsException("There is no more available places in this team.");
        }

        --availablePlaces;
        fighters.put(fighter.getId(), fighter);

        if (leader == null){
            leader = fighter;
        }

        fighter.setTeam(this);
        fighter.setCurrentCell(FightCell.getFirstAvailableStartCell(teamType, fight.getCells()));
        fighter.getCurrentCell().setCurrentFighter(fighter);

        if (fight.getState() == FightStateEnum.PLACE){
            fighter.getHandler().notifyFightJoin(
                    fight.getTeam(FightTeamEnum.CHALLENGER),
                    fight.getTeam(FightTeamEnum.DEFENDER)
            );

            fight.notifyAddFighter(fighter);
        }
    }

    public IFighter removeFighter(long id) throws IndexOutOfBoundsException {
        IFighter fighter = fighters.remove(id);

        if (fighter != null){
            ++availablePlaces;
        }

        return fighter;
    }

    public IFighter getFighter(long id) throws IndexOutOfBoundsException {
        return fighters.get(id);
    }

    public IFighter getLeader() {
        return leader;
    }

    public Collection<IFighter> getFighters(){
        return fighters.values();
    }

    public boolean isReady(){
        if (fighters.isEmpty()){
            return false;
        }

        for (IFighter fighter : fighters.values()){
            if (!fighter.isReady()){
                return false;
            }
        }
        return true;
    }

    public boolean isAlive(){
        if (fighters.isEmpty()){
            return false;
        }

        for (IFighter fighter : fighters.values()){
            if (!fighter.isAlive()){
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(){
        return fighters.isEmpty();
    }

    public boolean isAvailable(){
        return availablePlaces > 0;
    }

    public boolean setAttribute(final FightAttributeType attribute) throws FightException {
        final boolean active = !attributes.get(attribute);
        attributes.remove(attribute);
        attributes.put(attribute, active);

        fight.foreachTeam(this.teamType, new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyUpdateAttribute(attribute, active);
            }
        });

        return active;
    }

    public boolean getAttribute(FightAttributeType attribute){
        return attributes.get(attribute);
    }

    @Override
    public Iterator<IFighter> iterator() {
        return fighters.values().iterator();
    }
}
