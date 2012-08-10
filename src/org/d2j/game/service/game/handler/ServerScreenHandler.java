package org.d2j.game.service.game.handler;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.ApproachGameMessageFormatter;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.game.GameClientHandler;
import org.d2j.game.service.game.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 09:56
 * IDE : IntelliJ IDEA
 */
public class ServerScreenHandler extends GameClientHandler {
    public static final long SUBSCRIPTION_DURATION = 31536000000000L;
    private static final Logger logger = LoggerFactory.getLogger(ServerScreenHandler.class);

    private boolean authenticated;

    public ServerScreenHandler(GameService service, GameClient client) {
        super(service, client);
    }

    @Override
    public void parse(String packet) throws Exception {
        if (packet.charAt(0) != 'A') // we want A... we have *...
            throw new Exception("bad data received !");

        if (authenticated){
            String[] args;
            switch (packet.charAt(1)){
                case 'T': // we want A*... we have AT...
                    throw new Exception("Client already authenticated !");

                case 'V':
                    parseRegionalVersionRequestMessage();
                    break;

                case 'L':
                    parseCharactersListRequestMessage();
                    break;

                case 'P':
                    parseCharacterNameSuggestionRequestMessage();
                    break;

                case 'A':
                    args = packet.substring(2).split("\\|");
                    if (args.length != 6)
                        throw new Exception("Bad data received.");

                    parseCharacterCreationRequestMessage(
                            args[0],
                            Byte.parseByte(args[1]),
                            args[2].equals("1"),
                            Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]),
                            Integer.parseInt(args[5])
                    );
                    break;

                case 'D':
                    args = packet.substring(2).split("\\|");
                    if (args.length != 1 && args.length != 2)
                        throw new Exception("Bad data received.");

                    parseCharacterDeletionRequestMessage(
                            Long.parseLong(args[0]),
                            args.length > 1 ? args[1] : ""
                    );
                    break;

                case 'S':
                    parseCharacterSelectionRequestMessage(Long.parseLong(packet.substring(2)));
                    break;
            }
        }
        else{
            if (packet.charAt(1) != 'T') // we want AT... we have A*...
                throw new Exception("Client must be authenticated !");

            parseAuthenticationRequestMessage(packet.substring(2));
        }
    }

    @Override
    public void onClosed() {
        if (authenticated){
            service.getWorld().getLoginServerManager().setAccountDeconnected(client.getAccount().getId());
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    private String getCharactersListMessage(){
        return ApproachGameMessageFormatter.charactersListMessage(
                service.getConfiguration().getServerId(),
                SUBSCRIPTION_DURATION,
                org.d2j.game.model.Character.toBaseCharacterType(client.getAccount().getCharacters().values())
        );
    }

    private void parseAuthenticationRequestMessage(String ticket) throws Exception {
        GameAccount account = service.getWorld().getRepositoryManager().getAccounts().findByTicket(ticket);
        if (account == null){
            client.getSession().write(ApproachGameMessageFormatter.authenticationFailureMessage());
            client.getSession().close(false);
        }
        else{
            client.setAccount(account);
            client.getSession().write(ApproachGameMessageFormatter.
                    authenticationSuccessMessage(account.getCommunity()));

            authenticated = true;
        }
    }

    private void parseRegionalVersionRequestMessage() throws Exception {
        client.getSession().write(ApproachGameMessageFormatter.
                regionalVersionResponseMessage(client.getAccount().getCommunity()));
    }

    private void parseCharactersListRequestMessage() throws Exception {
        client.getSession().write(getCharactersListMessage());
    }

    private void parseCharacterNameSuggestionRequestMessage() throws Exception {
        if (service.getConfiguration().getCharacterNameSuggestionEnabled()){
            client.getSession().write(ApproachGameMessageFormatter.
                    characterNameSuggestionSuccessMessage(StringUtils.randomPseudo()));
        }
        else{
            client.getSession().write(ApproachGameMessageFormatter.characterNameSuggestionFailureMessage());
        }
    }

    private void parseCharacterCreationRequestMessage(String name, Byte breed, boolean gender, int color1, int color2, int color3) throws Exception {
        if (client.getAccount().getCharacters().size() >= service.getConfiguration().getMaxCharactersPerAccount()){
            client.getSession().write(ApproachGameMessageFormatter.accountFullMessage());
        }
        else if (service.getWorld().getRepositoryManager().getCharacters().nameExists(name)){
            client.getSession().write(ApproachGameMessageFormatter.characterNameAlreadyExistsMessage());
        }
        else{
            Character character = service.getWorld().getRepositoryManager().getCharacters().createDefault(
                    client.getAccount(),
                    name,
                    breed,
                    gender,
                    color1,
                    color2,
                    color3
            );

            service.getWorld().getLoginServerManager().refreshCharactersList(
                    client.getAccount().getId(),
                    client.getAccount().getCharacters().size()
            )
            .addListener(new IoFutureListener<WriteFuture>() {
                @Override
                public void operationComplete(WriteFuture o) {
                    try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())) {
                        buf.append(ApproachGameMessageFormatter.characterCreationSuccessMessage());
                        buf.append(getCharactersListMessage());
                    }
                }
            });
        }
    }

    private void parseCharacterDeletionRequestMessage(Long characterId, String secretAnswer){
        Character chr = client.getAccount().getCharacters().get(characterId);

        if (chr == null){
            client.getSession().write(ApproachGameMessageFormatter.characterDeletionFailureMessage());
        }
        else if (chr.getExperience().getLevel() > service.getConfiguration().getDeletionAnswerRequiredLevel() &&
                 !client.getAccount().getAnswer().equalsIgnoreCase(secretAnswer))
        {
            client.getSession().write(ApproachGameMessageFormatter.characterDeletionFailureMessage());
        }
        else{
            service.getWorld().getRepositoryManager().getCharacters().delete(chr);

            service.getWorld().getLoginServerManager().refreshCharactersList(
                    client.getAccount().getId(),
                    client.getAccount().getCharacters().size()
            )
            .addListener(new IoFutureListener<WriteFuture>() {
                @Override
                public void operationComplete(WriteFuture o) {
                    client.getSession().write(ApproachGameMessageFormatter.charactersListMessage(
                            service.getConfiguration().getServerId(),
                            31536000,
                            org.d2j.game.model.Character.toBaseCharacterType(client.getAccount().getCharacters().values())
                    ));
                }
            });
        }
    }

    private void parseCharacterSelectionRequestMessage(Long characterId){
        Character chr = client.getAccount().getCharacters().get(characterId);

        if (chr == null) {
            client.getSession().write(ApproachGameMessageFormatter.characterSelectionFailureMessage());
            client.getSession().close(false);
        }
        else{
            client.setCharacter(chr);
            client.getAccount().setOnline(client);

            client.getSession().write(ApproachGameMessageFormatter.characterSelectionSucessMessage(
                    chr.getId(),
                    chr.getName(),
                    chr.getExperience().getLevel(),
                    chr.getBreed().getId(),
                    chr.getGender(),
                    chr.getSkin(),
                    chr.getColor1(),
                    chr.getColor2(),
                    chr.getColor3(),
                    chr.getBag().toBaseItemType()
            ));

            client.setHandler(new RolePlayHandler(service, client));
        }
    }
}
