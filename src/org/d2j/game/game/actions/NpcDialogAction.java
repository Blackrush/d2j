package org.d2j.game.game.actions;

import org.d2j.common.CollectionUtils;
import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.DialogGameMessageFormatter;
import org.d2j.game.model.Npc;
import org.d2j.game.model.NpcQuestion;
import org.d2j.game.model.NpcResponse;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.Selector;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
public class NpcDialogAction implements IGameAction {
    private final GameClient client;
    private final Npc npc;
    private NpcQuestion question;

    public NpcDialogAction(GameClient client, Npc npc) {
        this.client = client;
        this.npc = npc;
        this.question = npc.getTemplate().getQuestion();
    }

    public NpcQuestion getQuestion() {
        return question;
    }

    public void setQuestion(NpcQuestion question) {
        this.question = question;

        Collection<Integer> responses = CollectionUtils.select(question.getResponses().values(), new Selector<NpcResponse, Integer>(){
            @Override
            public Integer select(NpcResponse o){
                return o.getId();
            }
        });
        client.getSession().write(DialogGameMessageFormatter.questionMessage(
                question.getId(),
                question.parseParameters(client.getCharacter()),
                responses
        ));
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.NPC_DIALOG;
    }

    @Override
    public void begin() throws GameActionException {
        Collection<Integer> responses = CollectionUtils.select(question.getResponses().values(), new Selector<NpcResponse, Integer>(){
            @Override
            public Integer select(NpcResponse o){
                return o != null ? o.getId() : null;
            }
        });

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(DialogGameMessageFormatter.dialogSuccessMessage(npc.getTemplate().getId()));
            buf.append(DialogGameMessageFormatter.questionMessage(
                    question.getId(),
                    question.parseParameters(client.getCharacter()),
                    responses
            ));
        }
    }

    @Override
    public void end() throws GameActionException {
        client.getSession().write(DialogGameMessageFormatter.dialogEndMessage());
    }

    @Override
    public void cancel() throws GameActionException {
        end();
    }

    public void reply(int responseId) throws GameActionException {
        NpcResponse response = question.getResponses().get(responseId);
        if (response == null){
            throw new GameActionException("Unknown response #" + responseId + ".");
        }
        else if (response.getAction() == null){
            end();
        }
        else{
            question = null;

            response.getAction().apply(client);

            for (NpcResponse next = response.getNext(); next != null; next = next.getNext()){
                next.getAction().apply(client);
            }

            if (question == null){
                end();
            }
        }
    }
}
