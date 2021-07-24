package net.dohaw.magic101core.items;

import org.bukkit.entity.Item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemProperties {
    private int level = 0; //Level requirement
    private int damage = 0;
    private int maxHealth = 0; // increase max health
    private double pierce = 0; //pierce percentage
    private double critChance = 0; //
    private double stunChance = 0;
    private double defense = 0;
    private double lifesteal = 0;
    private double lingeringChance = 0;
    private double lingeringDamage = 0;
    private double outgoingHealing = 0;
    private double incomingHealing = 0;
    private Map<String,Double> classSpecificProperties = new HashMap<>();

    public ItemProperties(){}

    public ItemProperties(int level, int damage, int maxHealth, double pierce, double critChance, double stunChance,
                          double defense, double lifesteal, double lingeringChance, double lingeringDamage,
                          double outgoingHealing, double incomingHealing){
        this.level = level;
        this.damage = damage;
        this.maxHealth = maxHealth;
        this.pierce = pierce;
        this.critChance = critChance;
        this.stunChance = stunChance;
        this.defense = defense;
        this.lifesteal = lifesteal;
        this.lingeringChance = lingeringChance;
        this.lingeringDamage = lingeringDamage;
        this.outgoingHealing = outgoingHealing;
        this.incomingHealing = incomingHealing;
    }

    //this looks really bad and im p sure theres better wyas to do it but idk how to in java
    public List<String> getPropLore() {
        return new ArrayList<String>() {{
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            add("&cLevel: &e" + getLevel());
            if (getDamage() != 0) {
                add("&cDamage: &e+" + getDamage());
            }
            if (getMaxHealth() != 0) {
                add("&cMax Health: &e+" + getMaxHealth());
            }
            if(getPierce() != 0){
                add("&cPierce: &e+" + decimalFormat.format(getPierce()) + "%");
            }
            if(getCritChance() != 0){
                add("&cCritical Strike Chance: &e+" + decimalFormat.format(getCritChance()) + "%");
            }
            if(getStunChance() != 0){
                add("&cStun Chance: &e+" + decimalFormat.format(getStunChance()) + "%");
            }
            if(getDefense() != 0){
                add("&cDefense: &e+" + decimalFormat.format(getDefense()) + "%");
            }
            if(getLifesteal() != 0){
                add("&cLifesteal: &e+" + decimalFormat.format(getLifesteal()) + "%");
            }
            if(getLingeringChance() != 0){
                add("&cLingering Chance: &e+" + decimalFormat.format(getLingeringChance()) + "%");
            }
            if(getLingeringDamage() != 0){
                add("&cLingering Damage: &e+" + decimalFormat.format(getLingeringDamage()) + "%");
            }
            if(getOutgoingHealing() != 0) {
                add("&cOutgoing Healing: &e+" + decimalFormat.format(getOutgoingHealing()) + "%");
            }
            if(getIncomingHealing() != 0){
                add("&cIncoming Healing: &e+" + decimalFormat.format(getIncomingHealing()) + "%");
            }
        }};
    }

    public List<String> getPropLoreWithoutLevel() {
        return new ArrayList<String>() {{
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            if (getDamage() != 0) {
                add("&cDamage: &e+" + getDamage());
            }
            if (getMaxHealth() != 0) {
                add("&cMax Health: &e+" + getMaxHealth());
            }
            if(getPierce() != 0){
                add("&cPierce: &e+" + decimalFormat.format(getPierce()) + "%");
            }
            if(getCritChance() != 0){
                add("&cCritical Strike Chance: &e+" + decimalFormat.format(getCritChance()) + "%");
            }
            if(getStunChance() != 0){
                add("&cStun Chance: &e+" + decimalFormat.format(getStunChance()) + "%");
            }
            if(getDefense() != 0){
                add("&cDefense: &e+" + decimalFormat.format(getDefense()) + "%");
            }
            if(getLifesteal() != 0){
                add("&cLifesteal: &e+" + decimalFormat.format(getLifesteal()) + "%");
            }
            if(getLingeringChance() != 0){
                add("&cLingering Chance: &e+" + decimalFormat.format(getLingeringChance()) + "%");
            }
            if(getLingeringDamage() != 0){
                add("&cLingering Damage: &e+" + decimalFormat.format(getLingeringDamage()) + "%");
            }
            if(getOutgoingHealing() != 0) {
                add("&cOutgoing Healing: &e+" + decimalFormat.format(getOutgoingHealing()) + "%");
            }
            if(getIncomingHealing() != 0){
                add("&cIncoming Healing: &e+" + decimalFormat.format(getIncomingHealing()) + "%");
            }
        }};
    }

    public static ItemProperties addTwoItemProperties(ItemProperties itemProperties1, ItemProperties itemProperties2){
        ItemProperties added = new ItemProperties();
        added.setDamage(itemProperties1.getDamage() + itemProperties2.getDamage());
        added.setMaxHealth(itemProperties1.getMaxHealth() + itemProperties2.getMaxHealth());
        added.setPierce(itemProperties1.getPierce() + itemProperties2.getPierce());
        added.setCritChance(itemProperties1.getCritChance() + itemProperties2.getCritChance());
        added.setStunChance(itemProperties1.getStunChance() + itemProperties2.getStunChance());
        added.setDefense(itemProperties1.getDefense() + itemProperties2.getDefense());
        added.setLifesteal(itemProperties1.getLifesteal() + itemProperties2.getLifesteal());
        added.setLingeringChance(itemProperties1.getLingeringChance() + itemProperties2.getLingeringChance());
        added.setLingeringDamage(itemProperties1.getLingeringDamage() + itemProperties2.getLingeringDamage());
        added.setOutgoingHealing(itemProperties1.getOutgoingHealing() + itemProperties2.getOutgoingHealing());
        added.setIncomingHealing(itemProperties1.getIncomingHealing() + itemProperties2.getIncomingHealing());

        return added;
    }

    public List<String> getClassPropsLore(){
        return new ArrayList<String>() {{
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                for(String propName: classSpecificProperties.keySet()){
                    if(classSpecificProperties.get(propName) == 0){
                        continue;
                    }
                    if(propName.contains("Damage")){
                        add("&c" + propName + ": &e+" + classSpecificProperties.get(propName).intValue());
                    }
                    else{
                        add("&c" + propName + ": &e+" + decimalFormat.format(classSpecificProperties.get(propName)) + "%");
                    }
                }
            }};
    }

    public List<String> getTotalPropLore(){
        List<String> totalPropLore = getPropLore();
        if(getClassPropsLore().size() > 0){
            totalPropLore.add("---------------------");
            totalPropLore.addAll(getClassPropsLore());
        }
        totalPropLore.add("---------------------");
        return totalPropLore;
    }

    public List<String> getTotalPropLoreWithoutLevel(){
        List<String> totalPropLore = getPropLoreWithoutLevel();
        if(getClassPropsLore().size() > 0){
            totalPropLore.add("---------------------");
            totalPropLore.addAll(getClassPropsLore());
        }
        totalPropLore.add("---------------------");
        return totalPropLore;
    }

    public void setClassProperty(String name, double amount){
        classSpecificProperties.put(name,amount);
    }

    public double getClassProperty(String name){
        return classSpecificProperties.getOrDefault(name,0d);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getPierce() {
        return pierce;
    }

    public void setPierce(double pierce) {
        this.pierce = pierce;
    }

    public double getCritChance() {
        return critChance;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }

    public double getStunChance() {
        return stunChance;
    }

    public void setStunChance(double stunChance) {
        this.stunChance = stunChance;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getLifesteal() {
        return lifesteal;
    }

    public void setLifesteal(double lifesteal) {
        this.lifesteal = lifesteal;
    }

    public double getLingeringChance() {
        return lingeringChance;
    }

    public void setLingeringChance(double lingeringChance) {
        this.lingeringChance = lingeringChance;
    }

    public double getLingeringDamage() {
        return lingeringDamage;
    }

    public void setLingeringDamage(double lingeringDamage) {
        this.lingeringDamage = lingeringDamage;
    }

    public double getOutgoingHealing() {
        return outgoingHealing;
    }

    public void setOutgoingHealing(double outgoingHealing) {
        this.outgoingHealing = outgoingHealing;
    }

    public double getIncomingHealing() {
        return incomingHealing;
    }

    public void setIncomingHealing(double incomingHealing) {
        this.incomingHealing = incomingHealing;
    }

    public void setClassSpecificProperties(Map<String,Double> classSpecificProperties){
        this.classSpecificProperties = classSpecificProperties;
    }
}

