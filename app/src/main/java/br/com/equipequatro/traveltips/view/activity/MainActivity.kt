package br.com.equipequatro.traveltips.view.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.ActivityMainBinding
import br.com.equipequatro.traveltips.view.fragment.FavoritesFragment
import br.com.equipequatro.traveltips.view.fragment.FeedsFragment
import br.com.equipequatro.traveltips.view.fragment.HomeFragment
import br.com.equipequatro.traveltips.view.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}