package br.com.equipequatro.traveltips

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import br.com.equipequatro.traveltips.databinding.ActivityMainBinding
import br.com.equipequatro.traveltips.ui.favorites.FavoritesFragment
import br.com.equipequatro.traveltips.ui.feeds.FeedsFragment
import br.com.equipequatro.traveltips.ui.home.HomeFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navViewDrawer: NavigationView = binding.navViewDrawer
        val toolbar: Toolbar = binding.toolbar

        setSupportActionBar( toolbar!! )

        var toogle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)//
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)


        val navView: BottomNavigationView = binding.navView
//        val scrollViewFragment: ScrollView = binding.scrollViewFragment

        navView.setOnItemSelectedListener {
            val supportFragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = when (it.itemId){
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

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutFragment, HomeFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}