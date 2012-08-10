package org.d2j.game;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.*;
import org.d2j.game.service.IWorld;
import org.d2j.utils.PeriodicAction;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.script.ScriptEngine;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 13:19
 * IDE : IntelliJ IDEA
 */
@Singleton
public class RepositoryManager extends PeriodicAction implements IRepositoryManager {
    private class RepositoryManagerModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(IGameConfiguration.class).toInstance(configuration);
            bind(ScriptEngine.class).toInstance(configuration.getScriptEngine());

            bind(EntitiesContext.class).annotatedWith(Dynamic.class).toInstance(dynamicContext);
            bind(EntitiesContext.class).annotatedWith(Static.class).toInstance(staticContext);

            bind(new TypeLiteral<IEntityRepository<GameAccount, Integer>>(){}).to(AccountRepository.class);
            bind(new TypeLiteral<IEntityRepository<Character, Long>>(){}).to(CharacterRepository.class);
            bind(new TypeLiteral<IEntityRepository<Spell, Long>>(){}).to(SpellRepository.class);
            bind(new TypeLiteral<IEntityRepository<Item, Long>>(){}).to(ItemRepository.class);
            bind(new TypeLiteral<IEntityRepository<Friend, Long>>(){}).to(FriendRepository.class);
            bind(new TypeLiteral<IEntityRepository<Npc, Integer>>(){}).to(NpcRepository.class);
            bind(new TypeLiteral<IEntityRepository<NpcSell, Integer>>(){}).to(NpcSellRepository.class);
            bind(new TypeLiteral<IEntityRepository<Guild, Integer>>(){}).to(GuildRepository.class);
            bind(new TypeLiteral<IEntityRepository<GuildMember, Long>>(){}).to(GuildMemberRepository.class);
            bind(new TypeLiteral<IEntityRepository<WaypointRecord, Long>>(){}).to(WaypointRecordRepository.class);
            bind(new TypeLiteral<IEntityRepository<StoredItem, Long>>(){}).to(StoredItemRepository.class);

            bind(new TypeLiteral<IBaseEntityRepository<BreedTemplate, Byte>>(){}).to(BreedTemplateRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<ExperienceTemplate, Short>>(){}).to(ExperienceTemplateRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<Map, Integer>>(){}).to(MapRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<MapTrigger, Integer>>(){}).to(MapTriggerRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<SpellTemplate, Integer>>(){}).to(SpellTemplateRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<SpellBreed, Integer>>(){}).to(SpellBreedRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<ItemTemplate, Integer>>(){}).to(ItemTemplateRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<ItemSetTemplate, Short>>(){}).to(ItemSetTemplateRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<NpcTemplate, Integer>>(){}).to(NpcTemplateRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<NpcQuestion, Integer>>(){}).to(NpcQuestionRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<NpcResponse, Integer>>(){}).to(NpcResponseRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<Waypoint, Short>>(){}).to(WaypointRepository.class);
            bind(new TypeLiteral<IBaseEntityRepository<MarketPlace, Short>>(){}).to(MarketPlaceRepository.class);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(RepositoryManager.class);

    private final IGameConfiguration configuration;
    private final IWorld world;

    private final EntitiesContext dynamicContext;
    private final EntitiesContext staticContext;
    private boolean started;
    private final ExecutorService executor;

    private final AccountRepository accounts;
    private final CharacterRepository characters;
    private final SpellRepository spells;
    private final ItemRepository items;
    private final FriendRepository friends;
    private final NpcRepository npcs;
    private final NpcSellRepository npcSells;
    private final GuildRepository guilds;
    private final GuildMemberRepository guildMembers;
    private final WaypointRecordRepository waypointRecords;
    private final StoredItemRepository storedItems;

    private final BreedTemplateRepository breedTemplates;
    private final ExperienceTemplateRepository experienceTemplates;
    private final MapRepository maps;
    private final MapTriggerRepository mapTriggers;
    private final SpellTemplateRepository spellTemplates;
    private final SpellBreedRepository spellBreeds;
    private final ItemTemplateRepository itemTemplates;
    private final ItemSetTemplateRepository itemSetTemplates;
    private final NpcTemplateRepository npcTemplates;
    private final NpcQuestionRepository npcQuestions;
    private final NpcResponseRepository npcResponses;
    private final WaypointRepository waypoints;
    private final MarketPlaceRepository marketPlaces;

    @Inject
    public RepositoryManager(IGameConfiguration configuration, IWorld world) throws ClassNotFoundException {
        super(configuration.getSaveDatabaseInterval());

        this.configuration = configuration;
        this.world = world;

        dynamicContext = new EntitiesContext(
                this.configuration.getDynamicConnectionInformations(),
                this.configuration.getExecutionInterval()
        );
        staticContext = new EntitiesContext(
                this.configuration.getStaticConnectionInformations(),
                -1
        );

        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Injector injector = Guice.createInjector(new RepositoryManagerModule());

        accounts = injector.getInstance(AccountRepository.class);
        characters = injector.getInstance(CharacterRepository.class);
        spells = injector.getInstance(SpellRepository.class);
        items = injector.getInstance(ItemRepository.class);
        friends = injector.getInstance(FriendRepository.class);
        npcs = injector.getInstance(NpcRepository.class);
        npcSells = injector.getInstance(NpcSellRepository.class);
        guilds = injector.getInstance(GuildRepository.class);
        guildMembers = injector.getInstance(GuildMemberRepository.class);
        waypointRecords = injector.getInstance(WaypointRecordRepository.class);
        storedItems = injector.getInstance(StoredItemRepository.class);

        breedTemplates = injector.getInstance(BreedTemplateRepository.class);
        experienceTemplates = injector.getInstance(ExperienceTemplateRepository.class);
        maps = injector.getInstance(MapRepository.class);
        mapTriggers = injector.getInstance(MapTriggerRepository.class);
        spellTemplates = injector.getInstance(SpellTemplateRepository.class);
        spellBreeds = injector.getInstance(SpellBreedRepository.class);
        itemTemplates = injector.getInstance(ItemTemplateRepository.class);
        itemSetTemplates = injector.getInstance(ItemSetTemplateRepository.class);
        npcTemplates = injector.getInstance(NpcTemplateRepository.class);
        npcQuestions = injector.getInstance(NpcQuestionRepository.class);
        npcResponses = injector.getInstance(NpcResponseRepository.class);
        waypoints = injector.getInstance(WaypointRepository.class);
        marketPlaces = injector.getInstance(MarketPlaceRepository.class);
    }

    private void loadStaticContext() throws SQLException {
        logger.info("{} breed templates loaded.", breedTemplates.loadAll());
        logger.info("{} spell templates loaded.", spellTemplates.loadAll());
        logger.info("{} breed's spells loaded.", spellBreeds.loadAll());
        logger.info("{} experience templates loaded.", experienceTemplates.loadAll());
        logger.info("{} maps loaded.", maps.loadAll());
        logger.info("{} map triggers loaded.", mapTriggers.loadAll());
        logger.info("{} item sets loaded.", itemSetTemplates.loadAll());
        logger.info("{} item templates loaded.", itemTemplates.loadAll());
        logger.info("{} npc reponses loaded.", npcResponses.loadAll());
        logger.info("{} npc questions loaded.", npcQuestions.loadAll());
        logger.info("{} npc templates loaded.", npcTemplates.loadAll());
        logger.info("{} waypoints loaded.", waypoints.loadAll());
        //logger.info("{} market places loaded.", marketPlaces.loadAll());
    }

    private void loadDynamicContext() throws SQLException {
        logger.info("{} accounts loaded.", accounts.loadAll());
        logger.info("{} characters loaded.", characters.loadAll());
        logger.info("{} spells loaded.", spells.loadAll());
        logger.info("{} items loaded.", items.loadAll());
        logger.info("{} friends loaded.", friends.loadAll());
        logger.info("{} npcs loaded.", npcs.loadAll());
        logger.info("{} npc sells loaded.", npcSells.loadAll());
        logger.info("{} guilds loaded.", guilds.loadAll());
        logger.info("{} guild members loaded.", guildMembers.loadAll());
        logger.info("{} waypoint records loaded.", waypointRecords.loadAll());
        logger.info("{} stored items loaded.", storedItems.loadAll());
    }

    private void saveDynamicContext() throws SQLException {
        logger.info("{} accounts saved.", accounts.saveAll());
        logger.info("{} characters saved.", characters.saveAll());
        logger.info("{} spells saved.", spells.saveAll());
        logger.info("{} items saved.", items.saveAll());
        logger.info("{} friends saved.", friends.saveAll());
        logger.info("{} npcs saved.", npcs.saveAll());
        logger.info("{} npc sells saved.", npcSells.saveAll());
        logger.info("{} guilds saved.", guilds.saveAll());
        logger.info("{} guild members saved.", guildMembers.saveAll());
        logger.info("{} waypoint records saved.", waypointRecords.saveAll());
        logger.info("{} stored items saved.", storedItems.saveAll());

        dynamicContext.commit();
    }

    @Override
    protected void action() {
        save();
    }

    @Override
    public void start(){
        if (started) return;

        try {
            staticContext.start();
            loadStaticContext();
            staticContext.stop();

            dynamicContext.start();
            loadDynamicContext();

            super.start();

            started = true;

            logger.info("Repositories successfully loaded.");
        } catch (LoadingException e) {
            logger.error("Can't load a repository because : " + e.getMessage());
        } catch (SQLException e) {
            logger.error("Can't start RepositoryManager because : " + e.getMessage());
        }
    }

    @Override
    public void stop(){
        if (!started) return;

        try {
            super.stop();

            saveDynamicContext();
            dynamicContext.commit();
        } catch (SQLException e) {
            logger.error("Can't end RepositoryManager.", e.getCause());
        } finally {
            try {
                dynamicContext.stop();
            } catch (SQLException e) {
                logger.error("Can't stop EntitiesContext : " + e.getMessage());
            } finally {
                started = false;
            }
        }
    }

    @Override
    public void save() {
        world.setState(WorldStateEnum.SAVING);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    saveDynamicContext();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                } finally {
                    world.setState(WorldStateEnum.ONLINE);
                }
            }
        });
    }

    @Override
    public AccountRepository getAccounts() {
        return accounts;
    }

    @Override
    public CharacterRepository getCharacters() {
        return characters;
    }

    @Override
    public BreedTemplateRepository getBreedTemplates() {
        return breedTemplates;
    }

    @Override
    public SpellRepository getSpells() {
        return spells;
    }

    @Override
    public ItemRepository getItems() {
        return items;
    }

    @Override
    public FriendRepository getFriends() {
        return friends;
    }

    @Override
    public NpcRepository getNpcs(){
        return npcs;
    }

    @Override
    public GuildRepository getGuilds() {
        return guilds;
    }

    @Override
    public GuildMemberRepository getGuildMembers() {
        return guildMembers;
    }

    @Override
    public ExperienceTemplateRepository getExperienceTemplates() {
        return experienceTemplates;
    }

    @Override
    public MapRepository getMaps() {
        return maps;
    }

    @Override
    public MapTriggerRepository getMapTriggers() {
        return mapTriggers;
    }

    @Override
    public SpellTemplateRepository getSpellTemplates() {
        return spellTemplates;
    }

    @Override
    public SpellBreedRepository getSpellBreeds() {
        return spellBreeds;
    }

    @Override
    public ItemTemplateRepository getItemTemplates() {
        return itemTemplates;
    }

    @Override
    public ItemSetTemplateRepository getItemSetTemplates() {
        return itemSetTemplates;
    }

    @Override
    public NpcTemplateRepository getNpcTemplates(){
        return npcTemplates;
    }

    @Override
    public NpcQuestionRepository getNpcQuestions() {
        return npcQuestions;
    }

    @Override
    public NpcResponseRepository getNpcResponses() {
        return npcResponses;
    }

    @Override
    public NpcSellRepository getNpcSells() {
        return npcSells;
    }

    @Override
    public WaypointRepository getWaypoints() {
        return waypoints;
    }

    @Override
    public MarketPlaceRepository getMarketPlaces() {
        return marketPlaces;
    }
}
