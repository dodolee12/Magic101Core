package net.dohaw.magic101core.prompts;

import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.items.properties.ClassItemPropertiesMenu;
import net.dohaw.magic101core.menus.items.properties.UniversalItemPropertiesMenu;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassPropertySelectionPrompt extends StringPrompt {

    private Type type;
    private ItemProperties properties;
    private ClassItemPropertiesMenu previousMenu;
    private ItemCreationSession session;
    private String fieldName;

    public enum Type{
        INTEGER,
        DOUBLE
    }

    public ClassPropertySelectionPrompt(ClassPropertySelectionPrompt.Type type, ItemProperties properties, ClassItemPropertiesMenu prevMenu, ItemCreationSession session, String fieldName) {
        this.type = type;
        this.properties = properties;
        this.previousMenu = prevMenu;
        this.session = session;
        this.fieldName = fieldName;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        String returnText = "";
        switch (type) {
            case INTEGER:
                returnText = "Please type the amount of " + fieldName + " on this item. Must be an integer number.";
                break;
            case DOUBLE:
                returnText = "Please type the amount of " + fieldName + " on this item. Must be a number between 0 and 1.";

        }
        return returnText;
    }

    @Nullable
    @Override
    public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String input) {
        Player player = (Player) conversationContext.getForWhom();

        switch(type){
            case INTEGER:
                int amountInt = validateInteger(input,player);
                if(amountInt == -1){
                    break;
                }
                properties.setClassProperty(fieldName,amountInt);
                break;
            case DOUBLE:
                double amountDouble = validateDouble(input,player);
                if(Double.isNaN(amountDouble)){
                    break;
                }
                properties.setClassProperty(fieldName,amountDouble);
                break;
        }
        session.setItemProperties(properties);
        previousMenu.setSession(session);
        previousMenu.clearItems();
        previousMenu.initializeItems(player);
        player.closeInventory();
        previousMenu.openInventory(player);
        return END_OF_CONVERSATION;
    }

    private int validateInteger(String input, Player player){
        try{
            return Integer.parseInt(input);
        }catch (Exception e){
            player.sendMessage("This is not an integer");
        }
        return -1;
    }

    private double validateDouble(String input, Player player){
        double number;
        try{
            number = Double.parseDouble(input);
        }catch (Exception e){
            player.sendMessage("This is not an number");
            return Double.NaN;
        }

        if(!(number >= 0 && number <= 1)){
            player.sendMessage("This is not an number between 0 and 1");
            return Double.NaN;
        }

        return number*100;
    }
}