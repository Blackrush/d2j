package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.AbstractCacheSystem;
import org.d2j.utils.DelegateCacheSystem;
import org.d2j.utils.Maker;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/01/12
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class HelpCommand extends AbstractCommand {
    public static final String PATTERN = "<b>{0}</b> : {1}\n";

    private Collection<ICommand> commands;
    private HashMap<Permissions, AbstractCacheSystem<String>> caches;

    public HelpCommand(Collection<ICommand> commands) {
        this.commands = commands;
        this.caches = new HashMap<>();
        for (Permissions permissions : Permissions.values()){
            this.caches.put(permissions, new DelegateCacheSystem<>(createMaker(permissions)));
        }
    }

    private Maker<String> createMaker(final Permissions permissions){
        return new Maker<String>() {
            @Override
            public String make() {
                StringBuilder sb = new StringBuilder();
                sb.append("All of these commands are available for the ").append(permissions).append(" right :\n");
                for (ICommand command : commands){
                    if (permissions.superiorOrEquals(command.level())){
                        sb.append(StringUtils.format(PATTERN, command.name(), command.help()));
                    }
                }
                return sb.toString();
            }
        };
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String helpMessage() {
        return "Show this dialog.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"COMMAND:facultative"};
    }

    @Override
    public Permissions level() {
        return Permissions.MEMBER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length > 0){
            for (ICommand command : commands){
                if (command.name().equalsIgnoreCase(args[0])){
                    out.info(PATTERN, command.name(), command.help());
                    return;
                }
            }
            out.error("Unknown command \"{0}\".", args[0]);
        }
        else{
            out.info(caches.get(client.getAccount().getRights()).get());
        }
    }
}
