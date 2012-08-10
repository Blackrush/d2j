package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.CharacterExperience;
import org.d2j.game.game.WaypointList;
import org.d2j.game.game.items.PersistentBag;
import org.d2j.game.game.items.StoreBag;
import org.d2j.game.game.statistics.CharacterStatistics;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 02/11/11
 * Time: 11:16
 * IDE : IntelliJ IDEA
 */
@Singleton
public class CharacterRepository extends AbstractEntityRepository<Character, Long> {
    private long nextId;

    private final IGameConfiguration configuration;

    private final IBaseEntityRepository<BreedTemplate, Byte> breedTemplates;
    private final IBaseEntityRepository<ExperienceTemplate, Short> experienceTemplates;
    private final IEntityRepository<GameAccount, Integer> accounts;
    private final IBaseEntityRepository<Map, Integer> maps;
    private final IEntityRepository<Item, Long> items;
    private final IEntityRepository<Spell, Long> spells;
    private final IBaseEntityRepository<SpellBreed, Integer> spellBreeds;
    private final IBaseEntityRepository<Waypoint, Short> waypoints;
    private final IEntityRepository<WaypointRecord, Long> waypointRecords;
    private final IEntityRepository<StoredItem, Long> storedItems;

    @Inject
    public CharacterRepository(@Dynamic EntitiesContext context, IGameConfiguration configuration, IBaseEntityRepository<BreedTemplate, Byte> breedTemplates, IBaseEntityRepository<ExperienceTemplate, Short> experienceTemplates, IEntityRepository<GameAccount, Integer> accounts, IBaseEntityRepository<Map, Integer> maps, IEntityRepository<Item, Long> items, IEntityRepository<Spell, Long> spells, IBaseEntityRepository<SpellBreed, Integer> spellBreeds, IBaseEntityRepository<Waypoint, Short> waypoints, IEntityRepository<WaypointRecord, Long> waypointRecords, IEntityRepository<StoredItem, Long> storedItems) {
        super(context);
        this.configuration = configuration;
        this.breedTemplates = breedTemplates;
        this.experienceTemplates = experienceTemplates;
        this.accounts = accounts;
        this.maps = maps;
        this.items = items;
        this.spells = spells;
        this.spellBreeds = spellBreeds;
        this.waypoints = waypoints;
        this.waypointRecords = waypointRecords;
        this.storedItems = storedItems;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        if (!breedTemplates.isLoaded()) throw new LoadingException("BreedTemplateRepository must be loaded.");

        if (!experienceTemplates.isLoaded()) throw new LoadingException("ExperienceTemplateRepository must be loaded.");

        if (!maps.isLoaded()) throw new LoadingException("MapRepository must be loaded.");

        if (!spellBreeds.isLoaded()) throw new LoadingException("SpellBreedRepository must be loaded.");

        if (!accounts.isLoaded()) throw new LoadingException("AccountRepository must be loaded.");
    }

    @Override
    protected void afterLoading() {
        for (Character chr : entities.values()){
            if (nextId < chr.getId())
                nextId = chr.getId();
        }
    }

    public boolean nameExists(String name){
        for (Character c : entities.values())
            if (name.equalsIgnoreCase(c.getName()))
                return true;
        return false;
    }

    public Character findByName(String name) {
        for (Character c : entities.values()){
            if (name.equals(c.getName())){
                return c;
            }
        }
        return null;
    }

    public Character findByIdOrName(String raw){
        try{
            Long id = Long.parseLong(raw);
            return findById(id);
        }
        catch (NumberFormatException e){
            return findByName(raw);
        }
    }

    public Character createDefault(GameAccount owner, String name, Byte breed, boolean gender, int color1, int color2, int color3){
        Character character = new Character();

        character.setName(name);
        character.setOwner(owner);
        character.setBreed(breedTemplates.findById(breed));
        character.setGender(gender);

        character.setColor1(color1);
        character.setColor2(color2);
        character.setColor3(color3);
        character.setSkin((short) (breed * 10 + (gender ? 1 : 0)));
        character.setSize(configuration.getStartSize());

        character.setExperience(new CharacterExperience(
                configuration.getStartLevel(),
                experienceTemplates.findById(configuration.getStartLevel()).getCharacter(),
                experienceTemplates,
                configuration,
                character
        ));

        character.setStatistics(new CharacterStatistics(character));
        character.getStatistics().setLife(character.getStatistics().getMaxLife());
        character.getStatistics().get(CharacteristicType.ActionPoints).setBase((short) (character.getBreed().getStartAP() + (character.getExperience().getLevel() > 99 ? 1 : 0)));
        character.getStatistics().get(CharacteristicType.MovementPoints).setBase(character.getBreed().getStartMP());
        character.getStatistics().get(CharacteristicType.Prospection).setBase(character.getBreed().getStartProspection());
        character.getStatistics().get(CharacteristicType.Summons).setBase((short) 1);

        character.setEnergy(configuration.getStartEnergy());

        character.setStatsPoints((short) (configuration.getStartLevel() * configuration.getStatsPointsPerLevel() - configuration.getStatsPointsPerLevel()));
        character.setSpellsPoints((short) (configuration.getStartLevel() - configuration.getSpellsPointsPerLevel()));

        character.setCurrentMap(maps.findById(configuration.getStartMapId()));
        character.setCurrentCell(configuration.getStartCellId());
        character.setCurrentOrientation(OrientationEnum.SOUTH_EAST);
        character.setMemorizedMap(maps.findById(configuration.getStartMapId()));
        character.setMemorizedCell(configuration.getStartCellId());

        for (SpellBreed spell : spellBreeds.all()){
            if (spell.getBreed() == character.getBreed() &&
                spell.getLevel() <= character.getExperience().getLevel())
            {
                Spell s = spell.getSpell().newInstance();
                s.setCharacter(character);
                s.setLevel(configuration.getSpellStartLevel());
                s.setPosition(spell.getDefaultPosition());

                character.getSpells().put(s.getTemplate().getId(), s);
            }
        }

        character.setBag(new PersistentBag(character, items));
        character.getBag().setKamas(configuration.getStartKamas());

        character.setWaypoints(new WaypointList(character, waypointRecords));
        if (configuration.getAddAllWaypoints()){
            for (Waypoint waypoint : waypoints.all()){
                character.getWaypoints().add(waypoint);
            }
        }

        character.setStore(new StoreBag(character, storedItems));

        create(character);

        return character;
    }

    @Override
    public void create(Character entity) {
        super.create(entity);

        entity.getOwner().getCharacters().put(entity.getId(), entity);

        for (Spell spell : entity.getSpells().values()){
            spells.create(spell);
        }
    }

    @Override
    public void delete(Character entity) {
        entity.getOwner().getCharacters().remove(entity.getId());

        for (Spell spell : entity.getSpells().values()){
            spells.delete(spell);
        }

        for (Item item : entity.getBag().values()){
            items.delete(item);
        }

        for (WaypointRecord record : entity.getWaypoints()){
            waypointRecords.delete(record);
        }

        super.delete(entity);
    }

    @Override
    protected void setNextId(Character entity) {
        entity.setId(++nextId);
    }

    @Override
    protected String getCreateQuery(Character entity) {
        return StringUtils.format(
                "INSERT INTO `characters`(`id`,`ownerId`,`name`,`breed`,`gender`,`color1`,`color2`,`color3`," +
                        "`skin`,`size`,`level`,`experience`,`energy`,`kamas`,`statsPoints`,`spellsPoints`," +
                        "`vitality`,`wisdom`,`strength`,`intelligence`,`chance`,`agility`,`life`," +
                        "`currentMap`,`currentCell`,`currentOrientation`,`memorizedMap`,`memorizedCell`,`actionPoints`,`movementPoints`) " +
                        "VALUES('{0}','{1}','{2}','{3}','{4}','{5}','{6}','{7}','{8}','{9}','{10}','{11}','{12}','{13}','{14}','{15}'," +
                        "'{16}','{17}','{18}','{19}','{20}','{21}','{22}','{23}','{24}','{25}','{26}','{27}','{28}','{29}');",

                entity.getId(),
                entity.getOwner().getId(),
                entity.getName(),
                entity.getBreed().getId(),
                entity.getGender() ? "1" : "0",
                entity.getColor1(),
                entity.getColor2(),
                entity.getColor3(),
                entity.getSkin(),
                entity.getSize(),
                entity.getExperience().getLevel(),
                entity.getExperience().getExperience(),
                entity.getEnergy(),
                entity.getBag().getKamas(),
                entity.getStatsPoints(),
                entity.getSpellsPoints(),
                entity.getStatistics().get(CharacteristicType.Vitality).getBase(),
                entity.getStatistics().get(CharacteristicType.Wisdom).getBase(),
                entity.getStatistics().get(CharacteristicType.Strength).getBase(),
                entity.getStatistics().get(CharacteristicType.Intelligence).getBase(),
                entity.getStatistics().get(CharacteristicType.Chance).getBase(),
                entity.getStatistics().get(CharacteristicType.Agility).getBase(),
                entity.getStatistics().getLife(),
                entity.getCurrentMap().getId(),
                entity.getCurrentCell(),
                entity.getCurrentOrientation().ordinal(),
                entity.getMemorizedMap().getId(),
                entity.getMemorizedCell(),
                entity.getStatistics().get(CharacteristicType.ActionPoints).getBase(),
                entity.getStatistics().get(CharacteristicType.MovementPoints).getBase()
        );
    }

    @Override
    protected String getDeleteQuery(Character entity) {
        return StringUtils.format(
                "DELETE FROM `characters` WHERE `id`='{0}';",
                entity.getId()
        );
    }

    @Override
    protected String getSaveQuery(Character entity) {
        return StringUtils.format(
                "UPDATE `characters` SET " +
                        "`skin`='{0}', `size`='{1}', " +
                        "`level`='{2}', `experience`='{3}', " +
                        "`energy`='{4}', `kamas`='{5}', " +
                        "`statsPoints`='{6}', `spellsPoints`='{7}', " +
                        "`vitality`='{8}', `wisdom`='{9}', " +
                        "`strength`='{10}', `intelligence`='{11}', " +
                        "`chance`='{12}', `agility`='{13}', " +
                        "`currentMap`='{14}', `currentCell`='{15}', " +
                        "`currentOrientation`='{16}', " +
                        "`memorizedMap`='{17}', `memorizedCell`='{18}', " +
                        "`life`='{19}', `actionPoints`='{20}', `movementPoints`='{21}'" +
                        " WHERE `id`='{22}';",

                entity.getSkin(),
                entity.getSize(),
                entity.getExperience().getLevel(),
                entity.getExperience().getExperience(),
                entity.getEnergy(),
                entity.getBag().getKamas(),
                entity.getStatsPoints(),
                entity.getSpellsPoints(),
                entity.getStatistics().get(CharacteristicType.Vitality).getBase(),
                entity.getStatistics().get(CharacteristicType.Wisdom).getBase(),
                entity.getStatistics().get(CharacteristicType.Strength).getBase(),
                entity.getStatistics().get(CharacteristicType.Intelligence).getBase(),
                entity.getStatistics().get(CharacteristicType.Chance).getBase(),
                entity.getStatistics().get(CharacteristicType.Agility).getBase(),
                entity.getCurrentMap().getId(),
                entity.getCurrentCell(),
                entity.getCurrentOrientation().ordinal(),
                entity.getMemorizedMap().getId(),
                entity.getMemorizedCell(),
                entity.getStatistics().getLife(),
                entity.getStatistics().get(CharacteristicType.ActionPoints).getBase(),
                entity.getStatistics().get(CharacteristicType.MovementPoints).getBase(),

                entity.getId()
        );
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `characters`;";
    }

    @Override
    protected String getLoadOneQuery(Long id) {
        return "SELECT * FROM `characters` WHERE `id`='" + id + "';";
    }

    @Override
    protected Character loadOne(ResultSet reader) throws SQLException {
        Character chr = new Character(
                reader.getLong("id"),
                accounts.findById(reader.getInt("ownerId")),
                reader.getString("name"),
                breedTemplates.findById(reader.getByte("breed")),
                reader.getBoolean("gender"),
                reader.getInt("color1"),
                reader.getInt("color2"),
                reader.getInt("color3"),
                reader.getShort("skin"),
                reader.getShort("size"),
                reader.getShort("energy"),
                reader.getShort("statsPoints"),
                reader.getShort("spellsPoints"),
                maps.findById(reader.getInt("currentMap")),
                reader.getShort("currentCell"),
                OrientationEnum.SOUTH_EAST,
                maps.findById(reader.getInt("memorizedMap")),
                reader.getShort("memorizedCell")
        );
        chr.setExperience(new CharacterExperience(
                reader.getShort("level"),
                reader.getLong("experience"),
                experienceTemplates,
                configuration,
                chr
        ));
        chr.setStatistics(new CharacterStatistics(
                chr,
                reader.getShort("life"),
                reader.getShort("actionPoints"),
                reader.getShort("movementPoints"),
                reader.getShort("vitality"),
                reader.getShort("wisdom"),
                reader.getShort("strength"),
                reader.getShort("intelligence"),
                reader.getShort("chance"),
                reader.getShort("agility")
        ));
        chr.setBag(new PersistentBag(chr, items));
        chr.getBag().setKamas(reader.getLong("kamas"));
        chr.setWaypoints(new WaypointList(chr, waypointRecords));
        chr.setStore(new StoreBag(chr, storedItems));
        chr.getStore().setActive(reader.getBoolean("storeActive"));

        chr.getOwner().getCharacters().put(chr.getId(), chr);

        return chr;
    }
}
