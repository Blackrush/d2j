package org.d2j.game.game.fights;

import org.d2j.common.client.protocol.enums.FightStateEnum;
import org.d2j.common.client.protocol.enums.FightTypeEnum;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.fights.actions.Turn;
import org.d2j.game.model.Map;

/**
 * User: Blackrush
 * Date: 21/12/11
 * Time: 15:29
 * IDE : IntelliJ IDEA
 */
public class ChallengeFight extends Fight {
    public ChallengeFight(IGameConfiguration configuration, Map map) {
        super(configuration, map);
    }

    @Override
    public int remainingTime() {
        return -1;
    }

    @Override
    public FightTypeEnum getFightType() {
        return FightTypeEnum.CHALLENGE;
    }

    @Override
    protected void onInited() {
        map.addFightFlag(this);
    }

    @Override
    public boolean startIfYouCan() throws FightException {
        if (getState() != FightStateEnum.PLACE){
            throw new FightException("Fight's state doesn't allow this request.");
        }
        else if (challengers.isReady() && defenders.isReady()){
            start();

            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onStarted() {
        map.removeFightFlag(this);
    }

    @Override
    public boolean stopIfYouCan() throws FightException {
        if (fighters.size() > 1){
            return false;
        }
        else{
            stop();
            return true;
        }
    }

    @Override
    protected void stopFight(FightStateEnum oldState, final FightTeamEnum winners) throws FightException {
        if (oldState == FightStateEnum.PLACE){
            foreach(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyQuit();
                }
            });

            map.removeFightFlag(this);
        }
        else if (oldState == FightStateEnum.ACTIVE){
            foreach(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyFightEnd(
                            getTeam(winners),
                            getTeam(FightUtils.oppositeTeam(winners))
                    );
                }
            });
        }
        else{ // must not happened !!!
            throw new FightException("Invalid request: fight's state doesn't allow this request.");
        }
    }

    @Override
    public void onQuit(IFighter fighter) throws FightException {
        switch (getState()){
            case PLACE:
                if (fighter.isLeader() || fighter.getTeam().getNbFighters() <= 1){
                    stop();
                }
                else{
                    fighter.getCurrentCell().setCurrentFighter(null);
                    fighter.getTeam().removeFighter(fighter.getId());
                    fighter.getHandler().notifyQuit();

                    notifyFighterQuit(fighter);
                }
                break;

            case ACTIVE:
                if (fighter.getTeam().getNbFighters() <= 1){
                    stop();
                }
                else{
                    fighters.remove(fighter);

                    fighter.getCurrentCell().setCurrentFighter(null);
                    fighter.getTeam().removeFighter(fighter.getId());
                    fighter.getHandler().notifyQuit();

                    notifyFighterQuit(fighter);

                    for (int i = 0; i < turns.size(); ++i){
                        Turn turn = turns.get(i);
                        if (turn.getFighter() == fighter){
                            turn.setAbandoned(true);

                            if (i == 0){ // if current turn
                                turn.end();
                            }

                            break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClosed(IFighter fighter) throws FightException {
        onQuit(fighter);
    }
}
