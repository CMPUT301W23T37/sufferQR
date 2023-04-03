package com.example.sufferqr;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class searchTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<SearchPlayer> rule = new ActivityTestRule<>(SearchPlayer.class,
            true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception something
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * close activity after each test
     * @throws Exception if not correctly closed
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     * Gets the Activity
     *
     * @throws Exception something
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test search displayed
     */
    @Test
    public void testSearchViewDisplay(){
        solo.assertCurrentActivity("Wrong Activity", SearchPlayer.class);
        onView(withId(R.id.searchBar_searchPlayer)).perform(click()).check(matches(isDisplayed()));
    }

    /**
     * Test if user able to find
     */
    @Test
    public void testSearchResult(){
        solo.assertCurrentActivity("Wrong Activity", SearchPlayer.class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Player").orderBy("sumScore", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    DocumentSnapshot documentSnapshot = value.getDocuments().get(0);
                    String name =String.valueOf( documentSnapshot.getData().get("name"));

                    final SearchPlayer activity = rule.getActivity();
                    SearchView searchView = (SearchView) solo.getView(R.id.searchBar_searchPlayer);
                    searchView.setQuery(name,true);
                    solo.waitForText(name,1,3000);
                }

            }
        });
    }


}
