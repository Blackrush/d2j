package org.d2j.game.game.actions;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.BasicGameMessageFormatter;
import org.d2j.common.client.protocol.GameMessageFormatter;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.Cell;
import org.d2j.game.model.Character;
import org.d2j.game.model.Map;
import org.d2j.game.model.MapTrigger;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.Future;

/**
 * User: Blackrush
 * Date: 13/11/11
 * Time: 15:16
 * IDE : IntelliJ IDEA
 */
public class RolePlayMovement implements IGameAction {
    public static void teleport(GameClient client, Map map, short cellId, OrientationEnum orientation) throws GameActionException {
        client.getCharacter().getCurrentMap().deleteObserver(client.getHandler());
        client.getCharacter().getCurrentMap().removeActor(client.getCharacter());

        client.getCharacter().setCurrentMap(map);
        client.getCharacter().setCurrentCell(cellId);
        client.getCharacter().setCurrentOrientation(orientation);

        client.getCharacter().getCurrentMap().addActor(client.getCharacter());
        client.getCharacter().getCurrentMap().addObserver(client.getHandler());

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(GameMessageFormatter.changeMapMessage(client.getCharacter().getId()));
            buf.append(GameMessageFormatter.mapDataMessage(
                    map.getId(),
                    map.getDate(),
                    map.getKey()
            ));
        }
    }
    public static void teleport(GameClient client, Map map, short cellId) throws GameActionException {
        teleport(client, map, cellId, client.getCharacter().getCurrentOrientation());
    }


    private final GameClient client;
    private final String path;
    private final Future<RolePlayMovement> endFuture;

    public RolePlayMovement(String path, GameClient client) {
        this.path = path;
        this.client = client;
        this.endFuture = new Future<>(this);
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.MOVEMENT;
    }

    @Override
    public void begin() throws GameActionException {
        if (client.getCharacter().getBag().isFull()){
            client.getSession().write(BasicGameMessageFormatter.noOperationMessage());

            throw new GameActionException("Vous êtes trop chargé pour pouvoir vous déplacer.");
        }
        else{
            client.getCharacter().getCurrentMap().move(this);
        }
    }

    @Override
    public void end() throws GameActionException {
        OrientationEnum orientation = Cell.decode(path.charAt(path.length() - 3));
        short cellId = Cell.decode(path.substring(path.length() - 2));

        client.getCharacter().setCurrentOrientation(orientation);
        client.getCharacter().setCurrentCell(cellId);

        MapTrigger trigger = client.getCharacter().getCurrentMap().getTriggers().get(cellId);
        if (trigger != null) {
            teleport(client, trigger.getNextMap(), trigger.getNextCellId());
        }
        else{
            client.getSession().write(BasicGameMessageFormatter.noOperationMessage());
        }

        try {
            endFuture.notifyListeners();
        } catch (Exception e) {
            throw new GameActionException(e);
        }
    }

    @Override
    public void cancel() throws GameActionException {
        end();
    }

    public void cancel(short cellId) throws GameActionException {
        client.getCharacter().setCurrentCell(cellId);
    }

    public Future<RolePlayMovement> getEndFuture() {
        return endFuture;
    }

    public Character getActor(){
        return client.getCharacter();
    }

    public String getPath(){
        return "a" + Cell.encode(client.getCharacter().getCurrentCell()) + path;
    }
}
