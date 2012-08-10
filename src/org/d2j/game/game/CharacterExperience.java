package org.d2j.game.game;

import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.events.LevelUpEvent;
import org.d2j.game.model.Character;
import org.d2j.game.model.ExperienceTemplate;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 09:42
 * IDE : IntelliJ IDEA
 */
public class CharacterExperience extends Experience {
    private final IGameConfiguration configuration;
    private final Character character;

    public CharacterExperience(short level, long experience, IBaseEntityRepository<ExperienceTemplate, Short> repository, IGameConfiguration configuration, Character character) {
        super(level, experience, repository);
        this.configuration = configuration;
        this.character = character;
    }

    @Override
    protected long getExperience(ExperienceTemplate tpl) {
        return tpl.getCharacter();
    }

    @Override
    protected ExperienceTemplate getByExperience(long exp) {
        for (ExperienceTemplate tpl : repository.all()){
            if (tpl.getCharacter() >= exp){
                return tpl;
            }
        }
        return null;
    }

    @Override
    protected void onLevelUp(int diff) {
        character.getStatistics().setMaxLife();
        character.addStatsPoints((short) (configuration.getStatsPointsPerLevel() * diff));
        character.addSpellsPoints((short) (configuration.getSpellsPointsPerLevel() * diff));

        LevelUpEvent event = new LevelUpEvent(character);
        character.notifyObservers(event);
    }
}
