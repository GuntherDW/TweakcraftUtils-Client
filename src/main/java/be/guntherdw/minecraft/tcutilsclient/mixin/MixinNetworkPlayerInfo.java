package be.guntherdw.minecraft.tcutilsclient.mixin;

import be.guntherdw.minecraft.tcutilsclient.LiteModTCUtilsClientMod;
import be.guntherdw.minecraft.tcutilsclient.handlers.TCUtilsClientModHandler;
import be.guntherdw.minecraft.tcutilsclient.handlers.TCUtilsClientModSkinsParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.image.BufferedImage;

/**
 * @author GuntherDW
 */
@Mixin(NetworkPlayerInfo.class)
public abstract class MixinNetworkPlayerInfo {

    @Shadow abstract GameProfile getGameProfile();

    @Inject(method = "getLocationCape()Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void onGetLocationCape(CallbackInfoReturnable<ResourceLocation> ci) {
        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();
        String playerName = getGameProfile().getName();
        if (nh.getCapes().containsKey(playerName)) {
            String resourceurl = "tcutils:capes/" + playerName.toLowerCase();
            String url = nh.getCapes().get(playerName);
            ResourceLocation rl = new ResourceLocation(resourceurl);
            TextureManager tm = Minecraft.getMinecraft().getTextureManager();
            ITextureObject ito = tm.getTexture(rl);
            final TCUtilsClientModSkinsParser tcUtilsClientModSkinsParser = new TCUtilsClientModSkinsParser();

            if (ito == null) {
                LiteModTCUtilsClientMod.getInstance().log.info("Getting new cape for " + playerName + " from " + url);
                ito = new ThreadDownloadImageData(null, url, null, tcUtilsClientModSkinsParser.returnCapeIImageBuffer());
                tm.loadTexture(rl, ito);
            }

            ci.setReturnValue(rl);
            ci.cancel();
        }
    }

    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void onGetLocationSkin(CallbackInfoReturnable<ResourceLocation> ci) {

        TCUtilsClientModHandler nh = LiteModTCUtilsClientMod.getInstance().getNicksHandler();
        String playerName = getGameProfile().getName();

        if (nh.getSkins().containsKey(playerName)) {
            String resourceurl = "tcutils:hdskins/" + playerName.toLowerCase();
            String url = nh.getSkins().get(playerName);
            ResourceLocation rl = new ResourceLocation(resourceurl);
            TextureManager tm = Minecraft.getMinecraft().getTextureManager();
            final TCUtilsClientModSkinsParser tcUtilsClientModSkinsParser = new TCUtilsClientModSkinsParser();
            // final ImageBufferDownload imageBufferDownload = new ImageBufferDownload();
            ITextureObject ito = tm.getTexture(rl);

            if (ito == null) {
                LiteModTCUtilsClientMod.getInstance().log.info("Getting new skin for " + playerName + " from " + url);

                ito = new ThreadDownloadImageData(null, url, DefaultPlayerSkin.getDefaultSkinLegacy(), TCUtilsClientModSkinsParser.returnSkinIImageBuffer(tcUtilsClientModSkinsParser));
                tm.loadTexture(rl, ito);
            }

            ci.setReturnValue(rl);
            ci.cancel();
        }

    }



}
