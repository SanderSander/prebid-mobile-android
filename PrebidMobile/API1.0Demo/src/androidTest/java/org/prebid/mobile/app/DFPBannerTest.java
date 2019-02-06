package org.prebid.mobile.app;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.web.assertion.WebViewAssertions.webContent;
import static androidx.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static androidx.test.espresso.web.matcher.DomMatchers.containingTextInBody;
import static androidx.test.espresso.web.model.Atoms.getCurrentUrl;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DFPBannerTest {
    @Rule
    public ActivityTestRule<MainActivity> m = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testDFPBannerWithoutAutoRefreshAndSize300x250() throws Exception {
        onView(withId(R.id.autoRefreshInput)).perform(typeText("0"));
        onView(withId(R.id.showAd)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.adFrame)).check(matches(isDisplayed()));
        onWebView().check(webMatches(getCurrentUrl(), containsString("pubads.g.doubleclick.net/gampad/ads")));
        onWebView().check(webContent(containingTextInBody("ucTag.renderAd")));
        assertEquals(1, ((DemoActivity) TestUtil.getCurrentActivity()).refreshCount);
        Thread.sleep(30000);
        assertEquals(1, ((DemoActivity) TestUtil.getCurrentActivity()).refreshCount);
    }

    @Test
    public void testDFPBannerWithoutAutoRefreshAndSize320x50() throws Exception {
        onView(withId(R.id.autoRefreshInput)).perform(typeText("0"));
        onView(withId(R.id.adSizeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("320x50"))).perform(click());
        onView(withId(R.id.showAd)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.adFrame)).check(matches(isDisplayed()));
        onWebView().check(webMatches(getCurrentUrl(), containsString("pubads.g.doubleclick.net/gampad/ads")));
        onWebView().check(webContent(containingTextInBody("ucTag.renderAd")));
        assertEquals(1, ((DemoActivity) TestUtil.getCurrentActivity()).refreshCount);
        Thread.sleep(30000);
        assertEquals(1, ((DemoActivity) TestUtil.getCurrentActivity()).refreshCount);
    }

    @Test
    public void testDFPBannerWithAutoRefreshAndSize300x250() throws Exception {
        onView(withId(R.id.autoRefreshInput)).perform(typeText("30000"));
        onView(withId(R.id.showAd)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.adFrame)).check(matches(isDisplayed()));
        onWebView().check(webMatches(getCurrentUrl(), containsString("pubads.g.doubleclick.net/gampad/ads")));
        onWebView().check(webContent(containingTextInBody("ucTag.renderAd")));
        assertEquals(1, ((DemoActivity) TestUtil.getCurrentActivity()).refreshCount);
        Thread.sleep(30000);
        assertEquals(2, ((DemoActivity) TestUtil.getCurrentActivity()).refreshCount);
        ((DemoActivity) TestUtil.getCurrentActivity()).stopAutoRefresh();
        Thread.sleep(35000);
        assertEquals(2, ((DemoActivity) TestUtil.getCurrentActivity()).refreshCount);
    }

}
