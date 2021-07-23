package net.dohaw.magic101core.menus.items;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.CustomItem;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.items.lore.ViewLoreMenu;
import net.dohaw.magic101core.menus.items.properties.ClassItemPropertiesMenu;
import net.dohaw.magic101core.menus.items.properties.UniversalItemPropertiesMenu;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.prompts.ItemCreationSessionPrompt;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EditItemMenu extends Menu implements Listener {

    private ItemCreationSession session;

    public EditItemMenu(JavaPlugin plugin, Menu previousMenu, CustomItem customItem) {
        super(plugin, previousMenu, "Edit Item", 54);
        this.session = new ItemCreationSession(customItem);
        JPUtils.registerEvents(this);
    }



    @Override
    public void initializeItems(Player player) {
        Material sessionMat = session.getMaterial();
        String sessionDisplayName = session.getDisplayName();
        ItemProperties sessionProps = session.getItemProperties();
        String sessionSchoolName = session.getSchool() != null ? session.getSchool().toString() : "Not Selected";
        String sessionSpellName = session.getSpellName();



        /*
            Change Material
         */
        inv.setItem(10, createGuiItem(sessionMat, "&eChange Material", new ArrayList<>()));

        /*
            Change Display Name
         */
        List<String> displayNameLore = new ArrayList<String>(){{
            add("&cCurrent Display Name: &e" + sessionDisplayName);
        }};
        inv.setItem(12, createGuiItem(Material.PAPER, "&eChange Display Name", displayNameLore));

        /*
            Change Properties
         */

        inv.setItem(16, createGuiItem(Material.TRIPWIRE_HOOK, "&eChange Universal Properties", sessionProps.getPropLore()));

        /*
            Set Class
         */

        List<String> schoolLore = new ArrayList<String>(){{
            add("&cCurrent Class: &e" + sessionSchoolName);
        }};

        inv.setItem(28, createGuiItem(Material.TRIPWIRE_HOOK, "&eChange Class", schoolLore));


        /*
            Change Lore
         */
        List<String> loreLore = new ArrayList<String>(){{
            add("&bClick me to edit the lore!");
        }};
        inv.setItem(30, createGuiItem(Material.WRITABLE_BOOK, "&eChange Lore", loreLore));

        /*
            Set Spells
         */

        List<String> spellLore = new ArrayList<String>(){{
            add("&cCurrent Spell: &e" + sessionSpellName);
        }};

        inv.setItem(32, createGuiItem(Material.WRITABLE_BOOK, "&eChange Spell", spellLore));

        inv.setItem(34, createGuiItem(Material.TRIPWIRE_HOOK, "&eChange Class Specific Properties", sessionProps.getClassPropsLore()));


        //back buttonr
        inv.setItem(inv.getSize() - 9, createGuiItem(Material.BARRIER, "&eDelete Item", new ArrayList<>()));

        //Done button
        inv.setItem(inv.getSize() - 5, createGuiItem(Material.EMERALD_BLOCK, "&eEdit Item", new ArrayList<>()));

        inv.setItem(inv.getSize() - 1, createGuiItem(session.getMaterial(), "&eGet Item", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(false);

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

        int editItemSlot = inv.getSize() - 5;
        int addItemSlot = inv.getSize() - 1;
        int deleteItemSlot = inv.getSize() - 9;

        if(slotClicked == 10 || slotClicked == 12 || slotClicked == 14){

            ItemCreationSessionPrompt.Change change;
            if(slotClicked == 10){
                change = ItemCreationSessionPrompt.Change.MATERIAL;
            }else{
                change = ItemCreationSessionPrompt.Change.DISPLAY_NAME;
            }


            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ItemCreationSessionPrompt(session, this, change))
                    .withLocalEcho(false).buildConversation(player);
            conv.begin();

            player.closeInventory();

        }else if(slotClicked == 16) {

            //Change properties menu
            UniversalItemPropertiesMenu universalItemPropertiesMenu = new UniversalItemPropertiesMenu(plugin, this, session);
            universalItemPropertiesMenu.initializeItems(player);
            player.closeInventory();
            universalItemPropertiesMenu.openInventory(player);

        }else if(slotClicked == 30) {
            ViewLoreMenu dilm = new ViewLoreMenu(plugin, this, session);
            dilm.initializeItems(player);
            player.closeInventory();
            dilm.openInventory(player);
        }else if(slotClicked == 28) {

            //change class
            Menu newMenu = new ItemClassMenu(plugin,this, session);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);

        }else if(slotClicked == 32) {

            //change spells
            if(session.getSchool() == null || session.getSchool() == Schools.UNIVERSAL){
                player.sendMessage("You must set a class before you can attach a spell to this item!");
                return;
            }

            Menu newMenu = new SelectSpellMenu(plugin,this, session.getSchool(), session);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);

        }else if(slotClicked == 34){

            ClassItemPropertiesMenu classItemPropertiesMenu = new ClassItemPropertiesMenu(plugin, this, session);
            classItemPropertiesMenu.initializeItems(player);
            player.closeInventory();
            classItemPropertiesMenu.openInventory(player);

        }else if(slotClicked == editItemSlot){
            //edit item\
            ALL_ITEMS.ALL_ITEMS_MAP.put(session.getKey(), session.toCustomItem());
            player.sendMessage("Item has been edited successfully");

            Menu customItemsMenu = ((DisplayItemsMenu) previousMenu).getPreviousMenu();

            previousMenu = new DisplayItemsMenu(plugin, customItemsMenu, null, ALL_ITEMS.ALL_ITEMS_MAP.isEmpty() ? null : ALL_ITEMS.ALL_ITEMS_MAP.firstKey());

            previousMenu.initializeItems(player);
            player.closeInventory();
            previousMenu.openInventory(player);

        }
        else if(slotClicked == addItemSlot){
            player.getInventory().addItem(ALL_ITEMS.ALL_ITEMS_MAP.get(session.getKey()).toItemStack());
        }
        else if(slotClicked == deleteItemSlot){
            ALL_ITEMS.ALL_ITEMS_MAP.remove(session.getKey());
            player.sendMessage("Item has been deleted successfully");

            Menu customItemsMenu = ((DisplayItemsMenu) previousMenu).getPreviousMenu();

            previousMenu = new DisplayItemsMenu(plugin, customItemsMenu, null, ALL_ITEMS.ALL_ITEMS_MAP.isEmpty() ? null : ALL_ITEMS.ALL_ITEMS_MAP.firstKey());

            previousMenu.initializeItems(player);
            player.closeInventory();
            previousMenu.openInventory(player);
        }
    }

    public ItemCreationSession getSession() {
        return session;
    }

    public void setSession(ItemCreationSession session) {
        this.session = session;
    }


}
