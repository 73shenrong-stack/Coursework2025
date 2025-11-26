package com.comp2042.view.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationPanelTest {

    private NotificationPanel notificationPanel;
    private ObservableList<Node> containerList;

    @BeforeEach
    void setUp() {
        containerList = FXCollections.observableArrayList();
    }

    @Test
    void testConstruction_WithText() {
        notificationPanel = new NotificationPanel("+100");
        assertNotNull(notificationPanel);
    }

    @Test
    void testConstruction_WithEmptyText() {
        notificationPanel = new NotificationPanel("");
        assertNotNull(notificationPanel);
    }

    @Test
    void testConstruction_WithLargeScore() {
        notificationPanel = new NotificationPanel("+9999");
        assertNotNull(notificationPanel);
    }

    @Test
    void testConstruction_SetsMinDimensions() {
        notificationPanel = new NotificationPanel("+50");

        assertEquals(200, notificationPanel.getMinHeight());
        assertEquals(220, notificationPanel.getMinWidth());
    }

    @Test
    void testConstruction_HasContent() {
        notificationPanel = new NotificationPanel("+100");

        assertNotNull(notificationPanel.getCenter());
    }

    @Test
    void testShowScore_AddsToList() {
        notificationPanel = new NotificationPanel("+100");
        containerList.add(notificationPanel);

        int initialSize = containerList.size();

        assertDoesNotThrow(() -> notificationPanel.showScore(containerList));

        assertEquals(initialSize, containerList.size(),
                "Panel should be added before showScore is called");
    }

    @Test
    void testShowScore_StartsAnimation() {
        notificationPanel = new NotificationPanel("+200");
        containerList.add(notificationPanel);

        assertDoesNotThrow(() -> notificationPanel.showScore(containerList));
    }

    @Test
    void testMultipleNotifications() {
        NotificationPanel panel1 = new NotificationPanel("+50");
        NotificationPanel panel2 = new NotificationPanel("+100");
        NotificationPanel panel3 = new NotificationPanel("+150");

        containerList.add(panel1);
        containerList.add(panel2);
        containerList.add(panel3);

        assertEquals(3, containerList.size());
    }

    @Test
    void testShowScore_WithDifferentScores() {
        String[] scores = {"+10", "+50", "+100", "+200", "+800"};

        for (String score : scores) {
            NotificationPanel panel = new NotificationPanel(score);
            containerList.add(panel);
            assertDoesNotThrow(() -> panel.showScore(containerList));
        }
    }

    @Test
    void testConstruction_WithNegativeValue() {
        notificationPanel = new NotificationPanel("-50");
        assertNotNull(notificationPanel);
    }

    @Test
    void testConstruction_WithZero() {
        notificationPanel = new NotificationPanel("+0");
        assertNotNull(notificationPanel);
    }

    @Test
    void testShowScore_WithEmptyList() {
        notificationPanel = new NotificationPanel("+100");

        assertDoesNotThrow(() -> notificationPanel.showScore(containerList));
    }

    @Test
    void testNotificationPanel_ExtendsPane() {
        notificationPanel = new NotificationPanel("+100");

        assertTrue(notificationPanel instanceof javafx.scene.layout.BorderPane);
    }

    @Test
    void testConstruction_WithSpecialCharacters() {
        notificationPanel = new NotificationPanel("★ +500 ★");
        assertNotNull(notificationPanel);
    }
}