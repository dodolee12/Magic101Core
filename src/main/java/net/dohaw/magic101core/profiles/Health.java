package net.dohaw.magic101core.profiles;

public class Health {
    int currentHealth;
    int maxHealth;

    public Health(int maxHealth){
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public String toString(){
        return "" + maxHealth;
    }
}
