package org.d2j.game.game.live_actions;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.IRepositoryManager;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.live_actions.actions.*;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLiveActionFactory implements ILiveActionFactory {
    private static final DefaultLiveActionFactory self = new DefaultLiveActionFactory();

    public static ILiveActionFactory getInstance() {
        return self;
    }

    private IGameConfiguration configuration;
    private IRepositoryManager repositoryManager;

    @Override
    public void load(IGameConfiguration configuration, IRepositoryManager repositoryManager){
        this.configuration = configuration;
        this.repositoryManager = repositoryManager;
    }

    @Override
    public ILiveAction make(LiveActionType type, String[] args) {
        switch (type) {
            case CONTINUE_QUESTION:
                return new ContinueQuestionAction(repositoryManager.getNpcQuestions().findById(Integer.parseInt(args[0])));

            case RESTAT:
                return new RestatAction(configuration);

            case TELEPORT:
                return new TeleportAction(
                        repositoryManager.getMaps().findById(Integer.parseInt(args[0])),
                        Short.parseShort(args[1]),
                        OrientationEnum.valueOf(Integer.parseInt(args[2]))
                );

            case ADD_ITEM:
                return new AddItemAction(
                        repositoryManager.getItemTemplates().findById(Integer.parseInt(args[0])),
                        Integer.parseInt(args[1])
                );

            case REMOVE_ITEM:
                return new RemoveItemAction(
                        repositoryManager.getItemTemplates().findById(Integer.parseInt(args[0])),
                        Integer.parseInt(args[1])
                );

            case CREATE_GUILD:
                return new CreateGuildAction();

            case SHOW_MESSAGE:
                return new ShowMessageAction(args[0]);

            case ADD_LIFE:
                return new AddLifeAction(Dice.parseDice(args[0]), args.length > 1 ? args[1] : null);

            case TELEPORT_BACK:
                return new TeleportBackAction(args.length > 0 ? args[0] : null);

            case ADD_KAMAS:
                return new AddKamasAction(Long.parseLong(args[0]));

            default:
                return null;
        }
    }
}
