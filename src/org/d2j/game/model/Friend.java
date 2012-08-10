package org.d2j.game.model;

import org.d2j.common.NumUtils;
import org.d2j.common.client.protocol.type.BaseFriendType;
import org.d2j.game.game.friends.FriendType;
import org.d2j.utils.database.entity.IEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: blackrush
 * Date: 31/12/11
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
public class Friend implements IEntity<Long> {
    public static boolean reciprocal(GameAccount a1, GameAccount a2){
        return a1.getFriends().isFriendWith(a2.getId()) && a2.getFriends().isFriendWith(a1.getId());
    }

    public static Collection<BaseFriendType> toBaseFriendType(Collection<Friend> friends){
        List<BaseFriendType> result = new ArrayList<>(friends.size());
        for (Friend friend : friends){
            result.add(friend.toBaseFriendType());
        }
        return result;
    }

    private long id;
    private GameAccount owner, target;
    private FriendType type;

    public Friend(GameAccount owner, GameAccount target, FriendType type) {
        this.owner = owner;
        this.target = target;
        this.type = type;
    }

    public Friend(long id, GameAccount owner, GameAccount target, FriendType type) {
        this.id = id;
        this.owner = owner;
        this.target = target;
        this.type = type;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public GameAccount getOwner() {
        return owner;
    }

    public void setOwner(GameAccount owner) {
        this.owner = owner;
    }

    public GameAccount getTarget() {
        return target;
    }

    public void setTarget(GameAccount target) {
        this.target = target;
    }

    public FriendType getType() {
        return type;
    }

    public void setType(FriendType type) {
        this.type = type;
    }

    public BaseFriendType toBaseFriendType(){
        if (target.isOnline()){
            return new BaseFriendType(
                    target.getNickname(),
                    true,
                    reciprocal(owner, target),
                    target.getClient().getCharacter().getName(),
                    target.getClient().getCharacter().getExperience().getLevel(),
                    NumUtils.SHORT_ZERO,
                    target.getClient().getCharacter().getBreed().getId(),
                    target.getClient().getCharacter().getGender(),
                    target.getClient().getCharacter().getSkin()
            );
        }
        else{
            return new BaseFriendType(
                    target.getNickname(),
                    false,
                    reciprocal(owner, target)
            );
        }
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }
}
