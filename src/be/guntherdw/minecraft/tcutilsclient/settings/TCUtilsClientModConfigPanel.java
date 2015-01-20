package be.guntherdw.minecraft.tcutilsclient.settings;

import be.guntherdw.minecraft.tcutilsclient.LiteModTCUtilsClientMod;
import com.mumfrey.liteloader.client.gui.GuiCheckbox;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guntherdw on 19/01/15.
 */
public class TCUtilsClientModConfigPanel extends Gui implements ConfigPanel {

    private static final int CONTROL_SPACING = 12;

    private Minecraft mc;
    private List<GuiButton> controlList = new ArrayList<GuiButton>();
    private LiteModTCUtilsClientMod mod;
    private TCUtilsClientModConfig config;
    private GuiButton activeControl;

    public TCUtilsClientModConfigPanel() {
        this.mc = Minecraft.getMinecraft();
    }


    /**
     * Panels should return the text to display at the top of the config panel window
     */
    @Override
    public String getPanelTitle() {
        return "TweakcraftUtils ClientMod settings";
    }

    /**
     * Get the height of the content area for scrolling purposes, return -1 to disable scrolling
     */
    @Override
    public int getContentHeight() {
        return controlList.size() * CONTROL_SPACING;
    }

    private void addOption(int id, int x, int y, boolean checked, String text) {
        GuiCheckbox cb = new GuiCheckbox(id, x, y, text);
        cb.checked = checked;
        controlList.add(cb);
    }

    /**
     * Called when the panel is displayed, initialise the panel (read settings, etc)
     *
     * @param host panel host
     */
    @Override
    public void onPanelShown(ConfigPanelHost host) {
        this.mod = host.getMod();
        this.config = mod.getTCUtilsClientModConfig();
        this.config.loadConfig();

        int id = 0;
        int spot = 0;

        this.controlList.clear();
        int top = 13;

        addOption(0, 24, 13,                   TCUtilsClientModConfig.fullBrightNames,     "Fullbright names");
        addOption(1, 24, 13 + CONTROL_SPACING, TCUtilsClientModConfig.drawNamesWithShadow, "Add shadow backing to names");
    }

    /**
     * Called when the window is resized whilst the panel is active
     *
     * @param host panel host
     */
    @Override
    public void onPanelResize(ConfigPanelHost host) {

    }

    /**
     * Called when the panel is closed, panel should save settings
     */
    @Override
    public void onPanelHidden() {
        this.config.saveConfig();
    }

    /**
     * Called every tick
     *
     * @param host
     */
    @Override
    public void onTick(ConfigPanelHost host) {

    }

    /**
     * Draw the configuration panel
     *
     * @param host
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    @Override
    public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks) {
        for (GuiButton control : this.controlList) {
            control.drawButton(this.mc, mouseX, mouseY);
        }
    }

    /**
     * Called when a mouse button is pressed
     *
     * @param host
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
        boolean makeActive = true;

        for (GuiButton control : this.controlList) {
            if (control.mousePressed(this.mc, mouseX, mouseY)) {
                if (makeActive) {
                    makeActive = false;
                    this.activeControl = control;
                    this.actionPerformed(control);
                }
            }
        }
    }

    private void actionPerformed(GuiButton control) {
        if (control instanceof GuiCheckbox) {
            GuiCheckbox chk = (GuiCheckbox) control;
            chk.checked = !chk.checked;
            if(control.id == 0)
                TCUtilsClientModConfig.fullBrightNames = chk.checked;
            if(control.id == 1)
                TCUtilsClientModConfig.drawNamesWithShadow = chk.checked;

        }
    }

    /**
     * Called when a mouse button is released
     *
     * @param host
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    public void mouseReleased(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
        if (this.activeControl != null) {
            this.activeControl.mouseReleased(mouseX, mouseY);
            this.activeControl = null;
        }
    }

    /**
     * Called when the mouse is moved
     *
     * @param host
     * @param mouseX
     * @param mouseY
     */
    @Override
    public void mouseMoved(ConfigPanelHost host, int mouseX, int mouseY) {

    }

    /**
     * Called when a key is pressed
     *
     * @param host
     * @param keyChar
     * @param keyCode
     */
    @Override
    public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            host.close();
            return;
        }
    }
}
