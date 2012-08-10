package org.d2j.game.repository;

import org.d2j.game.model.NpcQuestion;
import org.d2j.game.model.NpcResponse;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class NpcQuestionRepository extends AbstractBaseEntityRepository<NpcQuestion, Integer> {
    private final IBaseEntityRepository<NpcResponse, Integer> responses;

    @Inject
    protected NpcQuestionRepository(@Static EntitiesContext context, IBaseEntityRepository<NpcResponse, Integer> responses) {
        super(context);
        this.responses = responses;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!responses.isLoaded()){
            throw new LoadingException("NpcResponseRepository isn't loaded.");
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `npc_questions`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `npc_questions` WHERE `id`='" + id + "';";
    }

    @Override
    protected NpcQuestion loadOne(ResultSet reader) throws SQLException {
        HashMap<Integer, NpcResponse> responses = new HashMap<>();
        for (String responseIdStr : reader.getString("responses").split(";")){
            int responseId = Integer.parseInt(responseIdStr);
            responses.put(responseId, this.responses.findById(responseId));
        }

        return new NpcQuestion(
                reader.getInt("id"),
                responses,
                reader.getString("arguments").split(",")
        );
    }
}
