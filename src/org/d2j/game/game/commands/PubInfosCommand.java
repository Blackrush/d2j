package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.game.GameService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 05/02/12
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class PubInfosCommand extends AbstractCommand {
    private final GameService gameService;

    public PubInfosCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String helpMessage() {
        return "Show pub informations.";
    }

    @Override
    public String[] usageParameters() {
        return new String[0];
    }

    @Override
    public String name() {
        return "pub_infos";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        StringBuilder sb = new StringBuilder();

        List<String> messages = gameService.getPubSystem().getMessages();

        sb.append("There are ").append(messages.size()).append(" messages :\n");
        for (int i = 0; i < messages.size(); ++i){
            sb.append(' ').append(i).append(". ").append(messages.get(i)).append('\n');
        }

        out.info(sb.toString());
    }
}
