package br.com.equipequatro.traveltips.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import br.com.equipequatro.traveltips.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.view.fragment.FavoritesFragment
import br.com.equipequatro.traveltips.view.fragment.FeedsFragment
import br.com.equipequatro.traveltips.view.fragment.HomeFragment
import br.com.equipequatro.traveltips.view.fragment.ProfileFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navViewDrawer: NavigationView = binding.navViewDrawer
        val toolbar: Toolbar = binding.toolbar

        setSupportActionBar(toolbar!!)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)//
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)

        val navView: BottomNavigationView = binding.navView

        navView.setOnItemSelectedListener {
            val supportFragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = when (it.itemId) {
                R.id.navigation_home -> {
                    HomeFragment()
                }
                R.id.navigation_feeds -> {
                    FeedsFragment()
                }
                R.id.navigation_favorites -> {
                    FavoritesFragment()
                }
                else -> {
                    ProfileFragment()
                }
            }

            supportFragmentTransaction.replace(R.id.frameLayoutFragment, fragment)
            supportFragmentTransaction.commit()

            true
        }

        /* Seta o home como primeiro fragment */
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutFragment, HomeFragment())
            .commit()

        navViewDrawer.setNavigationItemSelectedListener {

            Toast.makeText(this, getString(it.itemId), Toast.LENGTH_SHORT)

            when (it.itemId) {
                R.id.nav_menu_profile -> {
                    val supportFragmentTransaction = supportFragmentManager.beginTransaction()
                    supportFragmentTransaction.replace(R.id.frameLayoutFragment, FeedsFragment())
                    supportFragmentTransaction.commit()
                }
                R.id.nav_menu_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_menu_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_menu_exit -> {
                    auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    finish()
                }
            }

            true
        }

        binding.btnNewPost.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Toast.makeText(this, getString(item.itemId), Toast.LENGTH_LONG)

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}