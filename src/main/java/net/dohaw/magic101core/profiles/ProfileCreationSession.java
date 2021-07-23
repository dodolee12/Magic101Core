package net.dohaw.magic101core.profiles;

public class ProfileCreationSession {

    private String profileName = "Profile";
    private String characterName = "Not Set";
    private Schools school = Schools.UNIVERSAL;

    public ProfileCreationSession(){
    }

    public ProfileCreationSession(String profileName, String characterName, Schools school) {
        this.profileName = profileName;
        this.characterName = characterName;
        this.school = school;
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

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }



}
