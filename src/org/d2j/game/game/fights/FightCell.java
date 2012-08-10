package org.d2j.game.game.fights;

import com.sun.istack.internal.Nullable;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.Cell;
import org.d2j.game.game.pathfinding.Node;
import org.d2j.game.game.spells.cell_effects.CellEffect;
import org.d2j.game.game.spells.cell_effects.CellEffectList;
import org.d2j.utils.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 16/11/11
 * Time: 09:28
 * IDE : IntelliJ IDEA
 */
public class FightCell extends Cell {
    public static FightCell[] toFightCell(Fight fight, Cell[] cells, String places){
        FightCell[] fightCells = new FightCell[cells.length];

        for (short i = 0; i < cells.length; ++i){
            fightCells[i] = new FightCell(
                    fight,
                    cells[i].getId(),
                    cells[i].isLineOfSight(),
                    cells[i].getMovementType(),
                    cells[i].getGroundLevel(),
                    cells[i].getGroundSlope(),
                    cells[i].getPosition()
            );
        }

        String[] aPlaces = places.split("\\|");
        for (int teamId = 0; teamId <= 1; ++teamId){
            String bPlaces = aPlaces[teamId];
            FightTeamEnum team = FightTeamEnum.valueOf(teamId);

            for (int i = 0; i < bPlaces.length(); i += 2){
                short cellId = (short) ((StringUtils.HASH.indexOf(bPlaces.charAt(i)) << 6) +
                                        (StringUtils.HASH.indexOf(bPlaces.charAt(i + 1))));
                fightCells[cellId].setStartCell(team);
            }
        }

        return fightCells;
    }

    public static FightCell getFirstAvailableStartCell(FightTeamEnum team, FightCell[] cells){
        for (FightCell cell : cells) {
            if (cell.isAvailable() && cell.getStartCell() == team) {
                return cell;
            }
        }
        return null;
    }

    private final Fight fight;
    private FightTeamEnum startCell;

    private IFighter currentFighter;
    private CellEffectList effects = new CellEffectList();

    public FightCell(Fight fight, short id, boolean lineOfSight, MovementType movementType, int groundLevel, int groundSlope, Point position) {
        super(id, lineOfSight, movementType, groundLevel, groundSlope, position);

        this.fight = fight;
    }

    public Fight getFight() {
        return fight;
    }

    public IFighter getCurrentFighter() {
        return currentFighter;
    }

    public void setCurrentFighter(@Nullable IFighter currentFighter) {
        this.currentFighter = currentFighter;
    }

    public boolean isAvailable(){
        return isWalkable() && currentFighter == null;
    }

    public FightTeamEnum getStartCell() {
        return startCell;
    }

    public void setStartCell(FightTeamEnum startCell) {
        this.startCell = startCell;
    }

    public boolean isStartCell(){
        return startCell != null &&
              (startCell == FightTeamEnum.CHALLENGER ||
               startCell == FightTeamEnum.DEFENDER);
    }

    public void addEffect(CellEffect effect){
        effects.add(effect);
    }

    public void removeEffect(CellEffect effect){
        effects.remove(effect);
    }

    public CellEffectList getEffects() {
        return effects;
    }
}
