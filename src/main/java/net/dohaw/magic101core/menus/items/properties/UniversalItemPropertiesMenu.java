package net.dohaw.magic101core.menus.items.properties;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.items.CreateItemMenu;
import net.dohaw.magic101core.menus.items.EditItemMenu;
import net.dohaw.magic101core.prompts.UniversalPropertySelectionPrompt;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversalItemPropertiesMenu extends Menu implements Listener {

    private ItemCreationSession session;

    public UniversalItemPropertiesMenu(JavaPlugin plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu, "Item Properties", 45);
        this.session = session;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player player) {
        ItemProperties sessionProperties = session.getItemProperties();

        setItem(4,Material.PAPER, "Damage", sessionProperties.getDamage(), true, false);
        setItem(10,Material.PAPER, "Level", sessionProperties.getLevel(), false, false);
        setItem(12,Material.PAPER, "Strength", sessionProperties.getStrength(),true,true);
        setItem(14,Material.PAPER, "Max Health", sessionProperties.getMaxHealth(),true, false);
        setItem(16,Material.PAPER, "Pierce", sessionProperties.getPierce(), true, true);
        setItem(19,Material.PAPER, "Critical Strike Chance", sessionProperties.getCritChance(), true, true);
        setItem(21,Material.PAPER, "Stun Chance", sessionProperties.getStunChance(), true, true);
        setItem(23,Material.PAPER, "Defense", sessionProperties.getDefense(), true, true);
        setItem(25,Material.PAPER, "Lifesteal", sessionProperties.getLifesteal(), true, true);
        setItem(28,Material.PAPER, "Lingering Chance", sessionProperties.getLingeringChance(), true, true);
        setItem(30,Material.PAPER, "Lingering Damage", sessionProperties.getLingeringDamage(), true, true);
        setItem(32,Material.PAPER, "Outgoing Healing", sessionProperties.getOutgoingHealing(), true, true);
        setItem(34,Material.PAPER, "Incoming Healing", sessionProperties.getIncomingHealing(), true, true);

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);

    }

    private void setItem(int invSlot, Material material, String fieldName, int field, boolean addition, boolean percent){
        List<String> lore = new ArrayList<String>(){{
            add("&cCurrent " + fieldName + ": &e" + (addition ? "+" : "" ) + field + (percent ? "%" : ""));
        }};
        inv.setItem(invSlot, createGuiItem(material, "&eChange " + fieldName, lore));
    }
    private void setItem(int invSlot, Material material, String fieldName, double field, boolean addition, boolean percent){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        List<String> lore = new ArrayList<String>(){{
            add("&cCurrent " + fieldName + ": &e" + (addition ? "+" : "" ) + decimalFormat.format(field) + (percent ? "%" : ""));
        }};
        inv.setItem(invSlot, createGuiItem(material, "&eChange " + fieldName, lore));
    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) return;

        if(slotClicked == (inv.getSize() - 1)){
            if(previousMenu instanceof CreateItemMenu){
                ((CreateItemMenu) previousMenu).setSession(session);
            }else if(previousMenu instanceof EditItemMenu){
                ((EditItemMenu) previousMenu).setSession(session);
            }
            previousMenu.initializeItems(player);
            player.closeInventory();
            previousMenu.openInventory(player);
            return;
        }

        UniversalPropertySelectionPrompt.Change change = slotToChange.get(slotClicked);

        ConversationFactory cf = new ConversationFactory(plugin);
        Conversation conv = cf.withFirstPrompt(new UniversalPropertySelectionPrompt(change, session.getItemProperties(), this, session))
                .withLocalEcho(false).buildConversation(player);;
        conv.begin();

        player.closeInventory();

    }

    public ItemCreationSession getSession() {
        return session;
    }

    public void setSession(ItemCreationSession session) {
        this.session = session;
    }

    private final Map<Integer, UniversalPropertySelectionPrompt.Change> slotToChange = new HashMap<Integer, UniversalPropertySelectionPrompt.Change>(){{
        put(4, UniversalPropertySelectionPrompt.Change.DAMAGE);
        put(10, UniversalPropertySelectionPrompt.Change.LEVEL);
        put(12, UniversalPropertySelectionPrompt.Change.STRENGTH);
        put(14, UniversalPropertySelectionPrompt.Change.MAX_HEALTH);
        put(16, UniversalPropertySelectionPrompt.Change.PIERCE);
        put(19, UniversalPropertySelectionPrompt.Change.CRIT_CHANCE);
        put(21, UniversalPropertySelectionPrompt.Change.STUN_CHANCE);
        put(23, UniversalPropertySelectionPrompt.Change.DEFENSE);
        put(25, UniversalPropertySelectionPrompt.Change.LIFESTEAL);
        put(28, UniversalPropertySelectionPrompt.Change.LINGERING_CHANCE);
        put(30, UniversalPropertySelectionPrompt.Change.LINGERING_DAMAGE);
        put(32, UniversalPropertySelectionPrompt.Change.OUTGOING_HEALING);
        put(34, UniversalPropertySelectionPrompt.Change.INCOMING_HEALING);
    }};

}
