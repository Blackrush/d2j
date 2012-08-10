package org.d2j.game.game.fights.buffs;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.fights.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 21/12/11
 * Time: 12:58
 * IDE : IntelliJ IDEA
 */
public class FightBuffs {
    private static final Logger logger = LoggerFactory.getLogger(FightBuffs.class);

    private final Fight fight;
    private final IFighter fighter;
    private final Map<SpellEffectsEnum, Buff> buffs;

    public FightBuffs(Fight fight, IFighter fighter) {
        this.fight = fight;
        this.fighter = fighter;
        this.buffs = new HashMap<>();
    }

    public void onTurnStarted() throws FightException {
        AppendableFightHandlerAction action = new AppendableFightHandlerAction();

        for (SpellEffectsEnum effect : buffs.keySet()){
            Buff buff = buffs.get(effect); // get buff

            buff.onTurnStarted(action);

            if (buff.getRemainingTurns() <= 0){ // remove buff if it's finished
                buff.onRemoved(action);
                buffs.remove(effect);
            }
        }

        fight.foreach(action);
    }

    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
        for (SpellEffectsEnum effect : buffs.keySet()){
            Buff buff = buffs.get(effect); // get buff

            buff.onTurnStopped(action);

            if (buff.getRemainingTurns() <= 0){ // remove buff if it's finished
                buff.onRemoved(action);
                buffs.remove(effect);
            }
        }
    }

    public void add(final Buff buff) {
        try {
            AppendableFightHandlerAction action = new AppendableFightHandlerAction(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyNewBuff(fighter, buff);
                }
            });

            buff.onAdded(action);

            fight.foreach(action);
        } catch (FightException e) {
            logger.error(e.getMessage(), e.getCause());
        }

        buffs.put(buff.getEffect(), buff);
    }

    public void clear(AppendableFightHandlerAction action) throws FightException {
        for (Buff buff : buffs.values()) {
            buff.onRemoved(action);
        }

        buffs.clear();
    }

    public boolean tryRemove(SpellEffectsEnum effect) throws FightException {
        Buff buff = buffs.remove(effect);
        if (buff != null) {
            AppendableFightHandlerAction action = new AppendableFightHandlerAction();
            buff.onRemoved(action);

            fight.foreach(action);

            return true;
        }
        return false;
    }

    public boolean contains(SpellEffectsEnum effect) {
        return buffs.containsKey(effect);
    }
}
