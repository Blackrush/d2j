package org.d2j.game.game.fights.actions;

import org.d2j.game.game.fights.*;
import org.d2j.utils.Future;
import org.joda.time.Duration;
import org.joda.time.Instant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Blackrush
 * Date: 20/11/11
 * Time: 21:42
 * IDE : IntelliJ IDEA
 */
public class Turn {
    private final Fight fight;
    private final IFighter fighter;
    private final Timer timer;

    private Instant end;
    private boolean abandoned;

    public Turn(IFighter fighter, Fight fight) {
        this.fighter = fighter;
        this.fight = fight;
        this.timer = new Timer((int) fight.getConfiguration().getTurnDuration().getMillis(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    end();
                } catch (FightException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void begin() throws FightException {
        end = Instant.now().plus(fight.getConfiguration().getTurnDuration());

        timer.start();

        onStarted();

        fight.foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyTurnStart(Turn.this);
            }
        });
    }

    private void _end() throws FightException {
        timer.stop();

        onEnded();

        fight.foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyFightersInformations(fight.getFighters());
                obj.notifyTurnStop(Turn.this);
            }
        });
        fight.nextTurn();
    }

    public void end() throws FightException {
        if (fighter.getCurrentAction() == null){
            _end();
        }
        else{
            fighter.getCurrentAction().getEndFuture().addListener(new Future.Listener<IFightAction>() {
                @Override
                public void listen(IFightAction obj) throws Exception {
                    _end();
                }
            });
        }
    }

    private void onStarted() throws FightException {
        fighter.getLogs().clear();
        fighter.getBuffs().onTurnStarted();
    }

    private void onEnded() throws FightException {
        AppendableFightHandlerAction action = new AppendableFightHandlerAction();
        fighter.getGlyphes().decrementRemainingTurns(action);
        fighter.getBuffs().onTurnStopped(action);
        fight.foreach(action);

        fighter.getStatistics().resetContext();
        fighter.getHandler().notifyRefreshStatistics();
    }

    public IFighter getFighter() {
        return fighter;
    }

    public Duration remainingTime(){
        return new Duration(Instant.now(), end);
    }

    public boolean hasAbandoned() {
        return abandoned;
    }

    public void setAbandoned(boolean abandoned) {
        this.abandoned = abandoned;
    }
}
