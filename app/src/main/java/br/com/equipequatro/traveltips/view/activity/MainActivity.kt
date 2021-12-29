package br.com.equipequatro.traveltips.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import br.com.equipequatro.traveltips.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.view.fragment.FeedsFragment
import br.com.equipequatro.traveltips.view.fragment.HomeFragment
import br.com.equipequatro.traveltips.view.fragment.ProfileFragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
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

        buscaUsuarioLogado()

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

    private fun buscaUsuarioLogado() {
        var navigationView =
            binding.navViewDrawer.findViewById<NavigationView>(R.id.nav_view_drawer)
        var headerView =
            LayoutInflater.from(this).inflate(R.layout.header_menu_layout, navigationView, false)
        navigationView.addHeaderView(headerView)
        navigationView.removeHeaderView(navigationView.getHeaderView(0))
        var user_logado = headerView.findViewById<TextView>(R.id.tv_nome_user_logado)
        var foto_user_logado = headerView.findViewById<ImageView>(R.id.iv_foto_user_logado)
        var email_user_logado = headerView.findViewById<TextView>(R.id.tv_email_user_logado)

        FirebaseFirestore.getInstance().collection("usuario")
            .whereEqualTo("uuid", FirebaseAuth.getInstance().currentUser?.uid)
            .get()
            .addOnSuccessListener(OnSuccessListener {
                for (user in it.documents) {
                    if (user.data?.get("nome") != null) {
                        user_logado.setText(user.data?.get("nome").toString())
                    }

                    if (user.data?.get("email") != null) {
                        email_user_logado.setText(user.data?.get("email").toString())
                    } else {
                        if (FirebaseAuth.getInstance().currentUser?.email != "") {
                            email_user_logado.setText(FirebaseAuth.getInstance().currentUser?.email)
                        }
                    }

                    if (user.data?.get("fotoPerfilUrl") != null) {
                        Glide.with(this)
                            .load(user.data?.get("fotoPerfilUrl").toString())
                            .into(foto_user_logado)
                    } else {
                        if (FirebaseAuth.getInstance().currentUser?.photoUrl != null) {
                            Glide.with(this)
                                .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
                                .into(foto_user_logado)
                        }
                    }


                }
            })


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