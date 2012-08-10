package org.d2j.game.game.commands;

import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/01/12
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public class CommandFactory implements ICommandFactory {
    private final IWorld world;
    private List<ICommand> commands;

    public CommandFactory(IWorld world){
        this.world = world;
        this.commands = new ArrayList<>();
    }

    @Override
    public int init(){
        commands.add(new AnnounceCommand(world));
        commands.add(new NameAnnounceCommand(world));
        commands.add(new AllCommand(world));
        commands.add(new MapAnnounceCommand());
        commands.add(new AlertCommand(world));
        commands.add(new SaveCommand(world));
        commands.add(new StateCommand(world));
        commands.add(new KickCommand(world, world.getRepositoryManager().getCharacters()));
        commands.add(new BanCommand(world));
        commands.add(new MuteCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new DemuteCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new RightCommand(world.getRepositoryManager().getAccounts()));
        commands.add(new EditPubMessageCommand(world.getGameService()));
        commands.add(new PubInfosCommand(world.getGameService()));
        commands.add(new ForcePubCommand(world.getGameService().getPubSystem()));
        commands.add(new TeleportCommand(world.getRepositoryManager().getMaps(), world.getRepositoryManager().getCharacters()));
        commands.add(new NameGoCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new GoNameCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new EditSkinCommand());
        commands.add(new EditSizeCommand());
        commands.add(new AddNpcCommand(world.getRepositoryManager()));
        commands.add(new RemoveNpcCommand(world.getRepositoryManager().getNpcs()));
        commands.add(new AddNpcSellCommand(world.getRepositoryManager()));
        commands.add(new RemoveNpcSellCommand(world.getRepositoryManager()));
        commands.add(new MapInformationsCommand());
        commands.add(new CreateGuildCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new GiveItemCommand(world.getRepositoryManager().getCharacters(), world.getRepositoryManager().getItemTemplates()));
        commands.add(new GiveKamasCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new EditLifeCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new AddStatsPointsCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new AddSpellsPointsCommand(world.getRepositoryManager().getCharacters()));
        commands.add(new LearnSpellCommand(world.getRepositoryManager().getCharacters(), world.getRepositoryManager().getSpellTemplates(), world.getRepositoryManager().getSpells()));
        commands.add(new AddWaypointsCommand(world.getRepositoryManager().getCharacters(), world.getRepositoryManager().getWaypoints()));

        commands.add(new WhoCommand(world));
        commands.add(new StaffCommand(world));
        commands.add(new InfosCommand(world));
        commands.add(new ReportCommand(world));

        if (world.getConfiguration().getCommandEnabled()){
            commands.add(new StartCommand(world.getConfiguration(), world.getRepositoryManager().getMaps()));
        }

        commands.add(new HelpCommand(commands));

        return commands.size();
    }

    @Override
    public void parse(GameClient client, String command, boolean console){
        DofusLogger out = console ? client.getConsoleOut() : client.getTchatOut();
        String[] args = command.split(" ");
        if (args.length <= 0){
            out.error("Invalid command.");
        }
        else{
            String name = args[0];
            String[] args_;
            if (args.length <= 1){
                args_ = new String[0];
            }
            else {
                args_ = Arrays.copyOfRange(args, 1, args.length);
            }

            for (ICommand command_ : commands){
                if (name.equals(command_.name()) &&
                    client.getAccount().getRights().superiorOrEquals(command_.level()))
                {
                    try {
                        command_.parse(client, out, args_);
                    } catch (NumberFormatException e) {
                        out.error("A number is expected.");
                    } catch (Exception e) {
                        out.error(e.getMessage());
                    }
                    return;
                }
            }

            out.error("Unknown command \"{0}\".", name);
        }
    }
}
