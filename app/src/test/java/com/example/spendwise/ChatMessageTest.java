package com.example.spendwise;

import static org.junit.Assert.*;
import com.example.spendwise.model.ChatMessage;
import org.junit.Test;

public class ChatMessageTest {

    @Test
    public void testEmptyMessageCreation() {
        ChatMessage message = new ChatMessage();

        assertNull("Role should be null by default", message.getRole());
        assertNull("Content should be null by default", message.getContent());
        assertEquals("Timestamp should be 0 by default", 0, message.getTimestamp());
    }

    @Test
    public void testNullRoleHandling() {
        String content = "Test content";
        ChatMessage message = new ChatMessage(null, content);

        assertNull("Null role should be accepted", message.getRole());
        assertEquals("Content should still be set", content, message.getContent());
    }

    @Test
    public void testNullContentHandling() {
        String role = "user";
        ChatMessage message = new ChatMessage(role, null);

        assertEquals("Role should be set", role, message.getRole());
        assertNull("Null content should be accepted", message.getContent());
    }

    @Test
    public void testEmptyStringContent() {
        String role = "user";
        String emptyContent = "";
        ChatMessage message = new ChatMessage(role, emptyContent);

        assertEquals("Role should be set", role, message.getRole());
        assertEquals("Empty content should be preserved", "", message.getContent());
        assertNotNull("Content should not be null", message.getContent());
    }

    @Test
    public void testLongMessageContent() {
        String role = "ai";
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longContent.append("This is a long message. ");
        }

        ChatMessage message = new ChatMessage(role, longContent.toString());

        assertEquals("Long content should be stored correctly",
                longContent.toString(), message.getContent());
        assertTrue("Content length should be substantial",
                message.getContent().length() > 10000);
    }

    @Test
    public void testSpecialCharactersInContent() {
        String role = "user";
        String specialContent = "Hello! @#$%^&*() 你好 🎉 \n\t\r";

        ChatMessage message = new ChatMessage(role, specialContent);

        assertEquals("Special characters should be preserved",
                specialContent, message.getContent());
    }

    @Test
    public void testMultilineContent() {
        String role = "user";
        String multilineContent = "Line 1\nLine 2\nLine 3";

        ChatMessage message = new ChatMessage(role, multilineContent);

        assertEquals("Multiline content should be preserved",
                multilineContent, message.getContent());
        assertTrue("Content should contain newlines",
                message.getContent().contains("\n"));
    }

    @Test
    public void testTimestampUniqueness() throws InterruptedException {
        ChatMessage message1 = new ChatMessage("user", "First message");
        Thread.sleep(10); // Small delay to ensure different timestamps
        ChatMessage message2 = new ChatMessage("user", "Second message");

        assertTrue("Timestamps should be different",
                message2.getTimestamp() > message1.getTimestamp());
    }

    @Test
    public void testTimestampReasonableRange() {
        long beforeCreation = System.currentTimeMillis();
        ChatMessage message = new ChatMessage("user", "Test");
        long afterCreation = System.currentTimeMillis();

        assertTrue("Timestamp should be after before-time",
                message.getTimestamp() >= beforeCreation);
        assertTrue("Timestamp should be before after-time",
                message.getTimestamp() <= afterCreation);
    }

    @Test
    public void testRoleTypes() {
        String[] roles = {"user", "ai", "system", "assistant", "bot"};

        for (String role : roles) {
            ChatMessage message = new ChatMessage(role, "Test content");
            assertEquals("Role should match for: " + role, role, message.getRole());
        }
    }

    @Test
    public void testContentUpdate() {
        ChatMessage message = new ChatMessage("user", "Original content");
        String originalContent = message.getContent();

        String newContent = "Updated content";
        message.setContent(newContent);

        assertNotEquals("Content should be different after update",
                originalContent, message.getContent());
        assertEquals("New content should be set", newContent, message.getContent());
    }

    @Test
    public void testRoleUpdate() {
        ChatMessage message = new ChatMessage("user", "Test content");
        String originalRole = message.getRole();

        message.setRole("ai");

        assertNotEquals("Role should be different after update",
                originalRole, message.getRole());
        assertEquals("New role should be ai", "ai", message.getRole());
    }

    @Test
    public void testWhitespaceContent() {
        String role = "user";
        String whitespaceContent = "   ";

        ChatMessage message = new ChatMessage(role, whitespaceContent);

        assertEquals("Whitespace content should be preserved",
                whitespaceContent, message.getContent());
        assertFalse("Whitespace content should not be empty string",
                message.getContent().isEmpty());
    }

    @Test
    public void testCaseSensitiveRole() {
        ChatMessage message1 = new ChatMessage("User", "Test");
        ChatMessage message2 = new ChatMessage("user", "Test");

        assertNotEquals("Roles with different cases should not be equal",
                message1.getRole(), message2.getRole());
    }

    @Test
    public void testMultipleUpdates() {
        ChatMessage message = new ChatMessage();

        message.setRole("user");
        message.setContent("First");
        assertEquals("First update should work", "First", message.getContent());

        message.setContent("Second");
        assertEquals("Second update should work", "Second", message.getContent());

        message.setContent("Third");
        assertEquals("Third update should work", "Third", message.getContent());
    }

    @Test
    public void testTimestampManualSet() {
        ChatMessage message = new ChatMessage("user", "Test");
        long customTimestamp = 1609459200000L; // Jan 1, 2021

        message.setTimestamp(customTimestamp);

        assertEquals("Custom timestamp should be set",
                customTimestamp, message.getTimestamp());
    }

    @Test
    public void testZeroTimestamp() {
        ChatMessage message = new ChatMessage("user", "Test");
        message.setTimestamp(0);

        assertEquals("Zero timestamp should be allowed", 0, message.getTimestamp());
    }

    @Test
    public void testNegativeTimestamp() {
        ChatMessage message = new ChatMessage("user", "Test");
        long negativeTimestamp = -1000L;
        message.setTimestamp(negativeTimestamp);

        assertEquals("Negative timestamp should be stored",
                negativeTimestamp, message.getTimestamp());
    }

    @Test
    public void testJsonLikeContent() {
        String role = "ai";
        String jsonContent = "{\"budget\": 200, \"category\": \"food\"}";

        ChatMessage message = new ChatMessage(role, jsonContent);

        assertEquals("JSON-like content should be preserved",
                jsonContent, message.getContent());
    }

    @Test
    public void testMessageChaining() {
        ChatMessage message = new ChatMessage();

        // Test if setters could potentially support chaining
        message.setRole("user");
        message.setContent("Test");
        message.setTimestamp(System.currentTimeMillis());

        assertNotNull("Role should be set", message.getRole());
        assertNotNull("Content should be set", message.getContent());
        assertTrue("Timestamp should be set", message.getTimestamp() > 0);
    }
}