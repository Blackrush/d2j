package org.d2j.game.game.friends;

import org.d2j.common.client.protocol.type.BaseFriendType;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.repository.IEntityRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: blackrush
 * Date: 31/12/11
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class FriendList {
    private final GameAccount owner;
    private final IEntityRepository<Friend, Long> repository;
    private final IGameConfiguration configuration;

    private HashMap<Integer, Friend> friends;
    private HashMap<Integer, Friend> ennemies;

    public FriendList(IEntityRepository<Friend, Long> repository, GameAccount owner, IGameConfiguration configuration) {
        this.repository = repository;
        this.owner = owner;
        this.configuration = configuration;
        this.friends = new HashMap<>(this.configuration.getMaxFriends());
        this.ennemies = new HashMap<>(this.configuration.getMaxEnnemies());
    }

    public void addData(Friend data){
        switch (data.getType()){
            case FRIEND:
                friends.put(data.getTarget().getId(), data);
                break;

            case ENNEMY:
                ennemies.put(data.getTarget().getId(), data);
                break;
        }
    }

    public void addFriend(GameAccount friend, Observer observer) throws FullListException {
        if (friends.size() >= configuration.getMaxFriends()){
            throw new FullListException();
        }

        Friend data = new Friend(owner, friend, FriendType.FRIEND);
        repository.create(data);

        friend.addObserver(observer);

        friends.put(friend.getId(), data);
    }

    public boolean removeFriend(Integer friendId, Observer observer){
        Friend data = friends.get(friendId);
        if (data != null){
            data.getTarget().deleteObserver(observer);
            repository.delete(data);
            friends.remove(friendId);

            return true;
        }
        return false;
    }

    public GameAccount getFriend(Integer friendId){
        Friend data = friends.get(friendId);
        return data != null ? data.getTarget() : null;
    }

    public boolean isFriendWith(Integer targetId){
        return friends.containsKey(targetId);
    }

    public boolean isFriendWith(GameAccount account){
        return isFriendWith(account.getId());
    }

    public boolean isFriendWith(Character character){
        return isFriendWith(character.getOwner());
    }

    public void subscribe(Observer observer){
        for (Friend data : friends.values()){
            data.getTarget().addObserver(observer);
        }
    }

    public void unsubcribe(Observer observer){
        for (Friend data : friends.values()){
            data.getTarget().deleteObserver(observer);
        }
    }

    public void addEnnemy(GameAccount ennemy) throws FullListException {
        if (ennemies.size() >= configuration.getMaxEnnemies()){
            throw new FullListException();
        }

        Friend data = new Friend(owner, ennemy, FriendType.ENNEMY);
        repository.create(data);

        ennemies.put(ennemy.getId(), data);
    }

    public boolean removeEnnemy(Integer ennemyId){
        Friend data = ennemies.get(ennemyId);
        if (data != null){
            repository.delete(data);
            ennemies.remove(ennemyId);

            return true;
        }
        return false;
    }

    public GameAccount getEnnemy(Integer ennemyId){
        Friend data = ennemies.get(ennemyId);
        return data != null ? data.getTarget() : null;
    }

    public boolean isEnnemyWith(Integer targetId){
        return ennemies.containsKey(targetId);
    }

    public boolean isEnnemyWith(GameAccount account){
        return isEnnemyWith(account.getId());
    }

    public boolean isEnnemyWith(Character character){
        return isEnnemyWith(character.getOwner());
    }

    public Collection<BaseFriendType> toBaseFriendType(){
        return Friend.toBaseFriendType(friends.values());
    }

    public BaseFriendType toBaseFriendType(Integer friendId){
        Friend data = friends.get(friendId);
        if (data != null){
            return data.toBaseFriendType();
        }
        return null;
    }
}
