package org.d2j.game.game;

import org.d2j.game.model.ExperienceTemplate;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 05/02/12
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class Experience {
    protected final IBaseEntityRepository<ExperienceTemplate, Short> repository;

    private short level;
    private long experience;
    private ExperienceTemplate current, next;

    public Experience(short level, long experience, IBaseEntityRepository<ExperienceTemplate, Short> repository) {
        this.level = level;
        this.experience = experience;
        this.repository = repository;
        this.current = this.repository.findById(this.level);
        this.next = this.current.next(this.repository);
    }

    protected abstract long getExperience(ExperienceTemplate tpl);
    protected abstract ExperienceTemplate getByExperience(long exp);

    protected abstract void onLevelUp(int diff);

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        int diff = this.level - level;
        if (diff == 0) return;

        this.level = level;
        this.current = repository.findById(this.level);
        this.next = current.next(repository);
        this.experience = getExperience(current);

        if (diff > 0){
            onLevelUp(diff);
        }
    }

    public Experience addLevel(short level){
        setLevel((short) (this.level + level));
        return this;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
        this.current = getByExperience(this.experience);
        this.next = current.next(repository);

        int diff = this.level - current.getLevel();

        this.level = current.getLevel();

        if (diff > 0){
            onLevelUp(diff);
        }
    }

    public Experience addExperience(long experience){
        setExperience(this.experience + experience);
        return this;
    }

    public long min(){
        return getExperience(current);
    }

    public long max(){
        return getExperience(next);
    }
}
