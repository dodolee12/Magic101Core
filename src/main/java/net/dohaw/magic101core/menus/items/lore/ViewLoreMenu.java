package net.dohaw.magic101core.menus.items.lore;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.prompts.ItemCreationSessionPrompt;
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

public class ViewLoreMenu extends Menu implements Listener {

    private ItemCreationSession session;
    private final Material ITEM_MAT = Material.PAPER;

    public ViewLoreMenu(JavaPlugin plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu, "View Lore", 45);
        this.session = session;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player player) {
        int num = 1;
        List<String> sessionLore = session.getLore();
        for(String line : sessionLore){
            String displayName = "&cLine &e" + num;
            List<String> itemLore = new ArrayList<>();
            itemLore.add(line);
            inv.addItem(createGuiItem(ITEM_MAT, displayName, itemLore));
            num++;
        }

        inv.setItem(inv.getSize() - 9, createGuiItem(Material.SLIME_BLOCK, "&eAdd Lore Line", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);
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

        int addLoreLineSlot = inv.getSize() - 9;

        if(clickedItem.getType() == ITEM_MAT){

            EditLoreMenu newMenu = new EditLoreMenu(plugin, this, session, slotClicked);

            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);

        }else if(clickedItem.getType() == backMat){
            goToPreviousMenu(player);
        }else if(slotClicked == addLoreLineSlot){

            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ItemCreationSessionPrompt(session,this,
                    ItemCreationSessionPrompt.Change.ADD_LORE_LINE, slotClicked))
                    .withLocalEcho(false).buildConversation(player);
            conv.begin();
            player.closeInventory();
        }
    }

    public ItemCreationSession getSession() {
        return session;
    }

    public void setSession(ItemCreationSession session) {
        this.session = session;
    }
}
