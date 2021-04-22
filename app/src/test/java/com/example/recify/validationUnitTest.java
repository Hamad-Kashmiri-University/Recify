package com.example.recify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class validationUnitTest {
    public Register register = new Register();

    @Test
    public void usernameValidateTestTrue() {
       assertTrue(register.usernameValidateTest("username"));
    }

    @Test
    public void usernameValidateTestFalse() {
        assertFalse(register.usernameValidateTest("usernamelongerthan12"));
    }

    @Test
    public void usernameValidateTestFalseEmpty() {
        assertFalse(register.usernameValidateTest(""));
    }


    @Test
    public void emailValidateTestTrue() {
        assertTrue(register.emailValidateTest("working@email.com"));
    }

    @Test
    public void emailValidateTestFalseEmpty() {
        assertFalse(register.emailValidateTest(""));
    }

    @Test
    public void emailValidateTestFalseBadEmail() {
        assertFalse(register.emailValidateTest("bademail"));
    }



    @Test
    public void passwordValidateTestTrue() {
        assertTrue(register.passwordValidateTest("password"));
    }

    public void passwordValidateTestFalseEmpty() {
        assertFalse(register.passwordValidateTest(""));
    }
}