package net.dohaw.magic101core.profiles;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private String profileName;
    private String characterName;
    private Schools school;
    private int level;
    private Health health;
    private ProfileCreationSession session;
    private boolean active;
    private Inventory playerInventory;
    private Location logoutLocation;


    public Profile(String profileName, String characterName, Schools school, int level, Health health, ProfileCreationSession session, boolean active){
        this.profileName = profileName;
        this.characterName = characterName;
        this.school = school;
        this.level = level;
        this.health = health;
        this.session = session;
        this.active = active;
    }

    //constructor for laoading
    public Profile(String profileName, String characterName, Schools school, int level, Health health, ProfileCreationSession session, Location logoutLocation) {
        this.profileName = profileName;
        this.characterName = characterName;
        this.school = school;
        this.level = level;
        this.health = health;
        this.session = session;
        this.logoutLocation = logoutLocation;
    }

    //eventually add inventory
    public static Profile loadProfileFromConfig(String profileName, String characterName, Schools school, int level, int maxHealth, int currentHealth, Location logoutLocation){
        Health profileHealth = Health.loadHealthFromConfig(maxHealth, currentHealth);
        ProfileCreationSession session = new ProfileCreationSession(profileName,characterName,school);
        return new Profile(profileName,characterName,school,level,profileHealth,session,logoutLocation);
    }

    public ProfileCreationSession getSession() {
        return session;
    }

    public void setSession(ProfileCreationSession session) {
        this.session = session;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Schools getSchool() {
        return school;
    }

    public void setSchool(Schools school) {
        this.school = school;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }

    public Location getLogoutLocation() {
        return logoutLocation;
    }

    public void setLogoutLocation(Location logoutLocation) {
        this.logoutLocation = logoutLocation;
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(Inventory playerInventory) {
        this.playerInventory = playerInventory;
    }
}
/*
	Profile name
	Character name
	Class/school
	Stats
	Items
	Last logout location
	IsActive (is the player currently playing it right now)
 */