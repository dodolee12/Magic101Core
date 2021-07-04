package net.dohaw.magic101core.profiles;

import net.dohaw.magic101core.menus.ProfileCreationMenu;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ProfileCreationSessionPrompt extends StringPrompt {

    public enum Change{
        PROFILE_NAME,
        CHARACTER_NAME,
    }

    private Change change;
    private ProfileCreationSession session;
    private ProfileCreationMenu prevMenu;

    public ProfileCreationSessionPrompt(Change change, ProfileCreationSession session, ProfileCreationMenu prevMenu){
        this.change = change;
        this.session = session;
        this.prevMenu = prevMenu;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        String returnText = "";
        switch(change){
            case PROFILE_NAME:
                returnText = "Please type what you would like your profile name to be.";
                break;
            case CHARACTER_NAME:
                returnText = "Please type what you would like your character name to be.";
                break;
        }
        return returnText;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        Player player = (Player) conversationContext.getForWhom();
        switch(change){
            case PROFILE_NAME:
                session.setProfileName(input);
                break;
            case CHARACTER_NAME:
                session.setCharacterName(input);
                break;
        }
        prevMenu.setSession(session);
        prevMenu.clearItems();
        prevMenu.initializeItems(player);
        prevMenu.openInventory(player);

        return END_OF_CONVERSATION;
    }



}