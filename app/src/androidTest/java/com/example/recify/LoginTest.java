package com.example.recify;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

public class LoginTest {

    @Rule
    public ActivityTestRule<Login> loginTestRule = new ActivityTestRule<Login>(Login.class);

    private Login mLogin = null;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(Register.class.getName(),null,false);


    @Before
    public void setUp() throws Exception {
        mLogin = loginTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isActivityValid() {
        View view = mLogin.findViewById(R.id.forgotPass);
        View view2 = mLogin.findViewById(R.id.registerButton);

        assertNotNull(view);
        assertNotNull(view2);
    }

    @Test
    public void testIntent() {
        //test if button works
        assertNotNull(mLogin.findViewById(R.id.registerButton));
        //onView(withId(R.id.registerButton)).perform(click());
        //if second activity created get those instruments
        Activity registerActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 6000);
        assertNull(registerActivity);
       //registerActivity.finish();

    }

    @Test
    public void onStart() {
    }

    @Test
    public void onCreate() {
    }

    @Test
    public void onActivityResult() {

    }

    @Test
    public void login() {
    }
}
