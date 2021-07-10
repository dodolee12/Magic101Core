package net.dohaw.magic101core.menus.items.lore;

import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.prompts.ItemCreationSessionPrompt;
import org.bukkit.Bukkit;
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

public class EditLoreMenu extends Menu implements Listener {
    private ItemCreationSession session;
    private int indexLoreLine;

    public EditLoreMenu(JavaPlugin plugin, ViewLoreMenu previousMenu, ItemCreationSession session, int indexLoreLine) {
        super(plugin, previousMenu, "Edit Lore Line", 27);
        this.session = session;
        this.indexLoreLine = indexLoreLine;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.setItem(10, createGuiItem(Material.PAPER, "&eEdit Line", new ArrayList<>()));
        inv.setItem(16, createGuiItem(Material.BARRIER, "&eDelete Line", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) return;

        if(slotClicked == 10){
            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ItemCreationSessionPrompt(
                    session, previousMenu,
                    ItemCreationSessionPrompt.Change.EDIT_LORE_LINE, indexLoreLine))
                    .withLocalEcho(false).buildConversation(player);
            conv.begin();

            player.closeInventory();
        }else if(slotClicked == 16){

            List<String> lore = session.getLore();
            lore.remove(indexLoreLine);
            session.setLore(lore);

            ViewLoreMenu dilm = (ViewLoreMenu) previousMenu;
            dilm.setSession(session);

            dilm.clearItems();
            dilm.initializeItems(player);
            player.closeInventory();
            dilm.openInventory(player);

            player.sendMessage("You have deleted this line from the lore!");
        }else if(clickedItem.getType() == backMat){
            goToPreviousMenu(player);
        }

    }

    public ItemCreationSession getSession() {
        return session;
    }

    public void setSession(ItemCreationSession session) {
        this.session = session;
    }
}
