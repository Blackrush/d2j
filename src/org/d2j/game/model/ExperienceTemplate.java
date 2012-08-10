package org.d2j.game.model;

import org.d2j.utils.database.entity.IBaseEntity;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 09:35
 * IDE : IntelliJ IDEA
 */
public class ExperienceTemplate implements IBaseEntity<Short> {
    private short level;
    private long character;
    private int job;
    private int mount;
    private short alignment;
    private long guild;

    public ExperienceTemplate() {

    }

    public ExperienceTemplate(short level, long character, int job, int mount, short alignment) {
        this.level = level;
        this.character = character;
        this.job = job;
        this.mount = mount;
        this.alignment = alignment;
        this.guild = this.character * 10;
    }

    @Override
    public Short getId() {
        return level;
    }

    /**
     * same as getId()
     * @return Short
     */
    public Short getLevel(){
        return level;
    }

    public long getCharacter() {
        return character;
    }

    public int getJob() {
        return job;
    }

    public int getMount() {
        return mount;
    }

    public short getAlignment() {
        return alignment;
    }

    public long getGuild() {
        return guild;
    }

    public ExperienceTemplate next(IBaseEntityRepository<ExperienceTemplate, Short> repo){
        return repo.findById((short) (level + 1));
    }

    public ExperienceTemplate previous(IBaseEntityRepository<ExperienceTemplate, Short> repo){
        return repo.findById((short)(level - 1));
    }
}
