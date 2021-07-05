package net.dohaw.magic101core.profiles;

public class Health {

    private int currentHealth;
    private int maxHealth;

    public Health(int maxHealth){
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public Health(int maxHealth, int currentHealth){
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
    }

    public static Health loadHealthFromConfig(int maxHealth, int currentHealth){
        return new Health(maxHealth,currentHealth);
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
