package be.guntherdw.minecraft.tcutilsclient.events.obf;

import com.mumfrey.liteloader.core.runtime.Obf;

/**
 * Created by guntherdw on 07/01/15.
 */
public class TCUtilsClientModObf extends Obf {

    public static final TCUtilsClientModObf Rendercls                   = new TCUtilsClientModObf("net.minecraft.client.renderer.entity.Render",               "cpu");
    public static final TCUtilsClientModObf Entitycls                   = new TCUtilsClientModObf("net.minecraft.entity.Entity",                               "wv");
    public static final TCUtilsClientModObf EntityAgeablecls            = new TCUtilsClientModObf("net.minecraft.entity.EntityAgeable",                        "ws");
    public static final TCUtilsClientModObf NetworkPlayerInfocls        = new TCUtilsClientModObf("net.minecraft.client.network.NetworkPlayerInfo",            "ces");
    public static final TCUtilsClientModObf ResourceLocationcls         = new TCUtilsClientModObf("net.minecraft.util.ResourceLocation",                       "oa");
    public static final TCUtilsClientModObf RenderLivingEntitycls       = new TCUtilsClientModObf("net.minecraft.client.renderer.entity.RendererLivingEntity", "cqv");
    public static final TCUtilsClientModObf EntityLivingBasecls         = new TCUtilsClientModObf("net.minecraft.entity.EntityLivingBase",                     "xm");

    public static final TCUtilsClientModObf RenderLivingLabelmthd       = new TCUtilsClientModObf("func_147906_a",  "a",    "renderLivingLabel");
    public static final TCUtilsClientModObf onLivingUpdatemthd          = new TCUtilsClientModObf("func_70636_d",   "m",    "onLivingUpdate");
    public static final TCUtilsClientModObf getLocationCapemthd         = new TCUtilsClientModObf("func_178837_g",  "h",    "getLocationCape");
    public static final TCUtilsClientModObf passSpecialRenderermthd     = new TCUtilsClientModObf("func_77033_b",   "b",    "passSpecialRender");
    public static final TCUtilsClientModObf canRenderNamemthd           = new TCUtilsClientModObf("func_177070_b",  "b",    "canRenderName");
    public static final TCUtilsClientModObf renderOffsetLivingLabelmthd = new TCUtilsClientModObf("func_177069_a",  "a",    "renderOffsetLivingLabel");
    // getLocationCape




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
