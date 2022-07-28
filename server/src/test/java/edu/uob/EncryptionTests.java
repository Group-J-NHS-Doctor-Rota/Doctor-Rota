package edu.uob;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionTests {

    @Test
    void testHashPassword() {
        String randomPassword = RandomStringUtils.randomAlphabetic(20);
        Encryption encryption = new Encryption();
        String hashedPassword = encryption.hashPassword(randomPassword);
        assertTrue(encryption.passwordMatches(randomPassword, hashedPassword),
                "Encryption should match hashed password to random one");
    }

    @Test
    void testRepeatedHashPassword() {
        String randomPassword = RandomStringUtils.randomAlphabetic(20);
        Encryption encryption = new Encryption();
        String hashedPassword1 = encryption.hashPassword(randomPassword);
        String hashedPassword2 = encryption.hashPassword(randomPassword);
        assertTrue(encryption.passwordMatches(randomPassword, hashedPassword1),
                "Encryption should match hashed password to random one");
        assertTrue(encryption.passwordMatches(randomPassword, hashedPassword2),
                "Encryption should match hashed password to random one");
        assertNotEquals(hashedPassword1, hashedPassword2,
                "Due to random salt, hashed passwords should never be the same");
    }

    @Test
    void testGetRandomToken() {
        String randomToken1 = Encryption.getRandomToken();
        String randomToken2 = Encryption.getRandomToken();
        assertNotEquals(randomToken1, randomToken2, "Tokens should not be the same");
        assertEquals(randomToken1.length(), randomToken2.length(), "Token lengths should be the same");
    }

    @Test
    void testGetPepper() {
        String pepper = ConnectionTools.getEnvOrSysVariable("PEPPER");
        // Check pepper has been found, so is not empty or null
        assertNotEquals("", pepper, "Pepper shouldn't be empty");
        assertNotEquals(null, pepper, "Pepper shouldn't be null");
    }

    @Test
    void testGetDefaultPassword() {
        String defaultPassword = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
        // Check default password has been found, so is not empty or null
        assertNotEquals("", defaultPassword, "Default password shouldn't be empty");
        assertNotEquals(null, defaultPassword, "Default password shouldn't be null");
    }
}
