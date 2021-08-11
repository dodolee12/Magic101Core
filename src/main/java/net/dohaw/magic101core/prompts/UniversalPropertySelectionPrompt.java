package net.dohaw.magic101core.prompts;

import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.items.properties.UniversalItemPropertiesMenu;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class UniversalPropertySelectionPrompt extends StringPrompt {

    private Change change;
    private ItemProperties properties;
    private UniversalItemPropertiesMenu previousMenu;
    private ItemCreationSession session;

    public enum Change {
        LEVEL,
        DAMAGE,
        STRENGTH,
        MAX_HEALTH,
        PIERCE,
        CRIT_CHANCE,
        STUN_CHANCE,
        DEFENSE,
        LIFESTEAL,
        LINGERING_CHANCE,
        LINGERING_DAMAGE,
        OUTGOING_HEALING,
        INCOMING_HEALING
    }

    public UniversalPropertySelectionPrompt(Change change, ItemProperties properties, UniversalItemPropertiesMenu prevMenu, ItemCreationSession session) {
        this.change = change;
        this.properties = properties;
        this.previousMenu = prevMenu;
        this.session = session;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        String returnText = "";
        switch(change){
            case LEVEL:
                returnText = "Please type the minimum level for this item. Must be an integer number.";
                break;
            case DAMAGE:
                returnText = "Please type the damage addition this item gives. Must be an integer number.";
                break;
            case MAX_HEALTH:
                returnText = "Please type how much health this item should give. Must be an integer number.";
                break;
            case STRENGTH:
                returnText = "Please type how much strength this item should give. Must be a number between 0 and 1.";
                break;
            case PIERCE:
                returnText = "Please type how much pierce this item should give. Must be a number between 0 and 1.";
                break;
            case CRIT_CHANCE:
                returnText = "Please type how much critical strike chance this item should give. Must be a number between 0 and 1.";
                break;
            case STUN_CHANCE:
                returnText = "Please type how much stun chance this item should give. Must be a number between 0 and 1.";
                break;
            case DEFENSE:
                returnText = "Please type how much defense this item should give. Must be a number between 0 and 1.";
                break;
            case LIFESTEAL:
                returnText = "Please type how much lifesteal this item should give. Must be a number between 0 and 1.";
                break;
            case LINGERING_CHANCE:
                returnText = "Please type how much lingering damage change this item should give. Must be a number between 0 and 1.";
                break;
            case LINGERING_DAMAGE:
                returnText = "Please type how much lingering damage this item should give. Must be a number between 0 and 1.";
                break;
            case OUTGOING_HEALING:
                returnText = "Please type how much outgoing healing this item should give. Must be a number between 0 and 1.";
                break;
            case INCOMING_HEALING:
                returnText = "Please type how much incoming healing this item should give. Must be a number between 0 and 1.";
                break;
        }
        return returnText;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        Player player = (Player) conversationContext.getForWhom();

        switch(change){
            case LEVEL:
                int level = validateInteger(input,player);
                if(level == -1){
                    break;
                }
                properties.setLevel(level);
                break;
            case DAMAGE:
                int damage = validateInteger(input,player);
                if(damage == -1){
                    break;
                }
                properties.setDamage(damage);
                break;
            case MAX_HEALTH:
                int maxHealth = validateInteger(input,player);
                if(maxHealth == -1){
                    break;
                }
                properties.setMaxHealth(maxHealth);
                break;
            case PIERCE:
                double pierce = validateDouble(input, player);
                if(Double.isNaN(pierce)){
                    break;
                }
                properties.setPierce(pierce);
                break;
            case STRENGTH:
                double strength = validateDouble(input, player);
                if(Double.isNaN(strength)){
                    break;
                }
                properties.setStrength(strength);
                break;
            case CRIT_CHANCE:
                double critChance = validateDouble(input, player);
                if(Double.isNaN(critChance)){
                    break;
                }
                properties.setCritChance(critChance);
                break;
            case STUN_CHANCE:
                double stunChance = validateDouble(input, player);
                if(Double.isNaN(stunChance)){
                    break;
                }
                properties.setStunChance(stunChance);
                break;
            case DEFENSE:
                double defense = validateDouble(input, player);
                if(Double.isNaN(defense)){
                    break;
                }
                properties.setDefense(defense);
                break;
            case LIFESTEAL:
                double lifesteal = validateDouble(input, player);
                if(Double.isNaN(lifesteal)){
                    break;
                }
                properties.setLifesteal(lifesteal);
                break;
            case LINGERING_CHANCE:
                double lingeringChance = validateDouble(input, player);
                if(Double.isNaN(lingeringChance)){
                    break;
                }
                properties.setLingeringChance(lingeringChance);
                break;
            case LINGERING_DAMAGE:
                double lingeringDamage = validateDouble(input,player);
                if(Double.isNaN(lingeringDamage)){
                    break;
                }
                properties.setLingeringDamage(lingeringDamage);
                break;
            case OUTGOING_HEALING:
                double outgoingHealing = validateDouble(input, player);
                if(Double.isNaN(outgoingHealing)){
                    break;
                }
                properties.setOutgoingHealing(outgoingHealing);
                break;
            case INCOMING_HEALING:
                double incomingHealing = validateDouble(input, player);
                if(Double.isNaN(incomingHealing)){
                    break;
                }
                properties.setIncomingHealing(incomingHealing);
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
