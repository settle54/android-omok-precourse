package nextstep.omok

import android.graphics.drawable.VectorDrawable
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class MainTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkChangePlayer() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertEquals(Player.BLACK, activity.currentPlayer)
            activity.changePlayer()
            assertEquals(Player.WHITE, activity.currentPlayer)
        }
    }

}