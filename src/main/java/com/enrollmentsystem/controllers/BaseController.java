package com.enrollmentsystem.controllers;

import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.utils.ViewNavigator;
import javafx.scene.Node;

public class BaseController {
    protected static UserSession session;
    private static Node root;
    private static int currentGradeLevel;

    public static void setSession(UserSession userSession) {
        session = userSession;
    }

    public static void setRoot(Node rootScene) {
        root = rootScene;
    }

    public static void logout() {
        UserSession.logout();
        session = null;
        ViewNavigator.logout(root);
    }

    public static void setCurrentGradeLevel(int gradeLevel) { currentGradeLevel = gradeLevel; }
    public static int getCurrentGradeLevel() { return currentGradeLevel; }
}
