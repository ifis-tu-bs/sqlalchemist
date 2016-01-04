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
            Role admin = new Role();
            admin.setPriority(10000);
            admin.setRoleName("Admin");
            admin.setOwnTaskSetPermissions(PermissionRules.getFullControl());
            admin.setForeignTaskSetPermissions(PermissionRules.getFullControl());
            admin.setOwnTaskPermissions(PermissionRules.getFullControl());
            admin.setForeignTaskPermissions(PermissionRules.getFullControl());
            admin.setHomeworkPermissions(PermissionRules.getFullControl());
            admin.setRolePermissions(PermissionRules.getFullControl());
            admin.setGroupPermissions(PermissionRules.getFullControl());
            admin.setUserPermissions(PermissionRules.getFullControl());
            admin.setDeletable(false);
            admin.setCreator(null);
            admin.setVotes(200);
            admin.save();

            // Create HiWi
            Role hiWi = new Role();
            hiWi.setPriority(1000);
            hiWi.setRoleName("HiWi");
            hiWi.setOwnTaskSetPermissions(PermissionRules.getFullControl());
            hiWi.setForeignTaskSetPermissions(PermissionRules.getFullControl());
            hiWi.setOwnTaskPermissions(PermissionRules.getFullControl());
            hiWi.setForeignTaskPermissions(PermissionRules.getFullControl());
            hiWi.setHomeworkPermissions(PermissionRules.getReadControl());
            hiWi.setRolePermissions(PermissionRules.getNoControl());
            hiWi.setGroupPermissions(PermissionRules.getFullControl());
            hiWi.setUserPermissions(PermissionRules.getReadControl());
            hiWi.setDeletable(false);
            hiWi.setCreator(null);
            hiWi.setVotes(100);
            hiWi.save();

            // Create Creator
            Role creator = new Role();
            creator.setPriority(100);
            creator.setRoleName("Creator");
            creator.setOwnTaskSetPermissions(PermissionRules.getFullControl());
            creator.setForeignTaskSetPermissions(PermissionRules.getReadControl());
            creator.setOwnTaskPermissions(PermissionRules.getFullControl());
            creator.setForeignTaskPermissions(PermissionRules.getReadControl());
            creator.setHomeworkPermissions(PermissionRules.getNoControl());
            creator.setRolePermissions(PermissionRules.getNoControl());
            creator.setUserPermissions(PermissionRules.getNoControl());
            creator.setDeletable(false);
            creator.setCreator(null);
            creator.setVotes(2);
            creator.save();

            // Create User
            Role user = new Role();
            user.setPriority(10);
            user.setRoleName("User");
            user.setOwnTaskSetPermissions(PermissionRules.getNoControl());
            user.setForeignTaskSetPermissions(PermissionRules.getNoControl());
            user.setOwnTaskPermissions(PermissionRules.getNoControl());
            user.setForeignTaskPermissions(PermissionRules.getNoControl());
            user.setHomeworkPermissions(PermissionRules.getNoControl());
            user.setRolePermissions(PermissionRules.getNoControl());
            user.setUserPermissions(PermissionRules.getNoControl());
            user.setDeletable(false);
            user.setCreator(null);
            user.setVotes(1);
            user.save();

            Logger.info("Done Initializing");
        }
    }
}
