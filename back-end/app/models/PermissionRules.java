package models;

import javax.persistence.Embeddable;

/**
 * @author fabiomazzone
 */
@Embeddable
public class PermissionRules {
    private boolean create  = false;
    private boolean read    = false;
    private boolean update  = false;
    private boolean delete  = false;

    public PermissionRules(boolean create, boolean read, boolean update, boolean delete) {
        this.create = create;
        this.read   = read;
        this.update = update;
        this.delete = delete;
    }

    // Nicer Calls :)

    public boolean canCreate() {
        return create;
    }

    public boolean canRead() {
        return read;
    }

    public boolean canUpdate() {
        return update;
    }

    public boolean canDelete() {
        return delete;
    }

    public boolean isCreate() {
        return create;
    }


    // Getter & Setter

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    // Fast Creation

    public static PermissionRules getFullControl() {
        return new PermissionRules(true, true, true, true);
    }

    public static PermissionRules getReadControl() {
        return new PermissionRules(false, true, false, false);
    }

    public static PermissionRules getNoControl() {
        return new PermissionRules(false, false, false, false);
    }


    // to String


    @Override
    public String toString() {
        return "PermissionRules{" +
                "create=" + create +
                ", read=" + read +
                ", update=" + update +
                ", delete=" + delete +
                '}';
    }
}
