package org.d2j.game.game.fights;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.client.protocol.type.BaseEndFighterType;
import org.d2j.common.client.protocol.type.BaseFighterType;
import org.d2j.common.client.protocol.type.BaseRolePlayActorType;
import org.d2j.game.game.fights.actions.IFightAction;
import org.d2j.game.game.fights.buffs.FightBuffs;
import org.d2j.game.game.spells.cell_effects.Glyph;
import org.d2j.game.game.spells.cell_effects.GlyphList;
import org.d2j.game.game.statistics.IStatistics;

import java.util.List;

/**
 * User: Blackrush
 * Date: 16/11/11
 * Time: 09:24
 * IDE : IntelliJ IDEA
 */
public interface IFighter {
    long getId();
    String getName();
    short getLevel();

    boolean isReady();
    Team getTeam();
    void setTeam(Team team);

    FightCell getCurrentCell();
    void setCurrentCell(FightCell currentCellId);

    OrientationEnum getCurrentOrientation();
    void setCurrentOrientation(OrientationEnum orientation);

    IStatistics getStatistics();
    boolean isAlive();
    boolean isLeader();

    IFightHandler getHandler();
    IFightAction getCurrentAction();
    void setCurrentAction(IFightAction action);

    FightLog getLogs();
    FightBuffs getBuffs();
    GlyphList getGlyphes();

    BaseRolePlayActorType toBaseRolePlayActorType();
    BaseFighterType toBaseFighterType();
    BaseEndFighterType toBaseEndFighterType();
}
