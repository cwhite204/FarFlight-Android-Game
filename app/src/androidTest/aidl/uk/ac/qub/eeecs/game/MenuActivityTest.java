package uk.ac.qub.eeecs.game;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.R;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by tompurdon on 21/01/2018.
 */
public class MenuActivityTest {
    @Rule
    public ActivityTestRule<MenuActivity> mActivityTestRule = new ActivityTestRule<MenuActivity>(MenuActivity.class);

    private MenuActivity mActivty = null;

    @Before
    public void setUp() throws Exception {

        mActivty = mActivityTestRule.getActivity();

    }

    @Test
    public void testLaunch()
    {
        View view = mActivty.findViewById(R.id.playBtn);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {

        mActivty = null;

    }

}