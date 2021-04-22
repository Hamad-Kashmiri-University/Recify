package com.example.recify;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AddUpdateRecipeTest {
    @Rule
    public ActivityTestRule<AddUpdateRecipe> addUpdateRecipeActivityTestRule = new ActivityTestRule<AddUpdateRecipe>(AddUpdateRecipe.class);
    private AddUpdateRecipe addUpdateRecipe = null;



    @Before
    public void setUp() throws Exception {
        addUpdateRecipe = addUpdateRecipeActivityTestRule.getActivity();

    }

    @Test
    public void formInputs() {
        onView(withId(R.id.recipeName)).perform(typeText("Name"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.recipeName)).perform(click());
        onView(withId(R.id.recipeName)).check(matches(withText("Name")));

        onView(withId(R.id.recipeTime)).perform(typeText("5"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.recipeTime)).perform(click());
        onView(withId(R.id.recipeTime)).check(matches(withText("5")));


        onView(withId(R.id.recipeInstructions)).perform(typeText("Instructions"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.recipeInstructions)).perform(click());
        onView(withId(R.id.recipeInstructions)).check(matches(withText("Instructions")));

        onView(withId(R.id.recipeIngredients)).perform(typeText("Ingredients"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.recipeIngredients)).check(matches(withText("Ingredients")));

    }
}