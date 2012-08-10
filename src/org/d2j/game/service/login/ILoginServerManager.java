package org.d2j.game.service.login;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.d2j.game.model.GameAccount;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 16:43
 * IDE : IntelliJ IDEA
 */
public interface ILoginServerManager extends IoHandler {
    void start();
    void stop();

    boolean isSynchronized();

    WriteFuture refreshWorld();
    WriteFuture setAccountDeconnected(Integer accountId);
    WriteFuture refreshCharactersList(Integer accountId, Integer characters);
    WriteFuture refreshGameAccount(GameAccount account);
}
