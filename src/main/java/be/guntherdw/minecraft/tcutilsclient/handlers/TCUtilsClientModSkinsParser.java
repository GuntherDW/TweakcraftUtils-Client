package be.guntherdw.minecraft.tcutilsclient.handlers;

import be.guntherdw.minecraft.tcutilsclient.LiteModTCUtilsClientMod;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class TCUtilsClientModSkinsParser implements IImageBuffer {
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;
    private int realimageWidth;
    private int realimageHeight;
    private int multiplier = 1;

    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {

        LiteModTCUtilsClientMod modInstance = LiteModTCUtilsClientMod.getInstance();

        if (bufferedImage == null) {
            modInstance.log.debug("bufferedImage was null!");
            return null;
        } else {
            multiplier = bufferedImage.getWidth() / 64;
            modInstance.log.debug("Image multiplier detected as " + multiplier);
            modInstance.log.debug("Image size : " + bufferedImage.getWidth() + "x" + bufferedImage.getHeight());
            boolean imageInOldFormat = bufferedImage.getWidth() != bufferedImage.getHeight();
            if(imageInOldFormat) modInstance.log.debug("Skin was in old format!");
            if(bufferedImage.getWidth() % 64 != 0) {
                modInstance.log.debug("Skin was not cleanly dividable by 64, quitting!");
                return null;
            }

            this.imageWidth  = 64 * multiplier;
            this.imageHeight = 64 * multiplier;

            BufferedImage bufferedImage_temp = new BufferedImage(this.imageWidth, this.imageHeight, 2);
            Graphics graphics = bufferedImage_temp.getGraphics();
            graphics.drawImage(bufferedImage, 0, 0, null);

            if (imageInOldFormat) {
                graphics.drawImage(bufferedImage_temp, 24 * multiplier, 48 * multiplier, 20 * multiplier, 52 * multiplier,  4 * multiplier, 16 * multiplier,  8 * multiplier, 20 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 28 * multiplier, 48 * multiplier, 24 * multiplier, 52 * multiplier,  8 * multiplier, 16 * multiplier, 12 * multiplier, 20 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 20 * multiplier, 52 * multiplier, 16 * multiplier, 64 * multiplier,  8 * multiplier, 20 * multiplier, 12 * multiplier, 32 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 24 * multiplier, 52 * multiplier, 20 * multiplier, 64 * multiplier,  4 * multiplier, 20 * multiplier,  8 * multiplier, 32 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 28 * multiplier, 52 * multiplier, 24 * multiplier, 64 * multiplier,  0             , 20 * multiplier,  4 * multiplier, 32 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 32 * multiplier, 52 * multiplier, 28 * multiplier, 64 * multiplier, 12 * multiplier, 20 * multiplier, 16 * multiplier, 32 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 40 * multiplier, 48 * multiplier, 36 * multiplier, 52 * multiplier, 44 * multiplier, 16 * multiplier, 48 * multiplier, 20 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 44 * multiplier, 48 * multiplier, 40 * multiplier, 52 * multiplier, 48 * multiplier, 16 * multiplier, 52 * multiplier, 20 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 36 * multiplier, 52 * multiplier, 32 * multiplier, 64 * multiplier, 48 * multiplier, 20 * multiplier, 52 * multiplier, 32 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 40 * multiplier, 52 * multiplier, 36 * multiplier, 64 * multiplier, 44 * multiplier, 20 * multiplier, 48 * multiplier, 32 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 44 * multiplier, 52 * multiplier, 40 * multiplier, 64 * multiplier, 40 * multiplier, 20 * multiplier, 44 * multiplier, 32 * multiplier, null);
                graphics.drawImage(bufferedImage_temp, 48 * multiplier, 52 * multiplier, 44 * multiplier, 64 * multiplier, 52 * multiplier, 20 * multiplier, 56 * multiplier, 32 * multiplier, null);
            }
            graphics.dispose();

            this.imageData = ((DataBufferInt) bufferedImage_temp.getRaster().getDataBuffer()).getData();

            this.setAreaOpaque     ( 0,                0,              32 * multiplier, 16 * multiplier);
            this.setAreaTransparent(32 * multiplier,   0,              64 * multiplier, 32 * multiplier);
            this.setAreaOpaque     ( 0,               16 * multiplier, 64 * multiplier, 32 * multiplier);
            this.setAreaTransparent( 0,               32 * multiplier, 16 * multiplier, 48 * multiplier);
            this.setAreaTransparent(16,               32 * multiplier, 40 * multiplier, 48 * multiplier);
            this.setAreaTransparent(40,               32 * multiplier, 56 * multiplier, 48 * multiplier);
            this.setAreaTransparent( 0,               48 * multiplier, 16 * multiplier, 64 * multiplier);
            this.setAreaOpaque     (16 * multiplier,  48 * multiplier, 48 * multiplier, 64 * multiplier);
            this.setAreaTransparent(48 * multiplier,  48 * multiplier, 64 * multiplier, 64 * multiplier);
            return bufferedImage_temp;
        }
    }

    public void skinAvailable() {
    }

    /*
     * Temporary fixes because Mixin can't handle internal classes....
     */
    public static IImageBuffer returnSkinIImageBuffer(final TCUtilsClientModSkinsParser instance) {

        IImageBuffer ib = new IImageBuffer() {
            @Override
            public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
                bufferedImage = instance.parseUserSkin(bufferedImage);
                return bufferedImage;
            }

            @Override
            public void skinAvailable() {
                instance.skinAvailable();
            }
        };

        return ib;

    }

    public static IImageBuffer returnCapeIImageBuffer() {
        IImageBuffer ib = new IImageBuffer() {
            @Override
            public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
                return bufferedImage;
            }

            @Override
            public void skinAvailable() {
            }
        };

        return ib;
    }
    /*
     * END of temporary fixes
     */

    /**
     * Makes the given area of the image transparent if it was previously completely opaque (used to remove the outer
     * layer of a skin around the head if it was saved all opaque; this would be redundant so it's assumed that the skin
     * maker is just using an image editor without an alpha channel)
     */
    private void setAreaTransparent(int fromX, int fromY, int toX, int toY) {
        if (!this.hasTransparency(fromX, fromY, toX, toY)) {
            for (int var5 = fromX; var5 < toX; ++var5) {
                for (int var6 = fromY; var6 < toY; ++var6) {
                    this.imageData[var5 + var6 * this.imageWidth] &= 16777215;
                }
            }
        }
    }

    /**
     * Makes the given area of the image opaque
     */
    private void setAreaOpaque(int fromX, int fromY, int toX, int toY) {
        for (int var5 = fromX; var5 < toX; ++var5) {
            for (int var6 = fromY; var6 < toY; ++var6) {
                this.imageData[var5 + var6 * this.imageWidth] |= -16777216;
            }
        }
    }

    /**
     * Returns true if the given area of the image contains transparent pixels
     */
    private boolean hasTransparency(int fromX, int fromY, int toX, int toY) {
        for (int var5 = fromX; var5 < toX; ++var5) {
            for (int var6 = fromY; var6 < toY; ++var6) {
                int var7 = this.imageData[var5 + var6 * this.imageWidth];

                if ((var7 >> 24 & 255) < 128) {
                    return true;
                }
            }
        }

        return false;
    }
}
