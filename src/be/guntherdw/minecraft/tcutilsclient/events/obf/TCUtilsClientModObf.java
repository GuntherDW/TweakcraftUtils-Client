package be.guntherdw.minecraft.tcutilsclient.events.obf;

import com.mumfrey.liteloader.core.runtime.Obf;

/**
 * Created by guntherdw on 07/01/15.
 */
public class TCUtilsClientModObf extends Obf {

    public static final TCUtilsClientModObf NetworkPlayerInfocls        = new TCUtilsClientModObf("net.minecraft.client.network.NetworkPlayerInfo",            "ces");
    public static final TCUtilsClientModObf ResourceLocationcls         = new TCUtilsClientModObf("net.minecraft.util.ResourceLocation",                       "oa");
    public static final TCUtilsClientModObf RenderLivingEntitycls       = new TCUtilsClientModObf("net.minecraft.client.renderer.entity.RendererLivingEntity", "cqv");
    public static final TCUtilsClientModObf EntityLivingBasecls         = new TCUtilsClientModObf("net.minecraft.entity.EntityLivingBase",                     "xm");

    public static final TCUtilsClientModObf getLocationSkinmthd         = new TCUtilsClientModObf("func_110306_p",  "i",    "getLocationSkin");
    public static final TCUtilsClientModObf getLocationCapemthd         = new TCUtilsClientModObf("func_178837_g",  "h",    "getLocationCape");
    public static final TCUtilsClientModObf passSpecialRenderermthd     = new TCUtilsClientModObf("func_77033_b",   "b",    "passSpecialRender");
    public static final TCUtilsClientModObf canRenderNamemthd           = new TCUtilsClientModObf("func_110813_b",  "a",    "canRenderName");

    /**
     * @param seargeName
     * @param obfName
     */
    protected TCUtilsClientModObf(String seargeName, String obfName) {
        super(seargeName, obfName, seargeName);
    }

    /**
     * @param seargeName
     * @param obfName
     * @param mcpName
     */
    protected TCUtilsClientModObf(String seargeName, String obfName, String mcpName) {
        super(seargeName, obfName, mcpName);
    }
}
