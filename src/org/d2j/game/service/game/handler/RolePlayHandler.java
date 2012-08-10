package org.d2j.game.service.game.handler;

import org.d2j.common.GuildEmblem;
import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.NumUtils;
import org.d2j.common.client.protocol.*;
import org.d2j.common.client.protocol.enums.*;
import org.d2j.game.game.RolePlayActors;
import org.d2j.game.game.actions.*;
import org.d2j.game.game.channels.ChannelLogs;
import org.d2j.game.game.events.*;
import org.d2j.game.game.fights.Fight;
import org.d2j.game.game.fights.Fighter;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.friends.FullListException;
import org.d2j.game.game.items.Bag;
import org.d2j.game.game.items.ItemEffect;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.game.trades.*;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.game.GameClientHandler;
import org.d2j.game.service.game.GameService;
import org.d2j.utils.Future;

import java.util.Collection;
import java.util.Date;
import java.util.Observable;

/**
 * User: Blackrush
 * Date: 05/11/11
 * Time: 12:09
 * IDE:  IntelliJ IDEA
 */
public class RolePlayHandler extends GameClientHandler {
    private final ChannelLogs channelLogs = new ChannelLogs(service.getConfiguration());

    public RolePlayHandler(GameService s, GameClient c) {
        super(s, c);

        if (client.getCharacter().getStore().isActive()){
            client.getCharacter().getStore().setActive(false);
            client.getCharacter().getCurrentMap().removeSeller(client.getCharacter());
        }

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())) {
            buf.append(ChannelGameMessageFormatter.addChannelMessage(client.getAccount().getEnabledChannels().toString()));

            buf.append(SpellGameMessageFormatter.spellListMessage(
                    Spell.toBaseSpellType(client.getCharacter().getSpells().values())
            ));

            buf.append(ChannelGameMessageFormatter.enabledEmotesMessage("")); //todo emotes

            buf.append(ApproachGameMessageFormatter.setRestrictionsMessage());

            buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                    client.getCharacter().getStatistics().getUsedPods(),
                    client.getCharacter().getStatistics().getMaxPods()
            ));

            buf.append(FriendGameMessageFormatter.
                    notifyFriendOnConnectMessage(client.getAccount().isNotifyFriendsOnConnect()));

            if (client.getCharacter().getGuildData() != null){
                buf.append(GuildGameMessageFormatter.statsMessage(
                        client.getCharacter().getGuildData().getGuild().getName(),
                        client.getCharacter().getGuildData().getGuild().getEmblem(),
                        client.getCharacter().getGuildData().getMember().getRights().toInt()
                ));
            }

            buf.append(BasicGameMessageFormatter.currentDateMessage(new Date()));

            buf.append(InfoGameMessageFormatter.welcomeMessage());

            if (client.getAccount().getLastConnection() != null && client.getAccount().getLastAddress() != null) {
                buf.append(InfoGameMessageFormatter.lastConnectionInformationMessage(
                        client.getAccount().getLastConnection(),
                        client.getAccount().getLastAddress()
                ));
            }
            else{
                buf.append(InfoGameMessageFormatter.currentAddressInformationMessage(client.getRemoteAddress()));
            }
        }

        client.getAccount().setLastConnection(new Date());
        client.getAccount().setLastAddress(client.getRemoteAddress());

        client.getAccount().getFriends().subscribe(this);
        service.getWorld().addObserver(this);
        client.getCharacter().addObserver(this);
        if (client.getCharacter().getGuildData() != null){
            client.getCharacter().getGuildData().getGuild().addObserver(this);
        }

        client.getTchatOut().info(service.getConfiguration().getMotd());
    }

    @Override
    public void parse(String packet) throws Exception {
        String[] args;
        switch (packet.charAt(0)){
            case 'A':
                switch (packet.charAt(1)){
                    case 'B':
                        parseBoostCharacteristicRequestMessage(
                                CharacteristicType.valueOf(Integer.parseInt(packet.substring(2)) - 6)
                        );
                        break;
                }
                break;

            case 'B':
                switch (packet.charAt(1)){
                    case 'A':
                        if (client.getAccount().hasRights()){
                            parseCommandRequestMessage(packet.substring(2), true);
                        }
                        else{
                            client.kick();
                        }
                        break;

                    case 'D':
                        parseCurrentDateRequestMessage();
                        break;

                    case 'M':
                        if (client.getAccount().isMuted()){
                            client.getTchatOut().error("Un Maître de Jeu vous a coupé la parole.");
                        }
                        else{
                            args = packet.substring(2).split("\\|");
                            if (args[0].length() > 1){
                                Character target = service.getWorld().getRepositoryManager().getCharacters().findByName(args[0]);
                                parseClientPrivateRequestMessage(target, args[1]);
                            }
                            else{
                                if (args[1].charAt(0) == service.getConfiguration().getCommandPrefix()){
                                    parseCommandRequestMessage(args[1].substring(1), false);
                                }
                                else{
                                    parseClientMultiRequestMessage(ChannelEnum.valueOf(packet.charAt(2)), args[1]);
                                }
                            }
                        }
                        break;

                    case 'W':
                        parseWhoisRequestMessage(packet.substring(2));
                        break;
                }
                break;

            case 'c':
                switch (packet.charAt(1)){
                    case 'C':
                        parseAddChannelRequestMessage(
                                packet.charAt(2) == '+',
                                ChannelEnum.valueOf(packet.charAt(3))
                        );
                        client.getSession().write(packet);
                        break;
                }
                break;

            case 'D':
                switch (packet.charAt(1)){
                    case 'C':
                        long npcId = Long.parseLong(packet.substring(2));
                        parseDialogRequestMessage(
                                client.getCharacter().getCurrentMap().getActor(npcId, Npc.class)
                        );
                        break;

                    case 'R':
                        args = packet.substring(2).split("\\|");
                        parseDialogReplyRequestMessage(
                                Integer.parseInt(args[0]),
                                Integer.parseInt(args[1])
                        );
                        break;

                    case 'V':
                        parseDialogEndRequestMessage();
                        break;
                }
                break;

            case 'E':
                switch (packet.charAt(1)){
                    case 'A':
                        parseTradeAcceptRequestMessage();
                        break;

                    case 'B':
                        args = packet.substring(2).split("\\|");
                        parseTradeBuyItemRequestMessage(
                                Long.parseLong(args[0]),
                                Integer.parseInt(args[1])
                        );
                        break;

                    case 'K':
                        parseTradeReadyRequestMessage();
                        break;

                    case 'M':
                        switch (packet.charAt(2)){
                            case 'G':
                                parseTradePutKamasRequestMessage(Long.parseLong(packet.substring(3)));
                                break;

                            case 'O':
                                args = packet.substring(4).split("\\|");
                                long itemId = Long.parseLong(args[0]);
                                int quantity = Integer.parseInt(args[1]);
                                switch (packet.charAt(3)){
                                    case '+':
                                        switch (client.getActions().current().getActionType()){
                                            case PLAYER_TRADE:
                                                parseTradeMoveItemRequestMessage(true, itemId, quantity);
                                                break;

                                            case STORE_MANAGEMENT:
                                                if (quantity == 0) {
                                                    parseStoreUpdateItemRequestMessage(itemId, Long.parseLong(args[2]));
                                                }
                                                else {
                                                    parseStoreAddItemRequestMessage(itemId, quantity, Long.parseLong(args[2]));
                                                }
                                                break;
                                        }
                                        break;

                                    case '-':
                                        switch (client.getActions().current().getActionType()){
                                            case PLAYER_TRADE:
                                                parseTradeMoveItemRequestMessage(false, itemId, quantity);
                                                break;

                                            case STORE_MANAGEMENT:
                                                parseStoreRemoveItemRequestMessage(itemId);
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;

                    case 'q':
                        parseStoreActivationRequestMessage();
                        break;

                    case 'R':
                        args = packet.substring(2).split("\\|");
                        switch (TradeTypeEnum.valueOf(Integer.parseInt(args[0]))){
                            case NPC:
                                long npcId = Long.parseLong(args[1]);
                                parseTradeNpcRequestMessage(
                                        client.getCharacter().getCurrentMap().getActor(npcId, Npc.class)
                                );
                                break;

                            case PLAYER:
                                parseTradePlayerRequestMessage(
                                        service.getWorld().getRepositoryManager().getCharacters()
                                        .findById(Long.parseLong(args[1]))
                                );
                                break;

                            case STORE_MANAGEMENT:
                                parseStoreManagementRequestMessage();
                                break;

                            case STORE:
                                parseTradeStoreRequestMessage(
                                        service.getWorld().getRepositoryManager().getCharacters()
                                                .findById(Long.parseLong(args[1]))
                                );
                                break;
                        }
                        break;

                    case 'S':
                        args = packet.substring(2).split("\\|");
                        parseTradeSellRequestMessage(
                                client.getCharacter().getBag().get(Long.parseLong(args[0])),
                                Integer.parseInt(args[1])
                        );
                        break;

                    case 'V':
                        parseTradeQuitRequestMessage();
                        break;
                }
                break;

            case 'F':
                switch (packet.charAt(1)){
                    case 'A':
                        parseAddFriendRequestMessage(packet.charAt(2) == '*' || packet.charAt(2) == '%' ?
                                packet.substring(3) :
                                packet.substring(2)
                        );
                        break;

                    case 'D':
                        parseDeleteFriendRequestMessage(packet.charAt(2) == '*' || packet.charAt(2) == '%' ?
                                packet.substring(3) :
                                packet.substring(2)
                        );
                        break;

                    case 'L':
                        parseFriendListRequestMessage();
                        break;

                    case 'O':
                        parseNotifyOnConnectMessage(packet.charAt(2) == '+');
                        break;
                }
                break;

            case 'G':
                switch (packet.charAt(1)){
                    case 'A':
                        parseGameActionRequestMessage(
                                ActionTypeEnum.valueOf(Integer.parseInt(packet.substring(2, 5))),
                                packet.substring(5)
                        );
                        break;

                    case 'C':
                        parseGameCreationRequestMessage();
                        break;

                    case 'I':
                        parseGameInformationsRequestMessage();
                        break;

                    case 'K':
                        parseGameActionEndRequestMessage(packet.charAt(2) == 'K', packet.substring(3));
                        break;
                }
                break;

            case 'g':
                switch (packet.charAt(1)){
                    case 'C':
                        args = packet.substring(2).split("\\|");
                        parseGuildCreationRequestMessage(
                                args[4],
                                new GuildEmblem(
                                        Integer.parseInt(args[0]),
                                        Integer.parseInt(args[1]),
                                        Integer.parseInt(args[2]),
                                        Integer.parseInt(args[3])
                                )
                        );
                        break;

                    case 'I':
                        switch (packet.charAt(2)){
                            case 'G':
                                parseGuildInformationsRequestMessage();
                                break;

                            case 'M':
                                parseGuildMembersListRequestMessage();
                                break;
                        }
                        break;

                    case 'J':
                        switch (packet.charAt(2)){
                            case 'E':
                                parseGuidInvitationDeclineRequestMessage();
                                break;

                            case 'K':
                                parseGuildInvitationAcceptRequestMessage();
                                break;

                            case 'R':
                                parseGuildInvitationRequestMessage(packet.substring(3));
                                break;
                        }
                        break;

                    case 'K': // gK<name> = kick
                        parseGuildKickMemberRequestMessage(packet.substring(2));
                        break;

                    case 'P': // gP<member>|<rank>|<rate>|<rights> = edit member
                        args = packet.substring(2).split("\\|");
                        parseGuildEditMemberRequestMessage(
                                Long.parseLong(args[0]),
                                Integer.parseInt(args[1]),
                                Byte.parseByte(args[2]),
                                Integer.parseInt(args[3])
                        );
                        break;

                    case 'V':
                        client.getSession().write(GuildGameMessageFormatter.quitCreationMessage());
                        break;
                }
                break;

            case 'O':
                switch (packet.charAt(1)){
                    case 'd':
                        args = packet.substring(2).split("\\|");
                        parseItemDeletionRequestMessage(
                                Long.parseLong(args[0]),
                                args.length > 1 ? Integer.parseInt(args[1]) : -1
                        );
                        break;

                    case 'M':
                        args = packet.substring(2).split("\\|");
                        parseItemMovementRequestMessage(
                                Long.parseLong(args[0]),
                                ItemPositionEnum.valueOf(Integer.parseInt(args[1]))
                        );
                        break;

                    case 'U':
                        parseItemUseRequestMessage(
                                client.getCharacter().getBag().get(Long.parseLong(packet.substring(2, packet.indexOf("|", 2))))
                        );
                        break;
                }
                break;

            case 'P':
                switch (packet.charAt(1)){
                    case 'A':
                        parsePartyAcceptInvitationRequestMessage();
                        break;

                    case 'I':
                        parsePartyInvitationRequestMessage(packet.substring(2));
                        break;

                    case 'R':
                        parsePartyDeclineInvitationRequestMessage();
                        break;

                    case 'V':
                        parsePartyQuitRequestMessage(
                                packet.length() > 2 ?
                                        client.getParty().getMember(Long.parseLong(packet.substring(2))) :
                                        null
                        );
                        break;
                }
                break;

            case 'S':
                switch (packet.charAt(1)){
                    case 'B':
                        parseBoostSpellRequestMessage(client.getCharacter().getSpells().get(
                                Integer.parseInt(packet.substring(2))
                        ));
                        break;

                    case 'M':
                        args = packet.substring(2).split("\\|");
                        if (args.length != 2){
                            throw new Exception("Invalid request: bad data received.");
                        }

                        parseMoveSpellRequestMessage(
                                client.getCharacter().getSpells().get(Integer.parseInt(args[0])),
                                Byte.parseByte(args[1])
                        );
                        break;
                }
                break;

            case 'W':
                switch (packet.charAt(1)){
                    case 'U':
                        parseUseWaypointRequestMessage(
                                service.getWorld().getRepositoryManager().getWaypoints()
                                    .findByMapId(Integer.parseInt(packet.substring(2)))
                        );
                        break;

                    case 'V':
                        parseCloseWaypointPanelRequestMessage();
                        break;
                }
                break;
        }
    }

    /**
     * GameClientHandler implementation
     */
    @Override
    public void onClosed() {
        service.getWorld().getLoginServerManager().setAccountDeconnected(client.getAccount().getId());

        client.getAccount().setOnline(null);
        client.getAccount().getFriends().unsubcribe(this);

        if (client.getParty() != null){
            client.getParty().deleteObserver(this);
            client.getParty().removeMember(client.getCharacter());
        }

        if (client.getCharacter().getGuildData() != null){
            client.getCharacter().getGuildData().getGuild().deleteObserver(this);
        }

        client.getCharacter().deleteObserver(this);
        client.getCharacter().getCurrentMap().deleteObserver(this);
        if (!client.getCharacter().getStore().isActive()){
            client.getCharacter().getCurrentMap().removeActor(client.getCharacter());
        }

        service.getWorld().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof IEvent){
            IEvent argEvent = (IEvent)arg;
            switch (argEvent.getEventType()){
                case MESSAGE:
                    MessageEvent messageEvent = (MessageEvent)argEvent;

                    if (client.getAccount().getEnabledChannels().contains(messageEvent.getChannel())){
                        client.getSession().write(ChannelGameMessageFormatter.clientMultiMessage(
                                messageEvent.getChannel().toChar(),
                                messageEvent.getActorId(),
                                messageEvent.getActorName(),
                                messageEvent.getMessage()
                        ));
                    }
                    break;

                case SYSTEM_MESSAGE:
                    SystemMessageEvent systemMessageEvent = (SystemMessageEvent)arg;
                    client.getTchatOut().log(systemMessageEvent.getMessage());
                    break;

                case ALERT_MESSAGE:
                    client.getSession().write(BasicGameMessageFormatter.alertMessage(
                            ((AlertMessageEvent)argEvent).getMessage()
                    ));
                    break;

                case MAP_UPDATE_ACTOR:
                    updateMapUpdateActorEvent((MapUpdateActorEvent) argEvent);
                    break;

                case PARTY_UPDATE_ACTOR:
                    updatePartyUpdateActorEvent((PartyUpdateMemberEvent)argEvent);
                    break;

                case LEVEL_UP:
                    LevelUpEvent levelUpEvent = (LevelUpEvent) argEvent;
                    if (levelUpEvent.getCharacter() == client.getCharacter()){
                        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                            buf.append(ApproachGameMessageFormatter.
                                    levelUpMessage(client.getCharacter().getExperience().getLevel()));
                            buf.append(client.getCharacter().getStatistics().
                                    getStatisticsMessage());
                        }

                    }
                    else if (client.getAccount().getFriends().isFriendWith(levelUpEvent.getCharacter())){
                        client.getTchatOut().info("Votre ami {0} est monté au niveau {1}.",
                                InfoGameMessageFormatter.urlize(levelUpEvent.getCharacter().getName()),
                                levelUpEvent.getCharacter().getExperience().getLevel()
                        );
                    }
                    break;

                case FRIEND_CONNECTION:
                    if (client.getAccount().isNotifyFriendsOnConnect()){
                        GameAccount friend = ((FriendConnectionEvent)argEvent).getFriend();
                        client.getSession().write(InfoGameMessageFormatter.friendConnectedMessage(
                                friend.getNickname(),
                                friend.getClient().getCharacter().getName()
                        ));
                    }
                    break;

                case INFO_MESSAGE:
                    client.getSession().write(InfoGameMessageFormatter.genericInfoMessage(((InfoMessageEvent)argEvent).getInfoType()));
                    break;

                case FLAG_UPDATE:
                    updateMapFlags((FlagUpdateEvent)argEvent);
                    break;

                case FIGHTS_UPDATE:
                    client.getSession().write(GameMessageFormatter.
                            fightCountMessage(client.getCharacter().getCurrentMap().getNbFights()));
                    break;

                case FLAG_ATTRIBUTE_UPDATE:
                    FlagAttributeUpdateEvent flagAttributeUpdateEvent = (FlagAttributeUpdateEvent)argEvent;
                    client.getSession().write(FightGameMessageFormatter.flagAttributeMessage(
                            flagAttributeUpdateEvent.isActive(),
                            flagAttributeUpdateEvent.getAttribute(),
                            flagAttributeUpdateEvent.getLeader().getId()
                    ));
                    break;
            }
        }
        else if (arg instanceof RolePlayMovement){
            RolePlayMovement movement = (RolePlayMovement)arg;

            client.getSession().write(GameMessageFormatter.actorMovementMessage(
                    movement.getActor().getId(),
                    movement.getPath()
            ));
        }
    }

    private void updateMapFlags(FlagUpdateEvent event) {
        Fight fight = event.getFight();
        switch (event.getUpdateType()){
            case ADD:
                try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                    buf.append(FightGameMessageFormatter.addFlagMessage(
                            fight.getId(),
                            fight.getFightType(),
                            fight.getChallengers().getLeader().toBaseRolePlayActorType(),
                            Fighter.toBaseFighterType(fight.getChallengers().getFighters()),
                            fight.getDefenders().getLeader().toBaseRolePlayActorType(),
                            Fighter.toBaseFighterType(fight.getDefenders().getFighters())
                    ));
                }
                break;

            case REMOVE:
                client.getSession().write(FightGameMessageFormatter.removeFlagMessage(fight.getId()));
                break;
        }
    }

    private void updatePartyUpdateActorEvent(PartyUpdateMemberEvent partyUpdateMemberEvent) {
        switch (partyUpdateMemberEvent.getPartyUpdateMemberType()){
            case ADD:
                client.getSession().write(PartyGameMessageFormatter.
                        addMemberMessage(partyUpdateMemberEvent.getMember().toBasePartyMemberType()));
                break;

            case REMOVE:
                if (partyUpdateMemberEvent.getMember() == client.getCharacter()){
                    client.getParty().deleteObserver(this);
                    client.setParty(null);
                    client.getSession().write(PartyGameMessageFormatter.quitMessage());
                }
                else{
                    client.getSession().write(PartyGameMessageFormatter.
                            removeMemberMessage(partyUpdateMemberEvent.getMember().getId()));
                }
                break;

            case REFRESH:
                client.getSession().write(PartyGameMessageFormatter.
                        refreshMemberMessage(partyUpdateMemberEvent.getMember().toBasePartyMemberType()));
                break;

            case LEADER:
                client.getSession().write(PartyGameMessageFormatter.
                        leaderInformationMessage(partyUpdateMemberEvent.getMember().getId()));
                break;
        }
    }

    private void updateMapUpdateActorEvent(MapUpdateActorEvent mapUpdateActorEvent){
        switch (mapUpdateActorEvent.getMapUpdateType()){
            case ADD:
                client.getSession().write(GameMessageFormatter.
                        showActorMessage(mapUpdateActorEvent.getActor().toBaseRolePlayActorType()));
                break;

            case REMOVE:
                client.getSession().write(GameMessageFormatter.
                        removeActorMessage(mapUpdateActorEvent.getActor().getPublicId()));
                break;

            case UPDATE:
                client.getSession().write(GameMessageFormatter.
                        updateActorMessage(mapUpdateActorEvent.getActor().toBaseRolePlayActorType()));
                break;

            case UPDATE_ACCESSORIES:
                if (mapUpdateActorEvent.getActor() instanceof Character){
                    client.getSession().write(ItemGameMessageFormatter.accessoriesMessage(
                            mapUpdateActorEvent.getActor().getPublicId(),
                            ((Character)mapUpdateActorEvent.getActor()).getAccessories()
                    ));
                }
                break;
        }
    }

    public void join(){
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            join(buf);
        }
    }

    public void join(NetworkStringBuffer buf){
        buf.append(GameMessageFormatter.gameCreationSuccessMessage());
        buf.append(client.getCharacter().getStatistics().getStatisticsMessage());
        buf.append(GameMessageFormatter.mapDataMessage(
                client.getCharacter().getCurrentMap().getId(),
                client.getCharacter().getCurrentMap().getDate(),
                client.getCharacter().getCurrentMap().getKey()
        ));
    }

    private void parseGameCreationRequestMessage() throws Exception {
        join();
    }

    private void parseCurrentDateRequestMessage(){
        client.getSession().write(BasicGameMessageFormatter.currentDateMessage(new Date()));
    }

    private void parseGameInformationsRequestMessage() {
        client.getCharacter().getCurrentMap().addActor(client.getCharacter());

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(GameMessageFormatter.showActorsMessage(RolePlayActors.toBaseRolePlayActorType(
                    client.getCharacter().getCurrentMap().getActors()
            )));
            buf.append(GameMessageFormatter.mapLoadedMessage());
            for (Fight fight : client.getCharacter().getCurrentMap().getFights()){
                if (fight.getState() != FightStateEnum.PLACE) continue;
                buf.append(FightGameMessageFormatter.addFlagMessage(
                        fight.getId(),
                        fight.getFightType(),
                        fight.getChallengers().getLeader().toBaseRolePlayActorType(),
                        Fighter.toBaseFighterType(fight.getChallengers().getFighters()),
                        fight.getDefenders().getLeader().toBaseRolePlayActorType(),
                        Fighter.toBaseFighterType(fight.getDefenders().getFighters())
                ));
            }
            buf.append(GameMessageFormatter.fightCountMessage(client.getCharacter().getCurrentMap().getNbFights()));
        }

        client.getCharacter().getCurrentMap().addObserver(this);
    }

    private void parseGameActionRequestMessage(ActionTypeEnum action, String extraData) throws Exception {
        String[] args;
        switch (action){
            case MOVEMENT:
                parseMovementRequestMessage(extraData);
                break;

            case ASK_FIGHT:
                parseChallengeRequestMessage(service.getWorld().getRepositoryManager().getCharacters().findById(Long.parseLong(extraData)));
                break;

            case ACCEPT_FIGHT:
                parseChallengeAcceptRequestMessage();
                break;

            case DECLINE_FIGHT:
                parseChallengeDeclineRequestMessage();
                break;

            case JOIN_FIGHT:
                args = extraData.split(";");
                parseJoinFightRequestMessage(
                        client.getCharacter().getCurrentMap().getFight(Long.parseLong(args[0])),
                        Long.parseLong(args[1])
                );
                break;

            case INTERACTIVE_OBJECT:
                args = extraData.split(";");
                parseUseInteractiveObjectRequestMessage(
                        Short.parseShort(args[0]),
                        InteractiveObjectTypeEnum.valueOf(Integer.parseInt(args[1]))
                );
                break;
        }
    }

    private void parseUseInteractiveObjectRequestMessage(short targetCell, InteractiveObjectTypeEnum type) throws Exception {
        switch (type){
            case WAYPOINT:
                parseTryOpenWaypointPanelRequestMessage();
                break;

            case SAVE_WAYPOINT:
                parseSaveWaypointRequestMessage();
                break;
        }
    }

    private void parseSaveWaypointRequestMessage() throws Exception {
        Waypoint waypoint = client.getCharacter().getCurrentMap().getWaypoint();
        if (waypoint == null){
            throw new Exception("There is not waypoint here");
        }

        client.getCharacter().setMemorizedMap(waypoint.getMap());
        client.getCharacter().setMemorizedCell(waypoint.getCellId());

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            if (!client.getCharacter().getWaypoints().contains(waypoint)){
                client.getCharacter().getWaypoints().add(waypoint);

                buf.append(InfoGameMessageFormatter.newZaapMessage());
            }

            buf.append(InfoGameMessageFormatter.waypointSavedMessage());
        }

    }

    private void parseMovementRequestMessage(String path) throws Exception {
        if (client.isBusy()){
            client.getSession().write(BasicGameMessageFormatter.noOperationMessage());
        }
        else{
            RolePlayMovement movement = new RolePlayMovement(path, client);

            try {
                movement.begin();
                client.getActions().push(movement);
            }
            catch(GameActionException e){
                client.getTchatOut().error(e.getMessage());
            }
        }
    }

    private void parseGameActionEndRequestMessage(boolean success, String args) throws Exception {
        if (success){
            client.getActions().pop().end();
        }
        else {
            IGameAction action = client.getActions().pop();
            switch (action.getActionType()){
                case MOVEMENT:
                    RolePlayMovement movement = (RolePlayMovement)action;
                    movement.cancel(Short.parseShort(args.substring(2)));
                    break;

                default:
                    action.cancel();
                    break;
            }
        }
    }

    public void parseClientPrivateRequestMessage(Character target, String message) {
        if (target == null){
            client.getSession().write(ChannelGameMessageFormatter.clientMultiErrorMessage());
        }
        else if (!target.getOwner().isOnline()){
            client.getSession().write(ChannelGameMessageFormatter.clientMultiErrorMessage());
        }
        else if (client.getAccount().getFriends().isEnnemyWith(target)){
            client.getSession().write(ChannelGameMessageFormatter.clientMultiErrorMessage());
        }
        else{
            target.getOwner().getClient().getSession().write(ChannelGameMessageFormatter.clientPrivateMessage(
                    true,
                    client.getCharacter().getId(),
                    client.getCharacter().getName(),
                    message
            ));

            client.getSession().write(ChannelGameMessageFormatter.clientPrivateMessage(
                    false,
                    target.getId(),
                    target.getName(),
                    message
            ));
        }
    }

    public void parseClientMultiRequestMessage(ChannelEnum channel, String message) throws Exception {
        if (!client.getAccount().getEnabledChannels().contains(channel)){
            client.getSession().write(BasicGameMessageFormatter.noOperationMessage());
        }
        else switch (channel){
            case General:
                client.getCharacter().getCurrentMap().speak(client.getCharacter(), channel, message);
                break;

            case Admin: if (!client.getAccount().hasRights()) break;
            case Alignment:
            case Trade:
            case Recruitment:
                if (channelLogs.isValid(channel))
                {
                    channelLogs.set(channel);

                    service.getWorld().speak(
                            client.getCharacter(),
                            channel,
                            message
                    );
                }
                else{
                    client.getSession().write(InfoGameMessageFormatter.
                            floodMessage(channelLogs.getRemaining(channel, service.getConfiguration())));
                }
                break;

            case Guild:
                if (client.getCharacter().getGuildData() != null){
                    client.getCharacter().getGuildData().getGuild().speak(client.getCharacter(), message);
                }
                else{
                    throw new Exception("You have not got a guild.");
                }
                break;

            case Party:
                if (client.getParty() != null){
                    client.getParty().speak(client.getCharacter(), message);
                }
                else{
                    throw new Exception("You have not got a party.");
                }
                break;

            default:
                break;
        }
    }

    private void parseChallengeRequestMessage(Character target) throws Exception {
        if (target == null)
            throw new Exception("unknown target");

        GameClient targetClient = target.getOwner().getClient();
        if (!client.getCharacter().getCurrentMap().canFight()){
            client.getTchatOut().error("Impossible de combattre sur cette map.");
        }
        else if (targetClient.isBusy() || client.isBusy()){
            client.getTchatOut().error("La cible est occupée.");
        }
        else if (target.getCurrentMap() != client.getCharacter().getCurrentMap()){
            client.getTchatOut().error("La cible n'est pas sur la même map que vous.");
        }
        else{
            ChallengeRequestInvitation invitation = new ChallengeRequestInvitation(client, targetClient);
            client.getActions().push(invitation);
            targetClient.getActions().push(invitation);

            invitation.begin();
        }
    }

    private void parseChallengeAcceptRequestMessage() throws Exception {
        if (client.getActions().peek().getActionType() != GameActionType.CHALLENGE_REQUEST_INVITATION)
            throw new Exception("Bad request: current action isn't a [" + GameActionType.CHALLENGE_REQUEST_INVITATION + "].");

        ChallengeRequestInvitation invitation = (ChallengeRequestInvitation)client.getActions().pop();

        if (invitation.getSender() == client)
            throw new Exception("Bad request: sender can't accept challenge.");

        if (invitation.getSender().getActions().peek().getActionType() != GameActionType.CHALLENGE_REQUEST_INVITATION)
            throw new Exception("Bad request: target's current action isn't a [" + GameActionType.CHALLENGE_REQUEST_INVITATION + "].");

        invitation.getSender().getActions().pop();

        invitation.accept();
    }

    private void parseChallengeDeclineRequestMessage() throws Exception {
        if (client.getActions().peek().getActionType() != GameActionType.CHALLENGE_REQUEST_INVITATION){
            throw new Exception("Bad request: current action isn't a [" + GameActionType.CHALLENGE_REQUEST_INVITATION + "].");
        }

        ChallengeRequestInvitation invitation = (ChallengeRequestInvitation) client.getActions().pop();

        if (invitation.getSender() == client){
            if (invitation.getTarget().getActions().peek().getActionType() != GameActionType.CHALLENGE_REQUEST_INVITATION){
                throw new Exception("Bad request: target's current action isn't a [" + GameActionType.CHALLENGE_REQUEST_INVITATION + "].");
            }
            invitation.getTarget().getActions().pop();
        }
        else{
            if (invitation.getSender().getActions().peek().getActionType() != GameActionType.CHALLENGE_REQUEST_INVITATION){
                throw new Exception("Bad request: sender's current action isn't a [" + GameActionType.CHALLENGE_REQUEST_INVITATION + "].");
            }
            invitation.getSender().getActions().pop();
        }

        invitation.decline();
    }

    private void parseJoinFightRequestMessage(Fight fight, long fighterId) throws Exception {
        if (fight == null){
            throw new Exception("Unknown fight.");
        }
        else if (fight.getState() != FightStateEnum.PLACE){
            throw new Exception("You can only join a fight that is not started.");
        }

        IFighter fighter = fight.getFighter(fighterId);

        if (fighter == null){
            throw new Exception("Unknown fighter.");
        }
        else if (!fighter.getTeam().isAvailable()){
            client.getSession().write(FightGameMessageFormatter.fightJoinErrorMessage(FightJoinErrorEnum.UNAVAILABLE));
        }
        else if (fighter.getTeam().getAttribute(FightAttributeType.DENY_ALL)){
            client.getSession().write(FightGameMessageFormatter.fightJoinErrorMessage(FightJoinErrorEnum.DENIED));
        }
        else if (fighter.getTeam().getAttribute(FightAttributeType.ALLOW_PARTY) &&
                 (client.getParty() == null || !client.getParty().containsMember(fighter.getId())))
        {
            client.getSession().write(FightGameMessageFormatter.fightJoinErrorMessage(FightJoinErrorEnum.DENIED));
        }
        else{
            client.getCharacter().getCurrentMap().deleteObserver(this);
            client.getCharacter().getCurrentMap().removeActor(client.getCharacter());

            client.setHandler(new FightHandler(
                    service,
                    client,
                    fight,
                    fighter.getTeam().getTeamType(),
                    this
            ));
        }
    }

    private void parseBoostSpellRequestMessage(Spell spell) {
        int cost = spell.getLevel();

        if (spell.getLevel() >= service.getConfiguration().getMaxLevelSpell()){
            client.getSession().write(SpellGameMessageFormatter.boostSpellErrorMessage());
        }
        else if (cost > client.getCharacter().getSpellsPoints()){
            client.getSession().write(SpellGameMessageFormatter.boostSpellErrorMessage());
        }
        else{
            client.getCharacter().addSpellsPoints((short) -cost);
            spell.addLevel(NumUtils.ONE_SHORT);

            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                buf.append(SpellGameMessageFormatter.boostSpellSuccessMessage(
                        spell.getTemplate().getId(),
                        spell.getLevel()
                ));

                buf.append(client.getCharacter().getStatistics().getStatisticsMessage());
            }
        }
    }

    private void parseMoveSpellRequestMessage(Spell spell, byte position) {
        client.getSession().write(BasicGameMessageFormatter.noOperationMessage());

        if (spell != null){
            if (position == -1){
                spell.setPosition((byte) -1);
            }
            else{
                for (Spell s : client.getCharacter().getSpells().values()){
                    if (s.getPosition() == position){
                        s.setPosition((byte) -1);
                    }
                }

                spell.setPosition(position);
            }
        }
    }

    private void parseCommandRequestMessage(String command, boolean console) {
        service.getCommandFactory().parse(client, command, console);
    }

    private void parseAddChannelRequestMessage(boolean add, ChannelEnum channel) {
        if (add){
            if (!client.getAccount().getEnabledChannels().contains(channel)){
                client.getAccount().getEnabledChannels().add(channel);
            }
        }
        else{
            client.getAccount().getEnabledChannels().remove(channel);
        }
    }

    private void parsePartyInvitationRequestMessage(String targetName) throws GameActionException {
        Character targetCharacter = service.getWorld().getRepositoryManager().getCharacters().findByName(targetName);
        if (targetCharacter != null && targetCharacter != client.getCharacter()){
            GameClient target = targetCharacter.getOwner().getClient();

            if (target == null){
                client.getSession().write(PartyGameMessageFormatter.targetNotFoundMessage(targetName));
            }
            else if (target.isBusy()){
                client.getTchatOut().info("{0} est occupé.", InfoGameMessageFormatter.urlize(targetName));
            }
            else if (target.getParty() != null){
                client.getSession().write(PartyGameMessageFormatter.targetAlreadyInPartyMessage(targetName));
            }
            else{
                PartyRequestInvitation invitation = new PartyRequestInvitation(client, target);

                client.getActions().push(invitation);
                target.getActions().push(invitation);

                invitation.begin();
            }
        }
        else{
            client.getSession().write(PartyGameMessageFormatter.targetNotFoundMessage(targetName));
        }
    }

    private void parsePartyAcceptInvitationRequestMessage() throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.PARTY_REQUEST_INVITATION)){
            throw new Exception("You are not invited.");
        }

        PartyRequestInvitation invitation = client.getActions().current();

        if (invitation.getSource() == client){
            throw new Exception("Only target can accept.");
        }

        invitation.accept();
    }

    private void parsePartyDeclineInvitationRequestMessage() throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.PARTY_REQUEST_INVITATION)){
            throw new Exception("You are not invited.");
        }

        PartyRequestInvitation invitation = client.getActions().current();
        invitation.decline();
    }

    private void parsePartyQuitRequestMessage(Character member) throws Exception {
        if (client.getParty() == null){
            throw new Exception("You have not got party.");
        }
        else if (member != null){
            if (client.getParty().getLeader() != client.getCharacter()){
                client.getTchatOut().error("Vous n'êtes pas le chef du groupe.");
            }
            else{
                client.getParty().removeMember(member);
            }
        }
        else{
            client.getParty().removeMember(client.getCharacter());

            client.getSession().write(PartyGameMessageFormatter.quitMessage());
        }
    }

    private void parseItemDeletionRequestMessage(long itemId, int quantity) throws Exception {
        Item item = client.getCharacter().getBag().get(itemId);
        if (item != null){
            item.addQuantity(-quantity);

            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                if (item.getQuantity() > 0){
                    buf.append(ItemGameMessageFormatter.
                            quantityMessage(item.getId(), item.getQuantity()));
                }
                else{
                    client.getCharacter().getBag().remove(itemId);

                    buf.append(ItemGameMessageFormatter.
                            deleteMessage(item.getId()));
                }

                buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                        client.getCharacter().getStatistics().getUsedPods(),
                        client.getCharacter().getStatistics().getMaxPods()
                ));
            }

        }
        else{
            throw new Exception("Invalid request: unknown item requested.");
        }
    }

    private void parseItemMovementRequestMessage(long itemId, ItemPositionEnum position) throws Exception {
        Item item = client.getCharacter().getBag().get(itemId);
        if (item == null){
            throw new Exception("Invalid request: item unknown.");
        }
        else if (item.getPosition() == position){
            throw new Exception("Invalid request: item is already at " + position);
        }
        else{
            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                if (position.equipment()){
                    if (!Bag.validMovement(item.getTemplate(), position)){
                        throw new Exception("Invalid request: a " + item.getTemplate().getType() + " cannot take place on " + position);
                    }
                    else if (!client.getCharacter().getBag().isAvailable(position)){
                        buf.append(ItemGameMessageFormatter.
                                fullInventoryMessage());
                    }
                    else if (client.getCharacter().getExperience().getLevel() < item.getTemplate().getLevel()){
                        buf.append(ItemGameMessageFormatter.
                                tooSlowLevelMessage());
                    }
                    else if (!Bag.canWearRing(client.getCharacter().getBag(), position, item.getTemplate())) {
                        buf.append(ItemGameMessageFormatter.
                                alreadyEquipedMessage());
                    }
                    else if (!ItemTemplate.isValid(service.getConfiguration().getScriptEngine(), client.getCharacter(), item.getTemplate())){
                        buf.append(InfoGameMessageFormatter.cantWearItemMessage());
                    }
                    else {
                        if (item.getQuantity() > 1){
                            Item old = item;

                            item = old.copy();
                            item.setOwner(client.getCharacter());
                            item.setQuantity(1);
                            old.addQuantity(-1);

                            client.getCharacter().getBag().add(item);

                            buf.append(ItemGameMessageFormatter.quantityMessage(old.getId(), old.getQuantity()));
                        }

                        item.setPosition(position);

                        client.getCharacter().getStatistics().resetEquipments();

                        item.apply(client.getCharacter().getStatistics());

                        buf.append(ItemGameMessageFormatter.
                                itemMovementMessage(item.getId(), item.getPosition()));

                        if (item.getTemplate().getItemSet() != null){
                            Collection<Item> itemSetItems = ItemSetTemplate.ofItemSet(
                                    item.getTemplate().getItemSet(),
                                    client.getCharacter().getBag().getEquipedItems()
                            );

                            if (itemSetItems.size() >= 2){
                                Collection<ItemEffect> effects = item.getTemplate().getItemSet().apply(
                                        itemSetItems.size(),
                                        client.getCharacter().getStatistics()
                                );

                                buf.append(client.getCharacter().getStatistics().
                                        getStatisticsMessage());
                                buf.append(ItemGameMessageFormatter.addItemSetMessage(
                                        item.getTemplate().getItemSet().getId(),
                                        Item.toBaseItemType(itemSetItems),
                                        ItemEffect.toBaseItemEffectType(effects)
                                ));
                            }
                            else{
                                buf.append(client.getCharacter().getStatistics().
                                        getStatisticsMessage());
                                buf.append(ItemGameMessageFormatter.
                                        removeItemSetMessage(item.getTemplate().getItemSet().getId()));
                            }
                        }

                        buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                                client.getCharacter().getStatistics().getUsedPods(),
                                client.getCharacter().getStatistics().getMaxPods()
                        ));

                        client.getCharacter().getCurrentMap().updateActorAccessories(client.getCharacter());
                        if (client.getParty() != null){
                            client.getParty().refresh(client.getCharacter());
                        }
                    }
                }
                else{
                    Item same = client.getCharacter().getBag().getSame(item);
                    if (same != null){
                        same.addQuantity(1);
                        item.addQuantity(-1);
                    }

                    item.setPosition(position);
                    client.getCharacter().getStatistics().refresh();

                    buf.append(ItemGameMessageFormatter.
                            itemMovementMessage(item.getId(), item.getPosition()));
                    buf.append(client.getCharacter().getStatistics().
                            getStatisticsMessage());
                    buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                            client.getCharacter().getStatistics().getUsedPods(),
                            client.getCharacter().getStatistics().getMaxPods()
                    ));

                    client.getCharacter().getCurrentMap().updateActorAccessories(client.getCharacter());
                    if (client.getParty() != null){
                        client.getParty().refresh(client.getCharacter());
                    }
                }
            }
        }
    }

    private void parseFriendListRequestMessage() {
        client.getSession().write(FriendGameMessageFormatter.
                friendListMessage(client.getAccount().getFriends().toBaseFriendType()));
    }

    private void parseAddFriendRequestMessage(String raw) {
        GameAccount target = service.getWorld().getRepositoryManager().getAccounts()
                                .findByIdOrNickname(raw);
        if (target == null){
            Character targetChr = service.getWorld().getRepositoryManager().getCharacters()
                                    .findByIdOrName(raw);
            if (targetChr == null){
                client.getSession().write(FriendGameMessageFormatter.
                        addFriendErrorMessage(FriendAddErrorEnum.NOT_FOUND));
                return;
            }
            else {
                target = targetChr.getOwner();
            }
        }

        if (target == client.getAccount()){
            client.getSession().write(FriendGameMessageFormatter.
                    addFriendErrorMessage(FriendAddErrorEnum.EGOCENTRIC));
        }
        else if (client.getAccount().getFriends().isFriendWith(target.getId())){
            client.getSession().write(FriendGameMessageFormatter.
                    addFriendErrorMessage(FriendAddErrorEnum.ALREADY_ADDED));
        }
        else{
            try {
                client.getAccount().getFriends().addFriend(target, this);

                client.getSession().write(FriendGameMessageFormatter.
                        addFriendMessage(client.getAccount().getFriends().toBaseFriendType(target.getId())));
            } catch (FullListException e) {
                client.getSession().write(FriendGameMessageFormatter.
                        addFriendErrorMessage(FriendAddErrorEnum.FULL_LIST));
            }
        }
    }

    private void parseDeleteFriendRequestMessage(String raw) {
        GameAccount target = service.getWorld().getRepositoryManager().getAccounts()
                                .findByIdOrNickname(raw);
        if (target == null){
            Character targetChr = service.getWorld().getRepositoryManager().getCharacters()
                                      .findByIdOrName(raw);
            if (targetChr == null){
                client.getSession().write(FriendGameMessageFormatter.deleteFriendErrorMessage());
                return;
            }

            target = targetChr.getOwner();
        }

        if (client.getAccount().getFriends().removeFriend(target.getId(), this)){
            client.getSession().write(FriendGameMessageFormatter.deleteFriendMessage());
        }
        else{
            client.getSession().write(FriendGameMessageFormatter.deleteFriendErrorMessage());
        }
    }

    private void parseNotifyOnConnectMessage(boolean notifyOnConnect) {
        client.getAccount().setNotifyFriendsOnConnect(notifyOnConnect);
        client.getSession().write(BasicGameMessageFormatter.noOperationMessage());
    }

    private void parseTradePlayerRequestMessage(Character targetCharacter) throws GameActionException {
        if (targetCharacter == null || !targetCharacter.getOwner().isOnline()){
            client.getSession().write(TradeGameMessageFormatter.
                    tradeRequestErrorMessage(TradeErrorEnum.UnknownOrOffline));
        }
        else if (targetCharacter.getOwner().getClient().isBusy()){
            client.getSession().write(TradeGameMessageFormatter.
                    tradeRequestErrorMessage(TradeErrorEnum.Busy));
        }
        else if (targetCharacter.getOwner().getFriends().isEnnemyWith(client.getAccount()) ||
                 client.getAccount().getFriends().isEnnemyWith(targetCharacter))
        {
            client.getSession().write(BasicGameMessageFormatter.
                    noOperationMessage());
        }
        else{
            GameClient target = targetCharacter.getOwner().getClient();

            TradePlayerRequestInvitation invitation = new TradePlayerRequestInvitation(
                    service.getWorld(),
                    client,
                    target
            );

            client.getActions().push(invitation);
            target.getActions().push(invitation);

            invitation.begin();
        }
    }

    private void parseTradeAcceptRequestMessage() throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.TRADE_PLAYER_REQUEST_INVITATION)){
            throw new Exception("You are not in a trade.");
        }

        TradePlayerRequestInvitation invitation = client.getActions().current();

        invitation.accept();
    }

    private void parseTradeQuitRequestMessage() throws Exception {
        GameActionType currentAction = client.getActions().current().getActionType();

        if (currentAction != GameActionType.TRADE_PLAYER_REQUEST_INVITATION &&
            currentAction != GameActionType.PLAYER_TRADE &&
            currentAction != GameActionType.NPC_TRADE &&
            currentAction != GameActionType.STORE_MANAGEMENT &&
            currentAction != GameActionType.STORE)
        {
            throw new Exception("You are not in a trade.");
        }

        client.getActions().pop().cancel();
    }

    private void parseTradePutKamasRequestMessage(long kamas) throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.PLAYER_TRADE)){
            throw new Exception("You are not in a trade.");
        }

        PlayerTrade trade = client.getActions().current();
        trade.putKamas(client.getTrader(), kamas);
    }

    private void parseTradeReadyRequestMessage() throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.PLAYER_TRADE)){
            throw new Exception("You are not in a trade.");
        }

        client.getTrader().setReady();
    }

    private void parseTradeMoveItemRequestMessage(boolean add, long itemId, int quantity) throws Exception {
        PlayerTrade trade = client.getActions().current();
        if (add){
            trade.add(client.getTrader(), client.getCharacter().getBag().get(itemId), quantity);
        }
        else{
            trade.remove(client.getTrader(), client.getTrader().getBag().get(itemId), quantity);
        }
    }

    private void parseBoostCharacteristicRequestMessage(CharacteristicType charac) throws Exception {
        short stats = client.getCharacter().getStatistics().get(charac).getBase();

        short[] bonusAndCost = client.getCharacter().getBreed().getBonusAndCost(charac, stats);
        short bonus = bonusAndCost[0], cost = bonusAndCost[1];

        if (cost > client.getCharacter().getStatsPoints()){
            client.getSession().write(ApproachGameMessageFormatter.boostCharacteristicErrorMessage());
        }
        else{

            client.getCharacter().addStatsPoints((short) -cost);
            client.getCharacter().getStatistics().get(charac).addBase(bonus);

            client.getSession().write(client.getCharacter().getStatistics().getStatisticsMessage());
        }
    }

    private void parseDialogRequestMessage(Npc npc) throws Exception {
        if (npc == null){
            throw new Exception("Unknown NPC.");
        }
        else if (client.isBusy()){
            throw new Exception("You're busy.");
        }
        else{
            NpcDialogAction action = new NpcDialogAction(client, npc);
            client.getActions().push(action);

            action.begin();
        }
    }

    private void parseDialogReplyRequestMessage(int questionId, int responseId) throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.NPC_DIALOG)){
            throw new Exception("You are not talking to a NPC.");
        }
        else{
            NpcDialogAction dialog = client.getActions().current();
            dialog.reply(responseId);
        }
    }

    private void parseDialogEndRequestMessage() throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.NPC_DIALOG)){
            throw new Exception("You are not talking with a NPC.");
        }
        else{
            client.getActions().pop().end();
        }
    }

    private void parseTradeNpcRequestMessage(Npc npc) throws Exception {
        if (npc == null){
            throw new Exception("Unknown NPC.");
        }
        else if (npc.getTemplate().getType() != NpcTypeEnum.BUY_SELL &&
                 npc.getTemplate().getType() != NpcTypeEnum.BUY &&
                 npc.getTemplate().getType() != NpcTypeEnum.SELL)
        {
            throw new Exception("You can not perform this action.");
        }
        else{
            NpcTrade trade = new NpcTrade(client, npc.getTemplate());
            client.getActions().push(trade);

            trade.begin();
        }
    }

    private void parseTradeBuyItemRequestMessage(long itemId, int quantity) throws GameActionException {
        PurchasableTrade trade = client.getActions().current();
        trade.buy(itemId, quantity);
    }

    private void parseTradeSellRequestMessage(Item item, int quantity) throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.NPC_TRADE)){
            throw new Exception("You are not in trade with a NPC.");
        }

        if (item == null){
            throw new Exception("Unknown item.");
        }

        NpcTrade trade = client.getActions().current();
        trade.sell(item, quantity);
    }

    private void parseGuildCreationRequestMessage(String name, GuildEmblem emblem) {
        if (client.getCharacter().getGuildData() != null){
            client.getSession().write(GuildGameMessageFormatter.alreadyHaveGuildMessage());
        }
        else if (service.getWorld().getRepositoryManager().getGuilds().exists(name)){
            client.getSession().write(GuildGameMessageFormatter.nameExistsMessage());
        }
        else if (service.getWorld().getRepositoryManager().getGuilds().exists(emblem)){
            client.getSession().write(GuildGameMessageFormatter.emblemExistsMessage());
        }
        else{
            Guild guild = service.getWorld().getRepositoryManager().getGuilds().createDefault(
                    client.getCharacter(),
                    name,
                    emblem
            );
            guild.addObserver(this);

            GuildMember member = client.getCharacter().getGuildData().getMember();

            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                buf.append(GuildGameMessageFormatter.statsMessage(
                        guild.getName(),
                        guild.getEmblem(),
                        member.getRights().toInt()
                ));
                buf.append(GuildGameMessageFormatter.creationSuccessMessage());
                buf.append(GuildGameMessageFormatter.quitCreationMessage());
            }
        }
    }

    private void parseGuildInformationsRequestMessage() throws Exception {
        if (client.getCharacter().getGuildData() == null){
            throw new Exception("You haven't got a guild.");
        }

        Guild guild = client.getCharacter().getGuildData().getGuild();

        client.getSession().write(GuildGameMessageFormatter.informationsGeneralMessage(
                guild.isValid(),
                guild.getExperience().getLevel(),
                guild.getExperience().getExperience(),
                guild.getExperience().min(),
                guild.getExperience().max()
        ));
    }

    private void parseGuildMembersListRequestMessage() throws Exception {
        if (client.getCharacter().getGuildData() == null){
            throw new Exception("You haven't got a guild.");
        }

        Guild guild = client.getCharacter().getGuildData().getGuild();

        client.getSession().write(GuildGameMessageFormatter.
                membersListMessage(guild.toBaseGuildMemberType()));
    }

    private void parseGuildInvitationRequestMessage(String name) throws Exception {
        Character targetChr = service.getWorld().getRepositoryManager().getCharacters().findByName(name);

        if (targetChr == null){
            throw new Exception("Unknown target.");
        }
        else if (!targetChr.getOwner().isOnline()){
            throw new Exception("Offline target.");
        }
        else if (targetChr.getOwner().getClient().isBusy()){
            client.getTchatOut().error("{0} est occupé.", targetChr.urlize());
        }
        else{
            GameClient target = targetChr.getOwner().getClient();

            IInvitation invitation = new GuildRequestInvitation(
                    service.getConfiguration(),
                    service.getWorld().getRepositoryManager(),
                    client,
                    target
            );

            client.getActions().push(invitation);
            target.getActions().push(invitation);

            invitation.begin();
        }
    }

    private void parseGuildInvitationAcceptRequestMessage() throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.GUILD_REQUEST_INVITATION)){
            throw new Exception("You are not invited.");
        }

        GuildRequestInvitation invitation = client.getActions().current();
        invitation.accept();

        client.getCharacter().getGuildData().getGuild().addObserver(this);
    }

    private void parseGuidInvitationDeclineRequestMessage() throws Exception {
        if (!client.getActions().currentActionIs(GameActionType.GUILD_REQUEST_INVITATION)){
            throw new Exception("You are not invited.");
        }

        GuildRequestInvitation invitation = client.getActions().current();
        invitation.decline();
    }

    private void parseGuildKickMemberRequestMessage(String name) throws Exception {
        if (client.getCharacter().getGuildData() == null){
            throw new Exception("You have not got a guild.");
        }

        GuildMember me = client.getCharacter().getGuildData().getMember();

        Guild guild = client.getCharacter().getGuildData().getGuild();
        GuildMember target = guild.getMember(name);

        if (target == null){
            throw new Exception("Unknown target.");
        }
        if (target.isLeader()){
            client.getTchatOut().error("You can not kick the leader.");
        }

        if (target == me || me.getRights().get(GuildMemberRightsEnum.BAN)){
            guild.removeMember(target);

            client.getSession().write(GuildGameMessageFormatter.kickLocalSuccessMessage(
                    client.getCharacter().getName(),
                    target.getMember().getName()
            ));

            if (target != me && target.getMember().getOwner().isOnline()){
                target.getMember().getOwner().getClient().getSession().write(GuildGameMessageFormatter.kickRemoteSuccessMessage(
                        client.getCharacter().getName()
                ));
            }
        }
        else{
            throw new Exception("You don't have enough rights for this action.");
        }
    }

    private void parseGuildEditMemberRequestMessage(long memberId, int rank, byte rate, int rights) throws Exception {
        if (client.getCharacter().getGuildData() == null){
            throw new Exception("You have not got a guild.");
        }

        Guild guild = client.getCharacter().getGuildData().getGuild();
        GuildMember me = client.getCharacter().getGuildData().getMember();

        GuildMember target = guild.getMember(memberId);
        if (target == null){
            throw new Exception("Unknown target.");
        }

        if (me != target && target.isLeader()){
            return; // can't edit the leader
        }

        if (me.getRights().get(GuildMemberRightsEnum.RANK)){
            if (rank == Guild.LEADER){
                if (!me.isLeader()){
                    client.getTchatOut().error("You can only give leader rank if you are the leader.");
                }
                else{
                    guild.setLeader(target);
                }
            }
            else{
                target.setRank(rank);
            }
        }

        if (me.getRights().get(GuildMemberRightsEnum.RATES_XP_ALL)){
            target.setExperienceRate(rate);
        }

        if (me.getRights().get(GuildMemberRightsEnum.RIGHTS)){
            target.getRights().fromInt(rights);
        }

        client.getSession().write(GuildGameMessageFormatter.statsMessage(
                guild.getName(),
                guild.getEmblem(),
                me.getRights().toInt()
        ));

        if (target.getMember().getOwner().isOnline()){
            target.getMember().getOwner().getClient().getSession().write(GuildGameMessageFormatter.statsMessage(
                    guild.getName(),
                    guild.getEmblem(),
                    target.getRights().toInt()
            ));
        }
    }

    private void parseItemUseRequestMessage(Item item) throws Exception {
        if (item == null){
            throw new Exception("Unknown item.");
        }
        else if (!item.getTemplate().isUsable()){
            throw new Exception("This item is not usable.");
        }

        UsableItemTemplate tpl = (UsableItemTemplate)item.getTemplate();
        tpl.use(client);

        item.addQuantity(-1);

        if (item.getQuantity() > 0){
            client.getSession().write(ItemGameMessageFormatter.quantityMessage(
                    item.getId(),
                    item.getQuantity()
            ));
        }
        else{
            client.getCharacter().getBag().remove(item.getId());
            client.getSession().write(ItemGameMessageFormatter.deleteMessage(item.getId()));
        }
    }

    private void parseWhoisRequestMessage(String raw) {
        Character target = service.getWorld().getRepositoryManager().getCharacters().findByIdOrName(raw);
        if (target != null){
            client.getSession().write(BasicGameMessageFormatter.whoisMessage(
                    target.getOwner().getNickname(),
                    target.getName()
            ));
        }
        else{
            client.getSession().write(BasicGameMessageFormatter.whoisErrorMessage(raw));
        }
    }

    private void parseTryOpenWaypointPanelRequestMessage() throws Exception {
        if (client.getActions().currentActionIs(GameActionType.MOVEMENT)){
            RolePlayMovement movement = client.getActions().current();
            movement.getEndFuture().addListener(new Future.Listener<RolePlayMovement>() {
                @Override
                public void listen(RolePlayMovement obj) throws Exception {
                    parseOpenWaypointPanelRequestMessage();
                }
            });
        }
        else if (client.isBusy()){
            throw new Exception("You are busy.");
        }
        else{
            parseOpenWaypointPanelRequestMessage();
        }
    }

    private void parseOpenWaypointPanelRequestMessage() throws Exception {
        WaypointPanelAction action = new WaypointPanelAction(client);
        client.getActions().push(action);

        action.begin();
    }

    private void parseUseWaypointRequestMessage(Waypoint waypoint) throws Exception {
        if (waypoint == null){
            throw new Exception("Unknown waypoint");
        }

        WaypointPanelAction action = (WaypointPanelAction) client.getActions().pop();
        action.use(waypoint);
    }

    private void parseCloseWaypointPanelRequestMessage() throws GameActionException {
        WaypointPanelAction action = (WaypointPanelAction) client.getActions().pop();
        action.end();
    }

    private void parseStoreManagementRequestMessage() throws GameActionException {
        if (client.isBusy()){
            client.getTchatOut().error("Vous êtes occupé.");
        }
        else{
            ManageStoreTrade trade = new ManageStoreTrade(client);
            client.getActions().push(trade);

            trade.begin();
        }
    }

    private void parseStoreAddItemRequestMessage(long itemId, int quantity, long price) throws GameActionException {
        ManageStoreTrade trade = client.getActions().current();
        trade.add(itemId, quantity, price);
    }

    private void parseStoreRemoveItemRequestMessage(long itemId) throws GameActionException {
        ManageStoreTrade trade = client.getActions().current();
        trade.remove(itemId);
    }

    private void parseStoreUpdateItemRequestMessage(long itemId, long price) throws GameActionException {
        ManageStoreTrade trade = client.getActions().current();
        trade.update(itemId, price);
    }

    private void parseStoreActivationRequestMessage() throws Exception {
        if (client.getCharacter().getStore().empty()){
            client.getSession().write(InfoGameMessageFormatter.emptyStoreMessage());
        }
        else if (!client.getCharacter().getCurrentMap().availableStorePlaces()){
            client.getSession().write(InfoGameMessageFormatter.notEnoughStorePlacesMessage(
                    client.getCharacter().getCurrentMap().getMaxStores()
            ));
        }
        else{
            if (client.isBusy()){
                if (client.getActions().currentActionIs(GameActionType.STORE_MANAGEMENT)){
                    client.getActions().pop().end();
                }
                else{
                    throw new Exception("You are busy.");
                }
            }

            client.kick();
            client.getCharacter().getStore().setActive(true);
        }
    }

    private void parseTradeStoreRequestMessage(Character seller) throws Exception {
        if (seller == null){
            throw new Exception("Unknown seller");
        }
        else if (seller.getCurrentMap() != client.getCharacter().getCurrentMap()){
            throw new Exception("Not on same map");
        }
        else if (!seller.getStore().isActive()){
            throw new Exception("Not a seller");
        }
        else{
            StoreTrade trade = new StoreTrade(client, seller);
            client.getActions().push(trade);

            trade.begin();
        }
    }
}
