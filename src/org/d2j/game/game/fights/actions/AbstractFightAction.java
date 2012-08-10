package org.d2j.game.game.fights.actions;

import org.d2j.game.game.fights.Fight;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.utils.Future;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 10/02/12
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFightAction implements IFightAction {
    protected final Fight fight;
    protected final IFighter fighter;

    private boolean success;
    private Future<IFightAction> endFuture;

    public AbstractFightAction(Fight fight, IFighter fighter) {
        this.fight = fight;
        this.fighter = fighter;
        this.endFuture = new Future<>((IFightAction)this);
    }

    protected abstract boolean _init();
    protected abstract void _begin() throws FightException;
    protected abstract void _end() throws FightException;

    protected void scheduleEnd(int milliseconds){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    end();
                } catch (FightException e) {
                    e.printStackTrace();
                }
            }
        }, milliseconds);
    }

    public final boolean init() throws FightException {
        success = _init();
        if (!success){
            fighter.getHandler().notifyNoAction();
            end();
        }
        return success;
    }

    @Override
    public final void begin() throws FightException {
        if (success) {
            fighter.setCurrentAction(this);
            _begin();
        }
    }

    @Override
    public final void end() throws FightException {
        if (success) {
            _end();
        }

        try {
            endFuture.notifyListeners();
        } catch (Exception e) {
            throw new FightException(e);
        } finally {
            fighter.setCurrentAction(null);
        }
    }

    @Override
    public Future<IFightAction> getEndFuture() {
        return endFuture;
    }

    @Override
    public final IFighter getFighter() {
        return fighter;
    }
}
