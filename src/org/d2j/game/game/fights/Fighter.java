package org.d2j.game.game.fights;

import org.d2j.common.NumUtils;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.client.protocol.type.BaseEndFighterType;
import org.d2j.common.client.protocol.type.BaseFighterType;
import org.d2j.common.client.protocol.type.BaseRolePlayActorType;
import org.d2j.common.client.protocol.type.CharacterFighterType;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.actions.IFightAction;
import org.d2j.game.game.fights.buffs.FightBuffs;
import org.d2j.game.game.spells.EffectFactory;
import org.d2j.game.game.spells.cell_effects.Glyph;
import org.d2j.game.game.spells.cell_effects.GlyphList;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.spells.filters.TargetFilter;
import org.d2j.game.game.statistics.CharacterStatistics;
import org.d2j.game.model.Spell;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.AbstractCacheSystem;
import org.d2j.utils.Reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * User: Blackrush
 * Date: 16/11/11
 * Time: 09:08
 * IDE : IntelliJ IDEA
 */
public class Fighter implements IFighter {
    public static Reference<Effect> FISTS_EFFECT = new AbstractCacheSystem<Effect>() {
        @Override
        protected void refresh() {
            obj = EffectFactory.getInstance().get(SpellEffectsEnum.DamageNeutral);
            obj.setDice(new Dice(1, 5, 0));
            obj.setFilter(TargetFilter.newDefault());
        }
    };

    public static Collection<BaseFighterType> toBaseFighterType(Collection<IFighter> fighters){
        List<BaseFighterType> types = new ArrayList<>(fighters.size());
        for (IFighter fighter : fighters){
            if (fighter != null)
                types.add(fighter.toBaseFighterType());
        }
        return types;
    }

    public static Collection<BaseEndFighterType> toBaseEndFighterType(Collection<IFighter> fighters){
        List<BaseEndFighterType> types = new ArrayList<>(fighters.size());
        for (IFighter fighter : fighters){
            if (fighter != null)
                types.add(fighter.toBaseEndFighterType());
        }
        return types;
    }

    private Team team;
    private final GameClient client;
    private final IFightHandler handler;
    private IFightAction currentAction;

    private final FightLog logs;
    private final FightBuffs buffs;
    private final GlyphList glyphes = new GlyphList();

    private FightCell currentCell;
    private OrientationEnum currentOrientation;
    private boolean ready;
    private CharacterStatistics statistics;

    public Fighter(Fight fight, GameClient client, IFightHandler handler) {
        this.client = client;
        this.currentOrientation = OrientationEnum.SOUTH_EAST;
        this.handler = handler;
        this.logs = new FightLog();
        this.buffs = new FightBuffs(fight, this);

        this.statistics = client.getCharacter().getStatistics().copy();
    }

    public HashMap<Integer, Spell> getSpells(){
        return client.getCharacter().getSpells();
    }

    @Override
    public long getId() {
        return client.getCharacter().getId();
    }

    @Override
    public String getName() {
        return client.getCharacter().getName();
    }

    @Override
    public short getLevel() {
        return client.getCharacter().getExperience().getLevel();
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public FightCell getCurrentCell() {
        return currentCell;
    }

    @Override
    public void setCurrentCell(FightCell currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    @Override
    public void setCurrentOrientation(OrientationEnum orientation) {
        this.currentOrientation = orientation;
    }

    @Override
    public CharacterStatistics getStatistics() {
        return statistics;
    }

    @Override
    public boolean isAlive() {
        return statistics.getLife() > 0;
    }

    @Override
    public boolean isLeader() {
        return team.getLeader() == this;
    }

    @Override
    public IFightHandler getHandler() {
        return handler;
    }

    @Override
    public IFightAction getCurrentAction() {
        return currentAction;
    }

    @Override
    public void setCurrentAction(IFightAction currentAction) {
        this.currentAction = currentAction;
    }

    @Override
    public FightLog getLogs() {
        return logs;
    }

    @Override
    public FightBuffs getBuffs() {
        return buffs;
    }

    @Override
    public GlyphList getGlyphes() {
        return glyphes;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public BaseRolePlayActorType toBaseRolePlayActorType() {
        return client.getCharacter().toBaseRolePlayActorType();
    }

    @Override
    public BaseFighterType toBaseFighterType() {
        return new CharacterFighterType(
                client.getCharacter().getId(),
                client.getCharacter().getName(),
                client.getCharacter().getBreed().getId(),
                client.getCharacter().getSkin(),
                client.getCharacter().getSize(),
                client.getCharacter().getExperience().getLevel(),
                currentCell.getId(),
                currentOrientation,
                !isAlive(),
                statistics,
                client.getCharacter().getGender(),
                NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, false, //todo alignment
                client.getCharacter().getColor1(),
                client.getCharacter().getColor2(),
                client.getCharacter().getColor3(),
                client.getCharacter().getAccessories(),
                client.getCharacter().getStatistics(),
                team.getTeamType()
        );
    }

    @Override
    public BaseEndFighterType toBaseEndFighterType() {
        return new BaseEndFighterType(
                client.getCharacter().getId(),
                client.getCharacter().getName(),
                client.getCharacter().getExperience().getLevel(),
                statistics.getLife(),
                statistics.getLife() > 0,
                client.getCharacter().getExperience().min(),
                client.getCharacter().getExperience().max(),
                client.getCharacter().getExperience().getExperience(),
                0, 0, 0, 0
        );
    }
}
