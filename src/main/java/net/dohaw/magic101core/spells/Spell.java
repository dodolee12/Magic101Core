package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Schools;

public abstract class Spell {
    Schools school;

    public Spell(Schools school){
        this.school = school;
    }

    public Schools getSchool() {
        return school;
    }

    public void setSchool(Schools school) {
        this.school = school;
    }


}
