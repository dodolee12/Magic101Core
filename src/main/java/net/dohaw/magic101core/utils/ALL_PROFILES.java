package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.profiles.Profile;

import java.util.*;

public final class ALL_PROFILES {
    public static Map<UUID,List<Profile>> ALL_PROFILES_MAP = new HashMap<>();

    public static Profile getProfileByProfileName(UUID playerUUID, String profileName){
        if(ALL_PROFILES_MAP.containsKey(playerUUID)){
            for(Profile profile: ALL_PROFILES_MAP.get(playerUUID)){
                if(profile.getProfileName().equals(profileName)){
                    return profile;
                }
            }
        }
        return null;
    }

    public static Profile findActiveProfile(UUID playerUUID){
        if(ALL_PROFILES_MAP.containsKey(playerUUID)){
            for(Profile profile: ALL_PROFILES_MAP.get(playerUUID)){
                if(profile.isActive()){
                    return profile;
                }
            }
        }
        return null;
    }
}
