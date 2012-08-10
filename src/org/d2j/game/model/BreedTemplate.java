package org.d2j.game.model;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.CharacteristicsEnum;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.utils.database.entity.IBaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 19:04
 * IDE : IntelliJ IDEA
 */
public class BreedTemplate implements IBaseEntity<Byte> {
    private Byte id;
    private short startAP;
    private short startMP;
    private short startLife;
    private short startProspection;
    private Map<CharacteristicType, Map<short[], short[]>> characteristics = new HashMap<>();
    private List<SpellBreed> spells = new ArrayList<>();

    public BreedTemplate(Byte id, short startAP, short startMP, short startLife, short startProspection,
                         String intelligence, String chance, String agility, String strength, String vitality, String wisdom) {
        this.id = id;
        this.startAP = startAP;
        this.startMP = startMP;
        this.startLife = startLife;
        this.startProspection = startProspection;

        load(CharacteristicType.Intelligence, intelligence);
        load(CharacteristicType.Intelligence, intelligence);
        load(CharacteristicType.Chance, chance);
        load(CharacteristicType.Agility, agility);
        load(CharacteristicType.Strength, strength);
        load(CharacteristicType.Vitality, vitality);
        load(CharacteristicType.Wisdom, wisdom);
    }

    public Byte getId() {
        return id;
    }

    public short getStartAP() {
        return startAP;
    }

    public short getStartMP() {
        return startMP;
    }

    public short getStartLife() {
        return startLife;
    }

    public short getStartProspection() {
        return startProspection;
    }

    public List<SpellBreed> getSpells() {
        return spells;
    }

    private void load(CharacteristicType characteristic, String data) {
        HashMap<short[], short[]> c = new HashMap<>();
        for (String a : data.split("\\|")){
            String[] args = a.split(":");

            c.put(
                    StringUtils.toShort(args[0].split(",")),
                    StringUtils.toShort(args[1].split("\\-"))
            );
        }
        characteristics.put(characteristic, c);
    }

    public short[] getBonusAndCost(CharacteristicType characteristic, short stats) {
        Map<short[], short[]> costs = characteristics.get(characteristic);
        for (short[] interval : costs.keySet()) {
            if (stats >= interval[0] && (interval.length <= 1 || stats <= interval[1])){
                return costs.get(interval);
            }
        }

        return null;
    }

    public List<SpellBreed> getDefaultSpells(){
        List<SpellBreed> results = new ArrayList<>();
        for (SpellBreed spell : spells){
            if (spell.getDefaultPosition() != -1){
                results.add(spell);
            }
        }
        return results;
    }

    public SpellBreed getNextSpell(short currentLevel){
        SpellBreed spell = spells.get(0);
        for (int i = 1; i < spells.size() && spell.getLevel() < currentLevel; ++i){
            spell = spells.get(i);
        }
        return spell;
    }

    public short getSkin(boolean gender){
        return (short) (id * 10 + (gender ? 1 : 0));
    }

    public String toShortString(){
        return toString().substring(0, 3);
    }

    @Override
    public String toString(){
        switch (id){
    		case 1:
                return "Féca";

    		case 2:
                return "Osamodas";

    		case 3:
                return "Enutrof";

    		case 4:
                return "Sram";

    		case 5:
    			return "Xélor";

    		case 6:
    			return "Ecaflip";

    		case 7:
                return "Eniripsa";

    		case 8:
    			return "Iop";

    		case 9:
    			return "Crâ";

    		case 10:
    			return "Sadida";

    		case 11:
    			return "Sacrieur";

    		case 12:
    			return "Pandawa";

            default:
                return "UNKNOWN";
    	}
    }
}
