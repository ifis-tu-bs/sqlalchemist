package bootstrap;

import dao.RoleDAO;

import models.PermissionRules;

import models.Role;
import play.Logger;

/**
 * @author fabiomazzone
 */
public class BootstrapRole {
    public static void init() {
        if(RoleDAO.getAll().size() == 0) {
            Logger.info("Initializing 'Role' data");
            // Create Admin
            Role admin = new Role(
                    10000,
                    "Admin",
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    false,
                    null);

            admin.save();

            // Create HiWi
            Role hiwi = new Role(
                    1000,
                    "HiWi",
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getReadControl(),
                    PermissionRules.getNoControl(),
                    PermissionRules.getReadControl(),
                    false,
                    null);

            hiwi.save();

            // Create Creator
            Role creator = new Role(
                    100,
                    "Creator",
                    PermissionRules.getFullControl(),
                    PermissionRules.getReadControl(),
                    PermissionRules.getFullControl(),
                    PermissionRules.getReadControl(),
                    PermissionRules.getNoControl(),
                    PermissionRules.getNoControl(),
                    PermissionRules.getNoControl(),
                    false,
                    null);

            creator.save();

            // Create User
            Role user = new Role(
                    10,
                    "User",
                    PermissionRules.getReadControl(),
                    PermissionRules.getReadControl(),
                    PermissionRules.getReadControl(),
                    PermissionRules.getReadControl(),
                    PermissionRules.getNoControl(),
                    PermissionRules.getNoControl(),
                    PermissionRules.getNoControl(),
                    false,
                    null);

            user.save();

            Logger.info("Done Initializing");
        }
    }
}
