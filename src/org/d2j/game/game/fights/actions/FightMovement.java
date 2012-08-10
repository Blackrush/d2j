package org.d2j.game.game.fights.actions;

import org.d2j.common.client.protocol.enums.ActionTypeEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.Cell;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.pathfinding.FightNode;
import org.d2j.game.game.pathfinding.Pathfinding;
import org.d2j.game.game.pathfinding.PathfindingException;
import org.d2j.game.game.statistics.CharacteristicType;

import java.util.List;

import static org.d2j.common.CollectionUtils.last;

/**
 * User: Blackrush
 * Date: 24/11/11
 * Time: 17:57
 * IDE : IntelliJ IDEA
 */
public class FightMovement extends AbstractFightAction {
    public static void move(IFighter fighter, FightCell target){
        fighter.getCurrentCell().setCurrentFighter(null);
        fighter.setCurrentCell(target);
        target.setCurrentFighter(fighter);
    }

    protected final FightCell end;

    protected List<FightNode> nodes;
    protected String path;
    protected short steps;

    public FightMovement(Fight fight, IFighter fighter, FightCell end) throws PathfindingException {
        super(fight, fighter);
        this.end = end;
    }

    @Override
    protected boolean _init() {
        try {
            this.nodes = Pathfinding.bestFightPath(
                    this.fight,
                    this.fighter,
                    end
            );

            if (nodes.size() == 0){
                return false;
            }
            else {
                this.path = "a" + Cell.encode(fighter.getCurrentCell().getId()) + Cell.encode(nodes);
                this.steps = (short) this.nodes.size();

                return true;
            }
        } catch (PathfindingException e) {
            return false;
        }
    }

    protected void _begin() throws FightException {
        fighter.getStatistics().get(CharacteristicType.MovementPoints).addContext((short) -steps);

        if (!fighter.getBuffs().contains(SpellEffectsEnum.Invisible)){
            fight.foreach(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyFighterMovement(FightMovement.this);
                }
            });
        }
        else{
            fighter.getHandler().notifyFighterMovement(this);
        }

        scheduleEnd(Pathfinding.estimateTime(nodes, fight.getMap().getWidth()));
    }

    protected void _end() throws FightException {
        FightNode last = last(nodes);
        move(fighter, last.getCell());
        fighter.setCurrentOrientation(last.getOrientation());

        if (!fighter.getBuffs().contains(SpellEffectsEnum.Invisible)){
            fight.foreach(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyBasicAction(ActionTypeEnum.MP_CHANGEMENT, fighter, (int) fighter.getId(), -steps);
                }
            });
        }
        else{
            fighter.getHandler().notifyBasicAction(ActionTypeEnum.MP_CHANGEMENT, fighter, (int) fighter.getId(), -steps);
        }

        fight.applyCellEffects(fighter, fighter.getCurrentCell());
    }

    public String getPath(){
        return path;
    }
}
