package be.guntherdw.minecraft.tcutilsclient.events;

import be.guntherdw.minecraft.tcutilsclient.events.obf.TCUtilsClientModObf;
import com.mumfrey.liteloader.transformers.event.Event;
import com.mumfrey.liteloader.transformers.event.EventInjectionTransformer;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.inject.MethodHead;

/**
 * Created by guntherdw on 07/01/15.
 */
public class TCUtilsClientModTransformer extends EventInjectionTransformer {
    /**
     * Subclasses should register events here
     */
    @Override
    protected void addEvents() {
        Event onGetLocationCape = Event.getOrCreate("ongetLocationCape", true);
        Event onGetLocationSkin = Event.getOrCreate("ongetLocationSkin", true);
        Event onPassSpecialRender = Event.getOrCreate("onPassSpecialRender", true);

        MethodInfo getLocationCape = new MethodInfo(TCUtilsClientModObf.NetworkPlayerInfocls, TCUtilsClientModObf.getLocationCapemthd, TCUtilsClientModObf.ResourceLocationcls);
        MethodInfo getLocationSkin = new MethodInfo(TCUtilsClientModObf.NetworkPlayerInfocls, TCUtilsClientModObf.getLocationSkinmthd, TCUtilsClientModObf.ResourceLocationcls);
        MethodInfo passSpecialRender = new MethodInfo(TCUtilsClientModObf.RenderLivingEntitycls, TCUtilsClientModObf.passSpecialRenderermthd, Void.TYPE, TCUtilsClientModObf.EntityLivingBasecls,  Double.TYPE,  Double.TYPE, Double.TYPE);

        MethodHead injectionPoint = new MethodHead();

        this.addEvent(onGetLocationCape, getLocationCape, injectionPoint);
        this.addEvent(onGetLocationSkin, getLocationSkin, injectionPoint);
        this.addEvent(onPassSpecialRender, passSpecialRender, injectionPoint);

        onGetLocationCape.addListener(new MethodInfo("be.guntherdw.minecraft.tcutilsclient.events.TCUtilsClientModEvents", "onGetLocationCape"));
        onGetLocationSkin.addListener(new MethodInfo("be.guntherdw.minecraft.tcutilsclient.events.TCUtilsClientModEvents", "onGetLocationSkin"));
        onPassSpecialRender.addListener(new MethodInfo("be.guntherdw.minecraft.tcutilsclient.events.TCUtilsClientModEvents", "onPassSpecialRender"));

    }
}
