package be.guntherdw.minecraft.tcutilsclient.events.obf;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.util.ObfuscationUtilities;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.Method;

/**
 * Wrapper for obf/mcp reflection-accessed private methods, added to centralise the locations I have to update the obfuscated method names
 *
 * @author Adam Mummery-Smith
 *
 * @param <P> Parent class type
 * @param <R> Method return type
 */
public class PrivateMethods<P, R> {
    public static class Void {
    }

    /**
     * Class to which this field belongs
     */
    public final Class<?> parentClass;

    /**
     * Name used to access the field, determined at init
     */
    private final String methodName;

    /**
     * Method
     */
    private final Method method;

    /**
     * Creates a new private field entry
     *
     * @param owner
     * @param mapping
     * @param parameterTypes
     */
    private PrivateMethods(Class<?> owner, Obf mapping, Class<?>... parameterTypes) {
        this.parentClass = owner;
        this.methodName = ObfuscationUtilities.getObfuscatedFieldName(mapping);

        Method method = null;

        try {
            method = this.parentClass.getDeclaredMethod(this.methodName, parameterTypes);
            method.setAccessible(true);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        this.method = method;
    }

    /**
     * Invoke the method and return a value
     *
     * @param instance
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public R invoke(P instance, Object... args) {
        try {
            return (R) this.method.invoke(instance, args);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Invoke a static method that returns a value
     *
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public R invokeStatic(Object... args) {
        try {
            return (R) this.method.invoke(null, args);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Invoke a method that returns void
     *
     * @param instance
     * @param args
     */
    public void invokeVoid(P instance, Object... args) {
        try {
            this.method.invoke(instance, args);
        } catch (Exception ex) {
        }
    }

    /**
     * Invoke a static method that returns void
     *
     * @param args
     */
    public void invokeStaticVoid(Object... args) {
        try {
            this.method.invoke(null, args);
        } catch (Exception ex) {
        }
    }

    public static final PrivateMethods<RendererLivingEntity, Boolean>       canRenderName = new PrivateMethods<RendererLivingEntity, Boolean>(RendererLivingEntity.class, TCUtilsClientModObf.canRenderNamemthd,           EntityLivingBase.class);
    // public static final TCUtilsPrivAccessHandler<RenderManager, Double>               canRenderName     = new TCUtilsPrivAccessHandler<RenderManager, Double>(RenderManager.class, TCUtilsClientModObf.EntityLivingBasecls);

}
