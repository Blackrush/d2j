package org.d2j.game.model;

import org.d2j.common.Permissions;
import org.d2j.game.game.channels.ChannelList;
import org.d2j.game.game.events.FriendConnectionEvent;
import org.d2j.game.game.events.IEvent;
import org.d2j.game.game.friends.FriendList;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.database.entity.IEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Observable;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 14:45
 * IDE : IntelliJ IDEA
 */
public class GameAccount extends Observable implements IEntity<Integer> {
    private Integer id;
    private String nickname;
    private String answer;
    private Permissions rights;
    private int community;
    private ChannelList enabledChannels;
    private Date lastConnection;
    private String lastAddress;
    private boolean muted;
    private boolean notifyFriendsOnConnect;
    private HashMap<Long, Character> characters = new HashMap<>(6);
    private FriendList friends;

    private GameClient client;

    public GameAccount(Integer id, String nickname, String answer, Permissions rights, int community, ChannelList enabledChannels, Date lastConnection, String lastAddress, boolean muted, boolean notifyFriendsOnConnect) {
        this.id = id;
        this.nickname = nickname;
        this.answer = answer;
        this.rights = rights;
        this.community = community;
        this.enabledChannels = enabledChannels;
        this.lastConnection = lastConnection;
        this.lastAddress = lastAddress;
        this.muted = muted;
        this.notifyFriendsOnConnect = notifyFriendsOnConnect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSubscriber(){
        return true; //todo
    }

    public void setSubscriber(boolean subscriber){
        //todo
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Permissions getRights() {
        return rights;
    }

    public void setRights(Permissions rights) {
        this.rights = rights;
    }

    public boolean hasRights(){
        return rights.superior(Permissions.MEMBER);
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public ChannelList getEnabledChannels() {
        return enabledChannels;
    }

    public void setEnabledChannels(ChannelList enabledChannels) {
        this.enabledChannels = enabledChannels;
    }

    public Date getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isNotifyFriendsOnConnect() {
        return notifyFriendsOnConnect;
    }

    public void setNotifyFriendsOnConnect(boolean notifyFriendsOnConnect) {
        this.notifyFriendsOnConnect = notifyFriendsOnConnect;
    }

    public HashMap<Long, Character> getCharacters() {
        return characters;
    }

    public FriendList getFriends() {
        return friends;
    }

    public void setFriends(FriendList friends) {
        this.friends = friends;
    }

    public GameClient getClient() {
        return client;
    }

    public boolean isOnline(){
        return client != null;
    }

    public void setOnline(GameClient client) {
        this.client = client;
        if (client != null){
            notifyObservers(new FriendConnectionEvent(this));
        }
    }

    public void notifyObservers(IEvent event){
        setChanged();
        super.notifyObservers(event);
    }

    @Override
    public void beforeSave() {

    }

    @Override
    public void onSaved() {

    }
}
