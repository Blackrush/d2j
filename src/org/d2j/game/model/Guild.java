package org.d2j.game.model;

import org.d2j.common.GuildEmblem;
import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.common.client.protocol.type.BaseGuildMemberType;
import org.d2j.game.game.GuildExperience;
import org.d2j.game.game.events.MessageEvent;
import org.d2j.utils.Reference;
import org.d2j.utils.SimpleReference;
import org.d2j.utils.database.entity.IEntity;
import org.d2j.utils.database.repository.IEntityRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 03/02/12
 * Time: 23:13
 * To change this template use File | Settings | File Templates.
 */
public class Guild extends Observable implements IEntity<Integer> {
    public static final int TESTING = 0, LEADER = 1, SND_LEADER = 2;

    private int id;
    private String name;
    private GuildExperience experience;
    private GuildEmblem emblem;
    private Reference<GuildMember> leader;
    private HashMap<Long, GuildMember> members;
    private final IEntityRepository<GuildMember, Long> guildMembers;

    public Guild(String name, GuildExperience experience, GuildEmblem emblem, IEntityRepository<GuildMember, Long> guildMembers) {
        this.name = name;
        this.experience = experience;
        this.emblem = emblem;
        this.guildMembers = guildMembers;
        this.members = new HashMap<>();
        this.leader = new SimpleReference<>();
    }

    public Guild(int id, String name, GuildExperience experience, GuildEmblem emblem, Reference<GuildMember> leader, IEntityRepository<GuildMember, Long> guildMembers) {
        this.id = id;
        this.name = name;
        this.experience = experience;
        this.emblem = emblem;
        this.guildMembers = guildMembers;
        this.members = new HashMap<>();
        this.leader = leader;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GuildExperience getExperience() {
        return experience;
    }

    public GuildEmblem getEmblem() {
        return emblem;
    }

    public void setEmblem(GuildEmblem emblem) {
        this.emblem = emblem;
    }

    public GuildMember getLeader() {
        return leader.get();
    }

    public void setLeader(GuildMember target) {
        if (!leader.isNull()){
            leader.get().setRank(SND_LEADER);
            leader.get().getRights().fill(true);
        }

        target.setRank(LEADER);
        target.getRights().fill(true);
        leader.set(target);
    }

    public GuildMember getMember(Long characterId){
        return members.get(characterId);
    }

    public GuildMember getMember(String characterName){
        for (GuildMember member : members.values()){
            if (characterName.equals(member.getMember().getName())){
                return member;
            }
        }
        return null;
    }

    public GuildMember addMember(Character character){
        GuildMember member = new GuildMember(this, character, TESTING);
        members.put(character.getId(), member);

        return member;
    }

    public void addMember(GuildMember member) {
        members.put(member.getMember().getId(), member);
    }

    public boolean removeMember(GuildMember member){
        if (member != null && members.containsKey(member.getMember().getId())){
            Character character = member.getMember();
            character.setGuildData(null);

            members.remove(character.getId());
            guildMembers.delete(member);

            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeMember(Long characterId){
        GuildMember member = members.get(characterId);

        if (member != null){
            Character character = member.getMember();
            character.setGuildData(null);

            members.remove(characterId);

            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeMember(String characterName){
        for (GuildMember member : members.values()){
            if (characterName.equals(member.getMember().getName())){
                return removeMember(member.getMember().getId());
            }
        }
        return false;
    }

    public Collection<GuildMember> getMembers(){
        return members.values();
    }

    public boolean isValid(){
        return members.size() >= 10;
    }

    public void speak(Character character, String message){
        if (members.containsKey(character.getId())){
            setChanged();
            notifyObservers(new MessageEvent(character.getId(), character.getName(), ChannelEnum.Guild, message));
        }
    }

    public Collection<BaseGuildMemberType> toBaseGuildMemberType(){
        return GuildMember.toBaseGuildMemberType(members.values());
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }
}
