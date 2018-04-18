package sebner.dev.swdestinyutilitykotlin.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.android.ext.android.inject
import sebner.dev.swdestinyutilitykotlin.R
import sebner.dev.swdestinyutilitykotlin.data.CardRepository
import sebner.dev.swdestinyutilitykotlin.data.SetRepository
import sebner.dev.swdestinyutilitykotlin.ui.cards.CardListFragment
import sebner.dev.swdestinyutilitykotlin.ui.damage.DamageFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val setRepository by inject<SetRepository>()
    private val cardRepository by inject<CardRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout,
                toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        savedInstanceState ?: kotlin.run {
            setRepository.startInitialSync()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, CardListFragment() as Fragment?, "main")
                    .commit()
        }
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cards -> supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, CardListFragment() as Fragment, "fragment_cards")
                    ?.commit()
            /* R.id.nav_battlefield -> supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, BattlefieldFragment() as Fragment, "frag_battlefield")
                    ?.commit()*/
            R.id.nav_damage -> supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, DamageFragment() as Fragment, "fragment_damage")
                    ?.commit()
            R.id.nav_debug_clear -> {
                cardRepository.clearAll()
                setRepository.clearAll()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun onFilterButtonPressed(v: View) {
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as FilterFragment).onFilterButtonPressed(v)
    }
}
