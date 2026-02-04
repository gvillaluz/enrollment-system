package com.enrollmentsystem.controllers;

import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.utils.ViewNavigator;
import javafx.scene.Node;

import java.io.IOException;

public class BaseController {
    protected static UserSession session;
    private static Node root;

    public static void setSession(UserSession userSession) {
        session = userSession;
    }

    public static void setRoot(Node rootScene) {
        root = rootScene;
    }

    public static void logout() {
        UserSession.logout();
        session = null;
        ViewNavigator.navigateLogin(root);
    }
}
