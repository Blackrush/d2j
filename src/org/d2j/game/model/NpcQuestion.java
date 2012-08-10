package org.d2j.game.model;

import org.d2j.utils.database.entity.IBaseEntity;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class NpcQuestion implements IBaseEntity<Integer> {
    public static final String BANK_COST_VAR_NAME = "bankCost";
    public static final String CHARACTER_NAME_VAR_NAME = "name";

    private int id;
    private HashMap<Integer, NpcResponse> responses;
    private String[] parameters;

    public NpcQuestion(int id, HashMap<Integer, NpcResponse> responses, String[] parameters) {
        this.id = id;
        this.responses = responses;
        this.parameters = parameters;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public HashMap<Integer, NpcResponse> getResponses() {
        return responses;
    }

    public String[] getParameters() {
        return parameters;
    }

    public String[] parseParameters(Character character){
        String[] result = new String[parameters.length];
        for (int i = 0; i < parameters.length; ++i){
            if (parameters[i].equalsIgnoreCase(BANK_COST_VAR_NAME)){
                result[i] = "0";
            }
            else if (parameters[i].equalsIgnoreCase(CHARACTER_NAME_VAR_NAME)){
                result[i] = character.getName();
            }
        }
        return result;
    }
}
