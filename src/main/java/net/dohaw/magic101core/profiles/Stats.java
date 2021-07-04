package net.dohaw.magic101core.profiles;

public class Stats {
    private int level;
    private Health health;

    public Stats(int level, Health health){
        this.level = level;
        this.health = health;
    }

    public String toString(){
        return "Level: " + level + ", " + "Health: " + health.toString();
    }
}
