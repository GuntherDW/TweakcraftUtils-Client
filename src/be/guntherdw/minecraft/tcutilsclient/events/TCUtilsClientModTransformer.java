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
        // Event onRenderLivingLabel = Event.getOrCreate("onRenderLivingLabel", true);
        // Event onLivingUpdate = Event.getOrCreate("onLivingUpdate", true);
        Event onGetLocationCape = Event.getOrCreate("ongetLocationCape", true);
        Event onPassSpecialRender = Event.getOrCreate("onPassSpecialRender", true);



        // MethodInfo renderLivingLabel = new MethodInfo(TCUtilsClientModObf.Rendercls, TCUtilsClientModObf.RenderLivingLabelmthd, Void.TYPE, TCUtilsClientModObf.Entitycls, String.class, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE);
        // MethodInfo livingUpdate = new MethodInfo(TCUtilsClientModObf.EntityAgeablecls, TCUtilsClientModObf.onLivingUpdatemthd, Void.TYPE);
        MethodInfo getLocationCape = new MethodInfo(TCUtilsClientModObf.NetworkPlayerInfocls, TCUtilsClientModObf.getLocationCapemthd, TCUtilsClientModObf.ResourceLocationcls);
        MethodInfo passSpecialRender = new MethodInfo(TCUtilsClientModObf.RenderLivingEntitycls, TCUtilsClientModObf.passSpecialRenderermthd, Void.TYPE, TCUtilsClientModObf.EntityLivingBasecls,  Double.TYPE,  Double.TYPE, Double.TYPE);

        MethodHead injectionPoint = new MethodHead();

        // this.addEvent(onRenderLivingLabel, renderLivingLabel, injectionPoint);
        // this.addEvent(onLivingUpdate, livingUpdate, injectionPoint);
        this.addEvent(onGetLocationCape, getLocationCape, injectionPoint);
        this.addEvent(onPassSpecialRender, passSpecialRender, injectionPoint);

        // onRenderLivingLabel.addListener(new MethodInfo("be.guntherdw.minecraft.tcutilsclient.events.TCUtilsClientModEvents", "onRenderLivingLabel"));
        // onLivingUpdate.addListener(new MethodInfo("be.guntherdw.minecraft.tcutilsclient.events.TCUtilsClientModEvents", "onLivingUpdate"));
        onGetLocationCape.addListener(new MethodInfo("be.guntherdw.minecraft.tcutilsclient.events.TCUtilsClientModEvents", "onGetLocationCape"));
        onPassSpecialRender.addListener(new MethodInfo("be.guntherdw.minecraft.tcutilsclient.events.TCUtilsClientModEvents", "onPassSpecialRender"));

    }
}
