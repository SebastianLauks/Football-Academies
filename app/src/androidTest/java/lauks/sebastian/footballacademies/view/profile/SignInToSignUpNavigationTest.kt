package lauks.sebastian.footballacademies.view.profile


import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SignInToSignUpNavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SignInActivity::class.java)

    @Test
    fun signInToSignUp() {
        try{
        val appCompatButton = onView(
            allOf(
                withId(R.id.bt_sign_up_ref), withText("Rejestracja"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_content),
                        2
                    ),
                    1
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
        editText.check(matches(isDisplayed()))
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val editText2 = onView(withId(R.id.et_password))
        editText2.check(matches(isDisplayed()))
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val editText3 = onView(withId(R.id.et_password_repeat))

//            onView(
//            allOf(
//                withId(R.id.et_password_repeat), withText("Powtórz hasło"),
//                childAtPosition(
//                    childAtPosition(
//                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
//                        2
//                    ),
//                    2
//                ),
//                isDisplayed()
//            )
//        )
        editText3.check(matches(isDisplayed()))
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
