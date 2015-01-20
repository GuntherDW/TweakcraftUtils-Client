package be.guntherdw.minecraft.tcutilsclient.events;


import be.guntherdw.minecraft.tcutilsclient.LiteModTCUtilsClientMod;
import be.guntherdw.minecraft.tcutilsclient.events.obf.PrivateMethods;
import be.guntherdw.minecraft.tcutilsclient.settings.TCUtilsClientModConfig;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.transformers.event.EventInfo;
import com.mumfrey.liteloader.transformers.event.ReturnEventInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

public class TCUtilsClientModEvents {

    public static void onGetLocationCape(ReturnEventInfo<NetworkPlayerInfo, ResourceLocation> returnEventInfo) {
        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();
        NetworkPlayerInfo source = returnEventInfo.getSource();
        String playerName = source.getGameProfile().getName();
        if(nh.getCapes().containsKey(playerName)) {
            String resourceurl = "tcutils:capes/"+playerName.toLowerCase();
            String url = nh.getCapes().get(playerName);
            ResourceLocation rl = new ResourceLocation(resourceurl);
            TextureManager tm = Minecraft.getMinecraft().getTextureManager();
            ITextureObject ito = tm.getTexture(rl);

            if (ito == null) {
                LiteModTCUtilsClientMod.getInstance().log.debug("Getting new cape for " + playerName + " from " + url);
                ito = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {
                    @Override
                    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
                        return bufferedImage;
                    }
                    @Override
                    public void skinAvailable() {
                        /* ibd.skinAvailable(); */
                    }
                });
                tm.loadTexture(rl, ito);
            }

            returnEventInfo.setReturnValue(rl);
            returnEventInfo.cancel();
        }
    }

    public static void onGetLocationSkin(ReturnEventInfo<NetworkPlayerInfo, ResourceLocation> returnEventInfo) {
        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();
        NetworkPlayerInfo source = returnEventInfo.getSource();
        String playerName = source.getGameProfile().getName();
        if (nh.getSkins().containsKey(playerName)) {
            String resourceurl = "tcutils:hdskins/" + playerName.toLowerCase();
            String url = nh.getSkins().get(playerName);
            ResourceLocation rl = new ResourceLocation(resourceurl);
            TextureManager tm = Minecraft.getMinecraft().getTextureManager();
            final TCUtilsClientModSkinsParser tcUtilsClientModSkinsParser = new TCUtilsClientModSkinsParser();
            final ImageBufferDownload imageBufferDownload = new ImageBufferDownload();
            ITextureObject ito = tm.getTexture(rl);

            if(ito == null) {
                LiteModTCUtilsClientMod.getInstance().log.debug("Getting new skin for " + playerName + " from " + url);
                ito = new ThreadDownloadImageData(null, url, DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
                    @Override
                    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
                        bufferedImage = tcUtilsClientModSkinsParser.parseUserSkin(bufferedImage);
                        return bufferedImage;
                    }

                    @Override
                    public void skinAvailable() {
                        tcUtilsClientModSkinsParser.skinAvailable();
                    }
                });
                tm.loadTexture(rl, ito);
                // source.getSkinType()
            }


            returnEventInfo.setReturnValue(rl);
            returnEventInfo.cancel();
        }
    }

    public static void onPassSpecialRender(EventInfo<RendererLivingEntity> e, EntityLivingBase entity, double x, double y, double z) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return;
        }
        RendererLivingEntity source = e.getSource();
        e.cancel();

        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();
        String playerName = entity.getName();
        String nick = playerName;
        boolean hasNick = nh.hasNick(playerName);

        if (hasNick) {
            nick = nh.getNick(playerName);
        } else {
            nh.addAsked(playerName);
        }
        // this.renderOffsetLivingLabel(p_77033_1_, p_77033_2_, p_77033_4_ - (p_77033_1_.isChild() ? (double)(p_77033_1_.height / 2.0F) : 0.0D), p_77033_6_, var11, 0.02666667F, var8);

        if (PrivateMethods.canRenderName.invoke(source, entity)) {

            boolean sneaking = entity.isSneaking();

            double var8 = entity.getDistanceSqToEntity(source.getRenderManager().livingPlayer);
            float maxDistance = sneaking ? 32.0F : 64.0F;

            if (var8 < (double) (maxDistance * maxDistance)) {

                GlStateManager.alphaFunc(516, 0.1F);
                FontRenderer fontRenderer = source.getFontRendererFromRenderManager();
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) x, (float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0D), (float) z);

                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-source.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(source.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
                GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
                if(sneaking) GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                worldRenderer.startDrawingQuads();
                int width = fontRenderer.getStringWidth(nick) / 2;
                worldRenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                worldRenderer.addVertex((double) (-width - 1), -1, 0);
                worldRenderer.addVertex((double) (-width - 1), 8, 0);
                worldRenderer.addVertex((double) (width + 1), 8, 0);
                worldRenderer.addVertex((double) (width + 1), -1, 0);
                tessellator.draw();
                GlStateManager.enableTexture2D();
                if (sneaking) {
                    GlStateManager.depthMask(true);
                    fontRenderer.drawString(nick, -width, 0, 553648127);
                } else {
                    fontRenderer.drawString(nick, -width, 0, 553648127);
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);

                    if(hasNick && TCUtilsClientModConfig.fullBrightNames) {
                        GL11.glPushMatrix();
                        RenderHelper.disableStandardItemLighting();
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        // GL11.glDepthMask(false);
                        // GL11.glDisable(GL11.GL_DEPTH_TEST);

                        boolean foggy = GL11.glIsEnabled(GL11.GL_FOG);
                        GL11.glDisable(GL11.GL_FOG);

                        GL11.glPushMatrix();

                        if(TCUtilsClientModConfig.drawNamesWithShadow)
                            fontRenderer.drawStringWithShadow(nick, -width, 0, -1);
                        else
                            fontRenderer.drawString(nick, -width, 0, -1);

                        GL11.glPopMatrix();
                        if (foggy) {
                            GL11.glEnable(GL11.GL_FOG);
                        }
                        // GL11.glEnable(GL11.GL_DEPTH_TEST);
                        // GL11.glDepthMask(true);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_BLEND);

                        RenderHelper.enableStandardItemLighting();
                        GL11.glPopMatrix();
                    } else {
                        fontRenderer.drawString(nick, -width, 0, -1);
                    }
                }
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();

            }
        }
    }
}
