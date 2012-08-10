package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.database.entity.IEntity;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/02/12
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
public class LearnSpellCommand extends AbstractCommand {
    private final CharacterRepository characters;
    private final IBaseEntityRepository<SpellTemplate, Integer> spellTemplates;
    private final IEntityRepository<Spell, Long> spells;

    public LearnSpellCommand(CharacterRepository characters, IBaseEntityRepository<SpellTemplate, Integer> spellTemplates, IEntityRepository<Spell, Long> spells) {
        this.characters = characters;
        this.spellTemplates = spellTemplates;
        this.spells = spells;
    }

    @Override
    public String helpMessage() {
        return "Learn a spell to a player.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID of SpellTemplate", "ID or NAME of Character:facultative"};
    }

    @Override
    public String name() {
        return "learn_spell";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1 || args.length > 2){
            out.error("Usage: " + usage());
        }
        else{
            SpellTemplate tpl = spellTemplates.findById(Integer.parseInt(args[0]));
            Character target = args.length > 1 ? characters.findByIdOrName(args[1]) : client.getCharacter();

            if (tpl == null){
                out.error("Unknown spell {0}.", args[0]);
            }
            else if (target == null){
                out.error("Unknown player {0}.", args[1]);
            }
            else{
                Spell spell = tpl.newInstance();
                spell.setCharacter(target);
                spell.setLevel((short) 1);
                spell.setPosition((byte) -1);

                target.getSpells().put(tpl.getId(), spell);

                out.info("{0} has successfully learned {1}.", tpl.getName());
            }
        }
    }
}

