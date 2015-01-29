package be.guntherdw.minecraft.tcutilsclient.settings;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.Buffer;

/**
 * Created by guntherdw on 19/01/15.
 */
public class TCUtilsClientModConfig {

    private File configFile;
    private Minecraft mcInstance;

    public static boolean fullBrightNames = false;
    public static boolean drawNamesWithShadow = false;
    public static boolean debugLogging = false;

    public TCUtilsClientModConfig(Minecraft mc) {
        this.mcInstance = mc;
        this.configFile = new File(mc.mcDataDir, "tcutilsclient.txt");
    }

    public void saveConfig() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(configFile));
            printWriter.println("tcutils-fullbrightnames:"+fullBrightNames);
            printWriter.println("tcutils-addshadowtonames:"+drawNamesWithShadow);
            printWriter.println("tcutils-debug:"+debugLogging);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        if(!configFile.exists()) saveConfig();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(":");
                if(split[0].equals("tcutils-fullbrightnames")) {
                    fullBrightNames = split[1].equalsIgnoreCase("true");
                }
                if (split[0].equals("tcutils-addshadowtonames")) {
                    drawNamesWithShadow = split[1].equalsIgnoreCase("true");
                }
                if (split[0].equals("tcutils-debug")) {
                    debugLogging = split[1].equalsIgnoreCase("true");
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
