package net.dohaw.magic101core.profiles;

import org.bukkit.Location;

public class Profile {
    private String profileName;
    private String characterName;
    private Schools school;
    private Stats stats;
    //private List<> customItems + items
    private Location logoutLocation;

    public Profile(String profileName, String characterName, Schools school, Stats stats){
        this.profileName = profileName;
        this.characterName = characterName;
        this.school = school;
        this.stats = stats;
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

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Location getLogoutLocation() {
        return logoutLocation;
    }

    public void setLogoutLocation(Location logoutLocation) {
        this.logoutLocation = logoutLocation;
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