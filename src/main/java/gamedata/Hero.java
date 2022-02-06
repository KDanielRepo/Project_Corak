package gamedata;

import lombok.Data;

import java.util.List;

@Data
public class Hero {
    private int attack;
    private int defense;
    private int might;
    private int knowledge;
    private int mana;
    private int moraleModifier;
    private int luckModifier;
    private int experience;
    private Skill personalSkill;
    private List<Skill> skills;
    private List<Unit> units;
    private List<Spell> spells;
}
