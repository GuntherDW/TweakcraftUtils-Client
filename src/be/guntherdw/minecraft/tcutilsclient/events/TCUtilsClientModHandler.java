package be.guntherdw.minecraft.tcutilsclient.events;

import be.guntherdw.minecraft.tcutilsclient.LiteModTCUtilsClientMod;
import be.guntherdw.minecraft.tcutilsclient.packages.TCUtilsClientModAnimalInformation;
import net.minecraft.network.PacketBuffer;

import java.util.*;

/**
 * Created by guntherdw on 07/01/15.
 */
public class TCUtilsClientModHandler {

    private LiteModTCUtilsClientMod modInstance;

    public enum packetMode {NICK, CAPE, ANIMAL, OTHER}

    /* Temp collections */

    private List<String> asked;
    private List<String> toAsk;
    protected boolean canAsk = false;

    /* end Temp collections */

    private Map<String, String> nicks;
    private Map<String, String> capes;
    private Map<Integer, TCUtilsClientModAnimalInformation> animals;

    public TCUtilsClientModHandler(LiteModTCUtilsClientMod modInstance) {
        this.modInstance = modInstance;
        asked = new ArrayList<String>();
        toAsk = new ArrayList<String>();
        nicks = new HashMap<String, String>();
        capes = new HashMap<String, String>();
        animals = new HashMap<Integer, TCUtilsClientModAnimalInformation>();
    }

    public List<String> getAsked() {
        return asked;
    }

    public List<String> getToAsk() {
        return toAsk;
    }

    public void reInit() {
        toAsk.clear();
        asked.clear();
        nicks.clear();
        capes.clear();
        animals.clear();
        canAsk = false;
    }

    public void addAsked(String playername) {
        if (!canAsk) return;
        if (asked.contains(playername)) return;
        modInstance.log.debug("Adding toAsk for " + playername);
        asked.add(playername);
        toAsk.add(playername);

        modInstance.toggleAskPacketQuestion();
    }

    public void clearToAsk() {
        toAsk.clear();
    }

    public boolean hasAsked(String player) {
        return asked.contains(player);
    }

    public boolean hasNick(String player) {
        return nicks.get(player.toLowerCase()) != null;
    }

    public void addNick(String player, String nick) {
        nicks.put(player.toLowerCase(), nick);
    }

    public String getNick(String player) {
        return nicks.get(player.toLowerCase());
    }

    public Map<String, String> getCapes() {
        return capes;
    }

    public Map<Integer, TCUtilsClientModAnimalInformation> getAnimals() {
        return animals;
    }

    public void receivePacket(PacketBuffer data) {
        // 50-60 Nicks
        // 60-70 Capes
        // 70-80 Animals
        List<String> lines = new ArrayList<String>();
        byte[] bytes = data.array();
        packetMode pm = packetMode.OTHER;
        if (bytes.length > 0) {

            String line = "";
            byte controlByte = bytes[0];
            if (controlByte >= 50 && controlByte < 60) {
                pm = packetMode.NICK;
                if (!canAsk) canAsk = true;
                // System.out.println("Nick mode selected");
                if (controlByte == 50) nicks.clear();
            } else if (controlByte >= 60 && controlByte < 70) {
                pm = packetMode.CAPE;
                // System.out.println("Cape mode selected");
                if (controlByte == 60) capes.clear();
            } else if (controlByte >= 70 && controlByte < 80) {
                pm = packetMode.ANIMAL;
                // System.out.println("Animal mode selected");
                if (controlByte == 70) animals.clear();
            }

            byte b1;
            for (int i = 1; i <= bytes.length; i++) {
                try {
                    b1 = bytes[i];
                    if (b1 == (byte) 0) {
                        if (pm == packetMode.NICK)
                            lines.add(line.replace("&c", "§"));
                        else
                            lines.add(line);
                        line = "";
                    } else {
                        line += (char) b1;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    break;
                }
            }

            switch (pm) {
                case NICK:
                    parseLines(lines);
                    break;
                case CAPE:
                    parseCapes(lines);
                    break;
                case ANIMAL:
                    parseAnimals(lines);
                    break;

            }
            // this.parseLines(lines);
        }
    }

    private void parseCapes(List<String> lines) {
        for (String line : lines) {
            String[] split = line.split(",");
            if (split.length == 2) {
                if (split[1].equals("null"))
                    capes.remove(split[0]);
                else {
                    // System.out.println("Adding cape for "+split[0]+ " (url : "+split[1]+")");
                    capes.put(split[0].toLowerCase(), split[1]);
                }
            } else {
                modInstance.log.debug("unknown line (" + split.length + ") : " + line);
            }
        }
    }

    private void parseAnimals(List<String> lines) {
        for (String line : lines) {
            String[] split = line.split(",");
            if (split.length == 3) {
                Integer entityId = Integer.parseInt(split[0]);
                Integer age = Integer.parseInt(split[1]);
                Boolean ageLock = Boolean.parseBoolean(split[2]);
                TCUtilsClientModAnimalInformation animalInfo = new TCUtilsClientModAnimalInformation(entityId, age, ageLock);
                animals.put(entityId, animalInfo);
            }
        }
    }

    private void parseLines(List<String> lines) {
        for (String line : lines) {
            String[] split = line.split(",");
            if (split.length == 2) {
                if (split[1].equals("null"))
                    nicks.remove(split[0]);
                else {
                    modInstance.log.debug("Received nick for " + split[0] + " -> " + split[1]);
                    addNick(split[0], split[1]);
                }
            }
        }
    }
}
