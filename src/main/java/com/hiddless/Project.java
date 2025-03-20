package com.hiddless;

import com.hiddless.Controller.LoginRegisterController;

import java.util.logging.Logger;

public class Project {
    private static final Logger logger = Logger.getLogger(Project.class.getName());

    public static void main(String[] args) {
        logger.info("🚀 Project is starting...");
        LoginRegisterController controller = new LoginRegisterController();
        controller.login();

    }


}
