package lauks.sebastian.footballacademies.view.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_main.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.view.news.NewsFragment
import lauks.sebastian.footballacademies.view.events.EventsFragment
import lauks.sebastian.footballacademies.view.squad.SquadFragment

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var newsFragment: NewsFragment
    lateinit var eventsFragment: EventsFragment
    lateinit var squadFragment: SquadFragment
    var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)

        actionBar = supportActionBar



        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

//        supportActionBar?.setDisplayHomeAsUpEnabled(true) // if it is uncommented, the hamburger icon becomes back arrow
        
        navigation_view.setNavigationItemSelectedListener(this)

        newsFragment = NewsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, newsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        actionBar?.title = getString(R.string.menu_news)

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_news -> {
                newsFragment = NewsFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, newsFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                actionBar?.title = getString(R.string.menu_news)
            }
            R.id.nav_events -> {
                eventsFragment = EventsFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, eventsFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                actionBar?.title = getString(R.string.menu_events)

            }
            R.id.nav_squad -> {
                squadFragment = SquadFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, squadFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                actionBar?.title = getString(R.string.menu_squad)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
