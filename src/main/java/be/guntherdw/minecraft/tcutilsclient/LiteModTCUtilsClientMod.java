package be.guntherdw.minecraft.tcutilsclient;

import be.guntherdw.minecraft.tcutilsclient.handlers.TCUtilsClientModHandler;
import be.guntherdw.minecraft.tcutilsclient.settings.TCUtilsClientModConfig;
import be.guntherdw.minecraft.tcutilsclient.settings.TCUtilsClientModConfigPanel;
import com.google.common.collect.ImmutableList;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.PluginChannelListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.util.log.LiteLoaderLogger;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketJoinGame;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;



/**
 * Created by guntherdw on 06/01/15.
 */
public class LiteModTCUtilsClientMod implements PluginChannelListener, Configurable, JoinGameListener, Tickable {

    private List<String> channels;
    private final String IMPROVCHAT_CHANNEL = "ImprovedChat";
    private final String TCNICK_CHANNEL = "TCUtils:nick";

    protected static String curChannel = null;

    protected Minecraft mcInstance;
    public Logger log;

    private boolean checked = false;
    protected boolean helloSent = false;
    private boolean shouldSendPackets = false;

    private int delayedHelo = 0;
    private int delayedAsk = 0;
    private static final int DELAYED_HELO_TICKS = 10;
    private static final int DELAYED_ASK_TICKS = 20;

    private TCUtilsClientModHandler nicksHandler;
    private TCUtilsClientModConfig tcUtilsClientModConfig;
    protected static LiteModTCUtilsClientMod instance;


    private boolean sendRegisterPacket = false;

    public LiteModTCUtilsClientMod() {
        this.mcInstance = Minecraft.getMinecraft();
        this.channels = new ArrayList<String>();
        channels.addAll(Arrays.asList(new String[]{IMPROVCHAT_CHANNEL, TCNICK_CHANNEL}));
        this.log = LiteLoaderLogger.getLogger();

        nicksHandler = new TCUtilsClientModHandler(this);
        instance = this;
    }

    public static LiteModTCUtilsClientMod getInstance() {
        return instance;
    }

    public TCUtilsClientModHandler getNicksHandler() {
        return nicksHandler;
    }

    public void toggleAskPacketQuestion() {
        if(delayedAsk == 0) { delayedAsk = DELAYED_ASK_TICKS; shouldSendPackets = true; }
    }

    /**
     * Called on join game
     *
     * @param netHandler     Net handler
     * @param joinGamePacket Join game packet
     * @param serverData     ServerData object representing the server being connected to
     * @param realmsServer   If connecting to a realm, a reference to the RealmsServer object
     */
    @Override
    public void onJoinGame(INetHandler netHandler, SPacketJoinGame joinGamePacket, ServerData serverData, RealmsServer realmsServer) {
        log.info("New server/game joined, sending new getDump packets in 10 gameTicks.");
        curChannel = null;
        sendRegisterPacket = true;
        delayedHelo = DELAYED_HELO_TICKS;
        checked = false;
        nicksHandler.reInit();
    }

    public CPacketCustomPayload getImprovChatRegisterPacket() {
        byte[] data = new byte[1];
        data[0] = (byte) 26;

        PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
        pb.writeBytes(data);

        return new CPacketCustomPayload(IMPROVCHAT_CHANNEL, pb);
    }

    public CPacketCustomPayload getTCUtilsNickRegisterPacket() {
        byte[] data = new byte[1];
        data[0] = (byte) 51;

        PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
        pb.writeBytes(data);

        return new CPacketCustomPayload(TCNICK_CHANNEL, pb);
    }

    public CPacketCustomPayload getTCUtilsNickRequestNickPacket(String playername) {

        PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
        pb.writeByte((byte) 53);
        try {
            pb.writeBytes(playername.getBytes("UTF-8"));
        } catch (Exception e) { }
        return new CPacketCustomPayload(TCNICK_CHANNEL, pb);
    }

    public CPacketCustomPayload getTCUtilsNickRequestNickPacket(List<String> list) {

        PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
        pb.writeByte((byte) 54);
        try {
            for(String playername : list) {
                pb.writeBytes(playername.getBytes("UTF-8"));
                pb.writeByte((byte) 0);
            }
        } catch (Exception e) { }
        return new CPacketCustomPayload(TCNICK_CHANNEL, pb);
    }

    /**
     * Get the mod version string
     *
     * @return the mod version as a string
     */
    @Override
    public String getVersion() {
        String version = LiteLoader.getInstance().getModMetaData(this, "version", "");
        String build = LiteLoader.getInstance().getModMetaData(this, "revision", "");

        return version + (!(build.equals("")) ? " (build: "+build+")" : "");
    }

    /**
     * Do startup stuff here, minecraft is not fully initialised when this function is called so mods *must not*
     * interact with minecraft in any way here
     *
     * @param configPath Configuration path to use
     */
    @Override
    public void init(File configPath) {
        tcUtilsClientModConfig = new TCUtilsClientModConfig(mcInstance);
        tcUtilsClientModConfig.loadConfig();
        if(TCUtilsClientModConfig.debugLogging)
            log.setLevel(Level.DEBUG);
    }

    public TCUtilsClientModConfig getTCUtilsClientModConfig() {
        return tcUtilsClientModConfig;
    }

    /**
     * Called when the loader detects that a version change has happened since this mod was last loaded
     *
     * @param version       new version
     * @param configPath    Path for the new version-specific config
     * @param oldConfigPath Path for the old version-specific config
     */
    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {

    }

    /**
     * Get the display name
     *
     * @return display name
     */
    @Override
    public String getName() {
        return "TCUtils-Clientmod";
        // return
    }

    /**
     * Called when a custom payload packet arrives on a channel this mod has registered
     *
     * @param channel Channel on which the custom payload was received
     * @param data    Custom payload data
     */
    @Override
    public void onCustomPayload(String channel, PacketBuffer data) {
        // System.out.println("Receveid packet for channel "+channel);
        if (channel.equals(IMPROVCHAT_CHANNEL)) {
            byte[] packetBytes = data.array();
            if (packetBytes.length > 0) {
                if (packetBytes[0] == (byte) 25) {
                    try {
                        String chatModeString = "";
                        byte[] cleanData = new byte[packetBytes.length - 1];
                        System.arraycopy(packetBytes, 1, cleanData, 0, packetBytes.length - 1);
                        chatModeString = new String(cleanData, "UTF-8").replaceAll("&c", "§");

                        if (chatModeString.equals("null")) {
                            curChannel = null;
                        } else {
                            curChannel = chatModeString;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        curChannel = null;
                    }
                }
            }
        } else if(channel.equals(TCNICK_CHANNEL)) {
            // System.out.println("Received packet for nicks!");
            nicksHandler.receivePacket(data);
        }

    }

    /**
     * Return a list of the plugin channels the mod wants to register
     *
     * @return plugin channel names as a list, it is recommended to use {@link ImmutableList#of} for this purpose
     */
    @Override
    public List<String> getChannels() {
        return Arrays.asList(channels.toArray(new String[0]));
    }

    public int getStringWidth_stripColor(String str) {
        char COLOR_CHAR = '\u00A7';
        Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");
        return getStringWidth(STRIP_COLOR_PATTERN.matcher(str).replaceAll(""));
    }

    public int getStringWidth(String str) {
        return getFontRenderer().getStringWidth(str);
    }

    public FontRenderer getFontRenderer() {
        return mcInstance.fontRendererObj;
    }

    public boolean shouldTick() {
        return LiteLoader.getClientPluginChannels().isRemoteChannelRegistered(IMPROVCHAT_CHANNEL)
            || LiteLoader.getClientPluginChannels().isRemoteChannelRegistered(TCNICK_CHANNEL);
    }

    public String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            sb.append(next);
            if(iterator.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    /**
     * Called every frame
     *
     * @param minecraft    Minecraft instance
     * @param partialTicks Partial tick value
     * @param inGame       True if in-game, false if in the menu
     * @param clock        True if this is a new tick, otherwise false if it's a regular frame
     */
    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
        if(!inGame) return;

        if (mcInstance.ingameGUI.getChatGUI().getChatOpen() && curChannel != null) {
            ScaledResolution scaledResolution = new ScaledResolution(mcInstance);
            int x1, x2;
            int y1, y2;

            x1 = /* scaledResolution.getScaledWidth() - */ 4;
            x2 = x1 + getStringWidth_stripColor(curChannel);
            y1 = scaledResolution.getScaledHeight() - 16;
            y2 = y1 - getFontRenderer().FONT_HEIGHT;

            Gui.drawRect(x1 - 2, y2 - 1, x2 + 2, y1 + 1, 0x77000000);
            getFontRenderer().drawStringWithShadow(curChannel, x1, y2, 0xFFFFFF);
        }

        if(!checked) {
            log.info("Is improvedchat enabled serverside? : " + LiteLoader.getClientPluginChannels().isRemoteChannelRegistered(IMPROVCHAT_CHANNEL));
            log.info("Is tcutils enabled serverside? : " + LiteLoader.getClientPluginChannels().isRemoteChannelRegistered(TCNICK_CHANNEL));
            checked = true;
        }

        if(!shouldTick()) return;
        if(!clock) return;

        if(sendRegisterPacket) {
            if(delayedHelo == 0) {
                if(LiteLoader.getClientPluginChannels().isRemoteChannelRegistered(IMPROVCHAT_CHANNEL)) {
                    Minecraft.getMinecraft().getConnection().sendPacket(getImprovChatRegisterPacket());
                } else {
                    log.warn("Not sending improvedchat 'getDump' because it is not enabled serverside!");
                }
                if(LiteLoader.getClientPluginChannels().isRemoteChannelRegistered(TCNICK_CHANNEL)) {
                    Minecraft.getMinecraft().getConnection().sendPacket(getTCUtilsNickRegisterPacket());
                } else {
                    log.warn("Not sending tcutils 'getDump' because it is not enabled serverside!");
                }
                sendRegisterPacket = false;
                helloSent = true;
            } else {
                delayedHelo--;
            }
        }

        if(shouldSendPackets) {
            if(delayedAsk == 0) {
                log.info("Asking for nicks (" + listToString(nicksHandler.getToAsk()) + ")");
                Minecraft.getMinecraft().getConnection().sendPacket(getTCUtilsNickRequestNickPacket(nicksHandler.getToAsk()));
                nicksHandler.clearToAsk();
                shouldSendPackets = false;
            } else {
                delayedAsk--;
            }
        }
    }

    /**
     * Get the class of the configuration panel to use, the returned class must have a
     * default (no-arg) constructor
     *
     * @return configuration panel class
     */
    @Override
    public Class<? extends ConfigPanel> getConfigPanelClass() {
        return TCUtilsClientModConfigPanel.class;
    }
}
