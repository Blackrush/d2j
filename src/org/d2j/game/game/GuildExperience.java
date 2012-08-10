package org.d2j.game.game;

import org.d2j.game.model.ExperienceTemplate;
import org.d2j.game.repository.ExperienceTemplateRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 05/02/12
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 */
public class GuildExperience extends Experience {
    public GuildExperience(short level, long experience, IBaseEntityRepository<ExperienceTemplate, Short> repository) {
        super(level, experience, repository);
    }

    @Override
    protected long getExperience(ExperienceTemplate tpl) {
        return tpl.getGuild();
    }

    @Override
    protected ExperienceTemplate getByExperience(long exp) {
        for (ExperienceTemplate tpl : repository.all()){
            if (tpl.getGuild() >= exp){
                return tpl;
            }
        }
        return null;
    }

    @Override
    protected void onLevelUp(int diff) {
    }
}
