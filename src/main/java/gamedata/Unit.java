package gamedata;

import gamedata.enums.UnitType;

import java.util.List;

public class Unit {
    private int attack;
    private int defense;
    private int ammo;
    private int[] damageRange;
    private int maxHealth;
    private int currentHealth;
    private int speed;
/*    private int cost;
    private int growth;*/ //Moze?
    private UnitType unitType;
    private Skill personalSkill;
    private boolean canCounter;
    private boolean canUsePersonalSkill;
    private boolean canFly;
    private int unitSize;
    private int amount;
    private List<Spell> spellsInEffect;
}
