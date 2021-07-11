package net.dohaw.magic101core.prompts;

import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.menus.items.CreateItemMenu;
import net.dohaw.magic101core.menus.items.DisplayItemsMenu;
import net.dohaw.magic101core.menus.items.EditItemMenu;
import net.dohaw.magic101core.menus.items.lore.EditLoreMenu;
import net.dohaw.magic101core.menus.items.lore.ViewLoreMenu;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemCreationSessionPrompt extends StringPrompt {

    private ItemCreationSession session;
    private Menu previousMenu;
    private Change change;

    private int indexLoreLine;

    public ItemCreationSessionPrompt(ItemCreationSession session, Menu previousMenu, Change change) {
        this.session = session;
        this.previousMenu = previousMenu;
        this.change = change;
    }

    public ItemCreationSessionPrompt(ItemCreationSession session, Menu previousMenu, Change change, int loreLine){
        this.session = session;
        this.previousMenu = previousMenu;
        this.change = change;
        this.indexLoreLine = loreLine;
    }

    /*
            What can be changed within a Session
         */
    public enum Change{
        MATERIAL,
        DISPLAY_NAME,
        KEY,
        EDIT_LORE_LINE,
        ADD_LORE_LINE,
    }

    /**
     * Gets the text to display to the user when this prompt is first
     * presented.
     *
     * @param context Context information about the conversation.
     * @return The text to display.
     */
    @Override
    public String getPromptText(ConversationContext context) {
        if(change == Change.MATERIAL){
            return "Please type what you would like the material for this item to be. This is NOT case-sensitive...";
        }else if(change == Change.DISPLAY_NAME) {
            return "Please type what you would like the display name for this item to be...";
        }else if(change == Change.EDIT_LORE_LINE || change == Change.ADD_LORE_LINE) {
            return "Please type what you would like the new lore line to be...";
        }else{
            return "Please type what you would like the key for this item to be...";
        }
    }

    /**
     * Accepts and processes input from the user. Using the input, the next
     * Prompt in the prompt graph is returned.
     *
     * @param context Context information about the conversation.
     * @param input   The input text from the user.
     * @return The next Prompt in the prompt graph.
     */
    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        Player player = (Player) context.getForWhom();

        if(change == Change.MATERIAL){

            Material newMat;
            try{
                newMat = Material.valueOf(input.toUpperCase());
                player.sendRawMessage("The material for this custom item has been set to " + newMat.name() + "!");
            }catch(IllegalArgumentException e){
                player.sendRawMessage("The material you desire can't be found! The material has been defaulted to an apple!");
                newMat = Material.APPLE;
            }
            session.setMaterial(newMat);

        }else if(change == Change.DISPLAY_NAME) {
            session.setDisplayName(input);
            player.sendRawMessage("The display name for this custom item has been set to " + input);
        }else if(change == Change.EDIT_LORE_LINE || change == Change.ADD_LORE_LINE) {

            List<String> lore = session.getLore();
            if (change == Change.EDIT_LORE_LINE) {
                lore.set(indexLoreLine, input);
            } else {
                lore.add(input);
            }
            player.sendRawMessage("The lore for this custom item has been updated!");

            session.setLore(lore);

            ((ViewLoreMenu) previousMenu).setSession(session);

            previousMenu.clearItems();
            previousMenu.initializeItems(player);
            player.closeInventory();
            previousMenu.openInventory(player);
            return END_OF_CONVERSATION;

        }else{
            if(!ALL_ITEMS.ALL_ITEMS_MAP.containsKey(input)){
                session.setKey(input.toLowerCase());
                player.sendRawMessage("The key for this custom item has been set to " + input.toLowerCase());
            }else{
                player.sendRawMessage("This is already a key! Please choose another one...");
                previousMenu.initializeItems(player);
                player.closeInventory();
                previousMenu.openInventory(player);
                return END_OF_CONVERSATION;
            }
        }

        if(previousMenu instanceof  CreateItemMenu){
            ((CreateItemMenu) previousMenu).setSession(session);
        }else if(previousMenu instanceof EditItemMenu){
            ((EditItemMenu) previousMenu).setSession(session);
        }

        previousMenu.clearItems();
        previousMenu.initializeItems(player);
        player.closeInventory();
        previousMenu.openInventory(player);
        return END_OF_CONVERSATION;
    }
}
