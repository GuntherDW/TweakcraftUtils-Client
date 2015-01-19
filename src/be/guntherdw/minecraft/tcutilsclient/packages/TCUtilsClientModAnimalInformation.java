package be.guntherdw.minecraft.tcutilsclient.packages;

/**
 * @author GuntherDW
 */
public class TCUtilsClientModAnimalInformation {

    private int entityId;
    private int age;
    private boolean ageLock;

    public TCUtilsClientModAnimalInformation(int entityId, int age, boolean ageLock) {
        this.age = age;
        this.entityId = entityId;
        this.ageLock = ageLock;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public boolean isAgeLock() {
        return ageLock;
    }

    public void setAgeLock(boolean ageLock) {
        this.ageLock = ageLock;
    }
}
