package be.guntherdw.minecraft.tcutilsclient.events;


import be.guntherdw.minecraft.tcutilsclient.LiteModTCUtilsClientMod;
import be.guntherdw.minecraft.tcutilsclient.settings.TCUtilsClientModConfig;
import com.mumfrey.liteloader.transformers.event.EventInfo;
import com.mumfrey.liteloader.transformers.event.ReturnEventInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

public class TCUtilsClientModEvents {

    public static void onGetLocationCape(ReturnEventInfo<NetworkPlayerInfo, ResourceLocation> returnEventInfo) {
        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();
        NetworkPlayerInfo source = returnEventInfo.getSource();
        String playerName = source.getGameProfile().getName();
        String playerNamel= playerName.toLowerCase();
        if(nh.getCapes().containsKey(playerNamel)) {
            // texmanager.

            String resourceurl = "capes/"+playerNamel;
            String url = nh.getCapes().get(playerNamel);

            ResourceLocation rl = new ResourceLocation(resourceurl);

            TextureManager tm = Minecraft.getMinecraft().getTextureManager();
            ITextureObject ito = tm.getTexture(rl);

            if (ito == null) {
                final ImageBufferDownload ibd = new ImageBufferDownload();

                ito = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {
                    @Override
                    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
                        // bufferedImage = ibd.parseUserSkin(bufferedImage);
                        // System.out.println("inside parseUserSkin");
                        return bufferedImage;
                    }

                    @Override
                    public void skinAvailable() {
                        ibd.skinAvailable();
                    }
                });



                tm.loadTexture(rl, ito);
                // EnumPlayerModelParts.CAPE;
                // var3.parseUserSkin();
                //((EntityPlayer)p_77043_1_).isWearing(EnumPlayerModelParts.CAPE))
                // EntityPlayer ep = source.getGameProfile();

            }

            returnEventInfo.setReturnValue(rl);
            returnEventInfo.cancel();
        }
    }

    /* TODO: No longer needed? */
    public static void onLivingUpdate(EventInfo<Entity> e) {

        /* Entity entity = e.getSource();
        if(!(entity instanceof EntityAgeable)) return;
        EntityAgeable ea = (EntityAgeable) entity;
        NicksHandler nh = LiteModChannelModPlugin.getInstance().getNicksHandler();

        Integer eid = entity.getEntityId();
        if(nh.getAnimals().containsKey(eid)) {
            TCUtilsAnimalInformation tcUtilsAnimalInformation = nh.getAnimals().get(eid);
            if(tcUtilsAnimalInformation.isAgeLock()) {
                e.cancel();
                ea.setGrowingAge(tcUtilsAnimalInformation.getAge());
            }
        } */
        // if(ea.getAge() < 0) {
        //    System.out.println(ea.getName()+" age : "+ea.getAge()+" growingAge : "+ea.getGrowingAge());
        // }
    }

    // public void passSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_)
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

                // if (entity.isSneaking()) {
                GlStateManager.pushMatrix();

                /* if(entity.isSneaking())
                    GlStateManager.translate((float) x, (float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
                else */
                // int addHeight = !entity.isSneaking() ? 1 : 0;


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
                        // GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glDepthMask(false);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);

                        boolean foggy = GL11.glIsEnabled(GL11.GL_FOG);
                        GL11.glDisable(GL11.GL_FOG);

                        GL11.glPushMatrix();

                        fontRenderer.drawString(nick, -width, 0, -1);

                        GL11.glPopMatrix();
                        if (foggy) {
                            GL11.glEnable(GL11.GL_FOG);
                        }
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(true);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        // GL11.glEnable(GL11.GL_TEXTURE_2D);
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


    // (Entity entityIn, String str, double x, double y, double z, int maxDistance)
    public static void onRenderLivingLabel(EventInfo<Render> e, Entity entityIn, String str, double x, double y, double z, int maxDistance) {
        String nick = str;
        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();

        if(entityIn instanceof EntityOtherPlayerMP) {
            String playerName = entityIn.getName();
            if (nh.hasNick(playerName)) {
                nick = nh.getNick(playerName);
            } else {
                nh.addAsked(playerName);
            }
            // System.out.println("hasNick(" + playerName + ") -> "+nick);
            // System.out.println(entityIn.getName()+"\tWearing cape? : "+((EntityPlayer)entityIn).isWearing(EnumPlayerModelParts.CAPE));
        }
        Render render = e.getSource();

        /* Original method */
        double var10 = entityIn.getDistanceSqToEntity(render.getRenderManager().livingPlayer);

        if (var10 <= (double) (maxDistance * maxDistance)) {
            FontRenderer var12 = render.getFontRendererFromRenderManager();
            float var13 = 1.6F;
            float var14 = 0.016666668F * var13;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-render.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(render.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-var14, -var14, var14);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator var15 = Tessellator.getInstance();
            WorldRenderer var16 = var15.getWorldRenderer();
            byte var17 = 0;

            if (nick.equals("deadmau5")) {
                var17 = -10;
            }

            GlStateManager.disableTexture2D();
            var16.startDrawingQuads();
            int var18 = var12.getStringWidth(nick) / 2;
            var16.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var16.addVertex((double) (-var18 - 1), (double) (-1 + var17), 0.0D);
            var16.addVertex((double) (-var18 - 1), (double) (8 + var17), 0.0D);
            var16.addVertex((double) (var18 + 1), (double) (8 + var17), 0.0D);
            var16.addVertex((double) (var18 + 1), (double) (-1 + var17), 0.0D);
            var15.draw();
            GlStateManager.enableTexture2D();
            var12.drawString(nick, -var12.getStringWidth(nick) / 2, var17, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            var12.drawString(nick, -var12.getStringWidth(nick) / 2, var17, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }

        e.cancel();
        // handler code here
    }
}
