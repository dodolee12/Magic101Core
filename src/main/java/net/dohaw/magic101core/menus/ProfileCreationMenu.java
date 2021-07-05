package net.dohaw.magic101core.menus;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.profiles.*;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.Constants;
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
import java.util.UUID;

public class ProfileCreationMenu extends Menu implements Listener {

    private ProfileCreationSession session;
    private boolean create;
    private int slotClicked;
    private ProfileSelectionMenu prevMenu;

    public ProfileCreationMenu(JavaPlugin plugin, ProfileCreationSession session, boolean create, int slotClicked, ProfileSelectionMenu prevMenu) {
        super(plugin, null, "Profile Creation", 45);
        this.session = session;
        JPUtils.registerEvents(this);
        this.create = create;
        this.slotClicked = slotClicked;
        this.prevMenu = prevMenu;
    }

    @Override
    public void initializeItems(Player player) {

        List<String> profileNameLore = new ArrayList<String>(){{
            add("&cCurrent Profile Name: &e" + session.getProfileName());
        }};

        inv.setItem(11, createGuiItem(Material.STICK, "&eChange Profile Name", profileNameLore));

        List<String> characterNameLore = new ArrayList<String>(){{
            add("&cCurrent Character Name: &e" + session.getCharacterName());
        }};

        inv.setItem(13, createGuiItem(Material.STICK, "&eChange Character Name", characterNameLore));

        if(create) {
            List<String> classNameLore = new ArrayList<String>() {{
                add("&cCurrent Class Selected: &e" + (session.getSchool() == null ? "None" : session.getSchool().toString()));
            }};
            inv.setItem(15, createGuiItem(Material.STICK, "&eChange Class", classNameLore));
        }

        inv.setItem(31, createGuiItem(Material.STICK, create ? "&eCreate Profile" : "&eSave", new ArrayList<>()));

        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        fillMenu(false);

    }

    public ProfileCreationSession getSession() {
        return session;
    }

    public void setSession(ProfileCreationSession session) {
        this.session = session;
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
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        //prof/char name select
        if(slotClicked == 11 || slotClicked == 13){
            ProfileCreationSessionPrompt.Change change = null;
            switch(slotClicked){
                case 11:
                    change = ProfileCreationSessionPrompt.Change.PROFILE_NAME;
                    break;
                case 13:
                    change = ProfileCreationSessionPrompt.Change.CHARACTER_NAME;
                    break;
            }
            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ProfileCreationSessionPrompt(change, session,this))
                    .withLocalEcho(false).buildConversation(player);;
            conv.begin();

            player.closeInventory();

        }
        //class selection
        else if(slotClicked == 15 && create) {
            Menu newMenu = new ClassSelectionMenu(plugin,this, session);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }
        //create
        else if(slotClicked == 31){
            UUID playerUUID = player.getUniqueId();
            List<Profile> profileList = ALL_PROFILES.ALL_PROFILES_MAP.get(playerUUID);
            if(create){
                if(session.getCharacterName().equals("Not Set") || session.getSchool() == null){
                    player.sendMessage("Please set a character name and a class.");
                    return;
                }
                if(ALL_PROFILES.getProfileByProfileName(playerUUID, session.getProfileName()) != null){
                    player.sendMessage("Your profile name must be different from all other profiles.");
                    return;
                }
                if(profileList == null){
                    profileList = new ArrayList<>();
                }
                profileList.add(new Profile(session.getProfileName(),session.getCharacterName(),session.getSchool(),
                                1,new Health(Constants.schoolToBaseHealth.get(session.getSchool())),session,
                        true));
                player.closeInventory();
                player.sendMessage(StringUtils.colorString("&bYou have created your class"));
                ALL_PROFILES.ALL_PROFILES_MAP.put(playerUUID,profileList);
            }
            else{
                Profile profile = ALL_PROFILES.ALL_PROFILES_MAP.get(playerUUID).get(this.slotClicked);
                profile.setSession(session);
                profile.setProfileName(session.getProfileName());
                profile.setCharacterName(session.getCharacterName());
                prevMenu.initializeItems(player);
                player.closeInventory();
                prevMenu.openInventory(player);
            }
        }

    }
}
