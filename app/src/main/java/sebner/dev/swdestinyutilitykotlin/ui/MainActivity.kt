package sebner.dev.swdestinyutilitykotlin.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import sebner.dev.swdestinyutilitykotlin.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import sebner.dev.swdestinyutilitykotlin.ui.damage.DamageFragment
import sebner.dev.swdestinyutilitykotlin.ui.cards.CardListFragment
import sebner.dev.swdestinyutilitykotlin.utils.InjectorUtils

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
            InjectorUtils().provideSetRepository(this).startInitialSync()
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
                InjectorUtils().provideCardRepository(this).clearAll()
                InjectorUtils().provideSetRepository(this).clearAll()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun onFilterButtonPressed(v: View) {
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as FilterFragment).onFilterButtonPressed(v)
    }
}
