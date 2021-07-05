package net.dohaw.magic101core.profiles;

public class Health {

    private int currentHealth;
    private int maxHealth;

    public Health(int maxHealth){
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public String toString(){
        return "" + maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
}
