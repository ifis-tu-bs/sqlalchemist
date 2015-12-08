package models;

import javax.persistence.Embeddable;

/**
 * @author fabiomazzone
 */
@Embeddable
public class ActionRule {
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;

    public ActionRule(boolean create, boolean read, boolean update, boolean delete) {
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }

    public boolean isCreate() {
        return create;
    }

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

    public static ActionRule getFullControl() {
        return new ActionRule(true, true, true, true);
    }

    public static ActionRule getReadControl() {
        return new ActionRule(false, true, false, false);
    }

    public static ActionRule getNoControl() {
        return new ActionRule(false, false, false, false);
    }
}
