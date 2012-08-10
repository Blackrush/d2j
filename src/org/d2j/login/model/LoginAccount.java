package org.d2j.login.model;

import org.d2j.common.Permissions;
import org.d2j.utils.database.entity.ISaveableEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 11:53
 * IDE : IntelliJ IDEA
 */
public class LoginAccount implements ISaveableEntity<Integer> {
    private Integer id;
    private String name;
    private String password;
    private String nickname;
    private String question;
    private String answer;
    private Permissions rights;
    private int community;
    private Map<Integer, Integer> characters = new HashMap<>();
    private boolean connected;

    private final Object lock = new Object();

    public LoginAccount() {

    }

    public LoginAccount(Integer id, String name, String password, String nickname, String question, String answer, Permissions rights, int community) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.question = question;
        this.answer = answer;
        this.rights = rights;
        this.community = community;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public Map<Integer, Integer> getCharacters() {
        return characters;
    }

    public void setCharacters(Map<Integer, Integer> characters) {
        this.characters = characters;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Object getLock() {
        return lock;
    }

    public void beforeSave() {

    }

    public void onSaved() {

    }
}
