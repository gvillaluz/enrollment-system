package com.enrollmentsystem.utils;

import javafx.scene.control.ScrollPane;
import org.w3c.dom.Node;

public class Scroll {
    public static void fixScrollSpeed(ScrollPane scrollPane) {//
        double scrollSpeed = 5.0;// Increase this number to scroll faster/smoother
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * scrollSpeed;
            double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / contentHeight);
        });
    }
}
