package net.dohaw.magic101core.items;

public class ItemProperties {
    private int level; //Level requirement
    private int maxHealth; // increase max health
    private int pierce; //pierce percentage
    private int critChance; //
    private int stunChance;
    private int defense;
    private int lifesteal;
    private int lingeringChance;
    private int lingeringDamage;
    private int outgoingHealing;
    private int notice;
    private int incomingHealing;

    public ItemProperties(int level, int maxHealth, int pierce, int critChance, int stunChance,
                          int defense, int lifesteal, int lingeringChance, int lingeringDamage,
                          int outgoingHealing, int notice, int incomingHealing){
        this.level = level;
        this.maxHealth = maxHealth;
        this.pierce = pierce;
        this.critChance = critChance;
        this.stunChance = stunChance;
        this.defense = defense;
        this.lifesteal = lifesteal;
        this.lingeringChance = lingeringChance;
        this.lingeringDamage = lingeringDamage;
        this.outgoingHealing = outgoingHealing;
        this.notice = notice;
        this.incomingHealing = incomingHealing;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getPierce() {
        return pierce;
    }

    public void setPierce(int pierce) {
        this.pierce = pierce;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public int getStunChance() {
        return stunChance;
    }

    public void setStunChance(int stunChance) {
        this.stunChance = stunChance;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getLifesteal() {
        return lifesteal;
    }

    public void setLifesteal(int lifesteal) {
        this.lifesteal = lifesteal;
    }

    public int getLingeringChance() {
        return lingeringChance;
    }

    public void setLingeringChance(int lingeringChance) {
        this.lingeringChance = lingeringChance;
    }

    public int getLingeringDamage() {
        return lingeringDamage;
    }

    public void setLingeringDamage(int lingeringDamage) {
        this.lingeringDamage = lingeringDamage;
    }

    public int getOutgoingHealing() {
        return outgoingHealing;
    }

    public void setOutgoingHealing(int outgoingHealing) {
        this.outgoingHealing = outgoingHealing;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public int getIncomingHealing() {
        return incomingHealing;
    }

    public void setIncomingHealing(int incomingHealing) {
        this.incomingHealing = incomingHealing;
    }
}
