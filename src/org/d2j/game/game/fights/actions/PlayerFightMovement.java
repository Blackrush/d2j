package org.d2j.game.game.fights.actions;

import org.d2j.common.client.protocol.enums.EndActionTypeEnum;
import org.d2j.game.game.Cell;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.pathfinding.PathfindingException;

/**
 * User: Blackrush
 * Date: 24/11/11
 * Time: 18:03
 * IDE : IntelliJ IDEA
 */
public class PlayerFightMovement extends FightMovement {
    public PlayerFightMovement(Fight fight, IFighter fighter, FightCell end) throws PathfindingException {
        super(fight, fighter, end);
    }

    @Override
    protected void _end() throws FightException {
        super._end();

        fight.foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyEndAction(EndActionTypeEnum.MOVEMENT, fighter);
            }
        });
    }
}
