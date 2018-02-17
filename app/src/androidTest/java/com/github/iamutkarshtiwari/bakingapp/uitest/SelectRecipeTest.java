package com.github.iamutkarshtiwari.bakingapp.uitest;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.github.iamutkarshtiwari.bakingapp.R;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by utkarshtiwari on 10/02/2018.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SelectRecipeTest {

    @Rule
    public ActivityTestRule<SelectRecipe> mActivityTestRule = new ActivityTestRule<>(SelectRecipe.class);

    @Test
    public void mainActivityTest() {

        ViewInteraction recyclerView = onView(allOf(withId(R.id.rView), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView2 = onView(allOf(withId(R.id.rView), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(2, click()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction txtDesc = onView(allOf(withId(R.id.txtDesc), isDisplayed()));
        ViewInteraction txtPrev = onView(allOf(withId(R.id.txtPrev), isDisplayed()));
        ViewInteraction txtNext = onView(allOf(withId(R.id.txtNext), isDisplayed()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        pressBack();

        ViewInteraction recyclerView3 = onView(allOf(withId(R.id.rView), isDisplayed()));

    }

}
