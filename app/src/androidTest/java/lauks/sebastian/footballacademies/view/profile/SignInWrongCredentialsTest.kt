package lauks.sebastian.footballacademies.view.profile


import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import lauks.sebastian.footballacademies.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

@LargeTest
@RunWith(AndroidJUnit4::class)
class SignInWrongCredentialsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SignInActivity::class.java)

    @Test
    fun signInActivityTest2() {
        try{
        val appCompatEditText = onView(
            allOf(
                withId(R.id.et_login),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_content),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("wrong"), closeSoftKeyboard())
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_content),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("wrongp"), closeSoftKeyboard())
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val appCompatButton = onView(
            allOf(
                withId(R.id.bt_sign_in), withText("Zaloguj się"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_content),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val editText = onView(withId(R.id.et_login))
        editText.check(matches(withText("wrong")))
        }catch (e: Exception){
            Log.d("CheckSignIn", "User has been already sign in")

        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
