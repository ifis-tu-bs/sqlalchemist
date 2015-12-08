package bootstrap;

import dao.RoleDAO;

import models.ActionRule;

import play.Logger;

/**
 * @author fabiomazzone
 */
public class BootstrapRole {
    public static void init() {
        if(RoleDAO.getAll().size() == 0) {
            Logger.info("Initializing 'Role' data");
            // Create Admin
            RoleDAO.create(
                    10000,
                    "Admin",
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    false,
                    null);

            // Create HiWi
            RoleDAO.create(
                    1000,
                    "HiWi",
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getReadControl(),
                    ActionRule.getNoControl(),
                    ActionRule.getReadControl(),
                    false,
                    null);

            // Create Creator
            RoleDAO.create(
                    100,
                    "Creator",
                    ActionRule.getFullControl(),
                    ActionRule.getReadControl(),
                    ActionRule.getFullControl(),
                    ActionRule.getReadControl(),
                    ActionRule.getNoControl(),
                    ActionRule.getNoControl(),
                    ActionRule.getNoControl(),
                    false,
                    null);

            // Create User
            RoleDAO.create(
                    10,
                    "User",
                    ActionRule.getReadControl(),
                    ActionRule.getReadControl(),
                    ActionRule.getReadControl(),
                    ActionRule.getReadControl(),
                    ActionRule.getNoControl(),
                    ActionRule.getNoControl(),
                    ActionRule.getNoControl(),
                    false,
                    null);

            Logger.info("Done Initializing");
        }
    }
}
