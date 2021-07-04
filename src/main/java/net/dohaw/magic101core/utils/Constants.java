package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.profiles.Schools;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public static Map<Schools, Integer> schoolToBaseHealth = new HashMap<Schools, Integer>(){{
        put(Schools.ICE,800);
        put(Schools.LIFE,700);
        put(Schools.DEATH,650);
        put(Schools.BALANCE,650);
        put(Schools.STORM,400);
        put(Schools.FIRE,550);
        put(Schools.MYTH,700);
    }};
}
