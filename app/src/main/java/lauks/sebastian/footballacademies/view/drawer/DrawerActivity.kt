package lauks.sebastian.footballacademies.view.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_main.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.view.events.NewsFragment
import lauks.sebastian.footballacademies.view.news.EventsFragment
import lauks.sebastian.footballacademies.view.squad.SquadFragment

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var newsFragment: NewsFragment
    lateinit var eventsFragment: EventsFragment
    lateinit var squadFragment: SquadFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.menu_news)


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
            }
            R.id.nav_events -> {
                eventsFragment = EventsFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, eventsFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_squad -> {
                squadFragment = SquadFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, squadFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
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
