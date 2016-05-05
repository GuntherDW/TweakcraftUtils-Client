package be.guntherdw.minecraft.tcutilsclient.mixin;

import be.guntherdw.minecraft.tcutilsclient.LiteModTCUtilsClientMod;
import be.guntherdw.minecraft.tcutilsclient.handlers.TCUtilsClientModHandler;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author GuntherDW
 */
@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {

    protected MixinRenderLivingBase(RenderManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At("HEAD"), cancellable = true)
    private void onRenderName(T entity, double x, double y, double z, CallbackInfo ci) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return;
        }
        ci.cancel();

        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();
        String playerName = entity.getName();
        String nick = playerName;
        boolean hasNick = nh.hasNick(playerName);

        if (hasNick) {
            nick = nh.getNick(playerName);
        } else {
            nh.addAsked(playerName);
        }

        if (this.canRenderName(entity)) {
            double distanceSqToEntity = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float maxDistance = entity.isSneaking() ? 32.0F : 64.0F;
            if (distanceSqToEntity < (double) (maxDistance * maxDistance)) {
                // String lvt_11_1_ = entity.getDisplayName().getFormattedText();
                GlStateManager.alphaFunc(516, 0.1F);
                this.renderEntityName_tcutilsclient(entity, x, y, z, nick, distanceSqToEntity, 64);
            }
        }
    }

    private void renderEntityName_tcutilsclient(T entity, double x, double y, double z, String displayName, double distance, int maxDistance) {
        double lvt_10_1_ = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (lvt_10_1_ <= (double) (maxDistance * maxDistance)) {
            boolean lvt_12_1_ = entity.isSneaking();
            GlStateManager.pushMatrix();
            float lvt_13_1_ = lvt_12_1_ ? 0.25F : 0.0F;
            GlStateManager.translate((float) x, (float) y + entity.height + 0.5F - lvt_13_1_, (float) z);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.025F, -0.025F, 0.025F);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            if (!lvt_12_1_) {
                GlStateManager.disableDepth();
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int extraHeight = displayName.equals("deadmau5") ? -10 : 0;
            FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
            int width = fontRenderer.getStringWidth(displayName) / 2;
            GlStateManager.disableTexture2D();
            Tessellator tesselator = Tessellator.getInstance();
            VertexBuffer vertexBuffer = tesselator.getBuffer();
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vertexBuffer.pos((double) (-width - 1), (double) (-1 + extraHeight), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexBuffer.pos((double) (-width - 1), (double) (8 + extraHeight), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexBuffer.pos((double) (width + 1), (double) (8 + extraHeight), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexBuffer.pos((double) (width + 1), (double) (-1 + extraHeight), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tesselator.draw();
            GlStateManager.enableTexture2D();
            if (!lvt_12_1_) {
                fontRenderer.drawString(displayName, -fontRenderer.getStringWidth(displayName) / 2, extraHeight, 553648127);
                GlStateManager.enableDepth();
            }

            GlStateManager.depthMask(true);
            fontRenderer.drawString(displayName, -fontRenderer.getStringWidth(displayName) / 2, extraHeight, lvt_12_1_ ? 553648127 : -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }


}
