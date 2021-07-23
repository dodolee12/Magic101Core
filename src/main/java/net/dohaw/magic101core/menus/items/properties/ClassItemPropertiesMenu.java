package net.dohaw.magic101core.menus.items.properties;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.items.CreateItemMenu;
import net.dohaw.magic101core.menus.items.EditItemMenu;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.prompts.ClassPropertySelectionPrompt;
import net.dohaw.magic101core.utils.Constants;
import net.dohaw.magic101core.utils.StringUtil;
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
import java.util.List;

public class ClassItemPropertiesMenu extends Menu implements Listener {

    private ItemCreationSession session;

    public ClassItemPropertiesMenu(JavaPlugin plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu, "Class Item Properties", 54);
        this.session = session;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player player) {
        int i = 1;
        ItemProperties sessionProps = session.getItemProperties();
        for(Schools school: Schools.values()){
            if(school == Schools.UNIVERSAL){
                continue;
            }
            inv.setItem(i,createGuiItem(Constants.schoolsToMaterial.get(school),
                    "&cClass: &e" + school,new ArrayList<>()));
            String damageProp = StringUtil.capitalizeFirstLetter(school.toString().toLowerCase()) + " Damage";
            String pierceProp = StringUtil.capitalizeFirstLetter(school.toString().toLowerCase()) + " Pierce";
            String resistProp = StringUtil.capitalizeFirstLetter(school.toString().toLowerCase()) + " Resist";

            setItem(i + 9, Material.PAPER,damageProp,(int) sessionProps.getClassProperty(damageProp),true,false);
            setItem(i + 18, Material.PAPER,pierceProp,sessionProps.getClassProperty(pierceProp),true,true);
            setItem(i + 27, Material.PAPER,resistProp,sessionProps.getClassProperty(resistProp),true,true);

            ++i;
        }
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

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        ItemStack clickedItem = e.getCurrentItem();

        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE))
            return;

        if (slotClicked == (inv.getSize() - 1)) {
            if (previousMenu instanceof CreateItemMenu) {
                ((CreateItemMenu) previousMenu).setSession(session);
            } else if (previousMenu instanceof EditItemMenu) {
                ((EditItemMenu) previousMenu).setSession(session);
            }
            previousMenu.initializeItems(player);
            player.closeInventory();
            previousMenu.openInventory(player);
            return;
        }
        String fieldName = clickedItem.getItemMeta().getDisplayName().substring(9);
        ClassPropertySelectionPrompt.Type type;
        if(fieldName.contains("Damage")){
            type = ClassPropertySelectionPrompt.Type.INTEGER;
        }
        else{
            type = ClassPropertySelectionPrompt.Type.DOUBLE;
        }

        ConversationFactory cf = new ConversationFactory(plugin);
        Conversation conv = cf.withFirstPrompt(new ClassPropertySelectionPrompt(type, session.getItemProperties(), this, session, fieldName))
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

}
