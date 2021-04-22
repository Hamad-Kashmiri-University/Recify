package com.example.recify;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class RegisterTest {

    @Rule
    public ActivityTestRule<Register> registerTestRule = new ActivityTestRule<Register>(Register.class);

    private Register mRegister = null;

    @Before
    public void setUp() throws Exception {
        mRegister = registerTestRule.getActivity();
    }



    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isActivityValid() {
        View view = mRegister.findViewById(R.id.logotext);
        View view2 = mRegister.findViewById(R.id.formSubmit);

        assertNotNull(view);
        assertNotNull(view2);
    }

}