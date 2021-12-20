package br.com.equipequatro.traveltips

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.equipequatro.traveltips.databinding.ActivityLoginBinding
import br.com.equipequatro.traveltips.databinding.FragmentLoginBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val viewLogin = binding.root

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        setContentView(viewLogin)



        binding.viewPagerLogin.adapter = FragTypeAdapter(supportFragmentManager, lifecycle)

        val tabela = arrayListOf("Login", "Register")

        TabLayoutMediator(binding.tabLayoutLogin, binding.viewPagerLogin){
                tab, posicao -> tab.text = tabela[posicao]
        }.attach()

    }

    class FragTypeAdapter(fragmentmanager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentmanager, lifecycle){
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {

            if (position == 0)
            {
                return LoginFragment()
            }
            else
            {
                return RegisterFragment()
            }
//
//            return   when (position) {
//                0 -> {
//                    LoginFragment()
//                }
//                1 -> {
//                    LoginFragment()
//                }
//                else ->
//                {
//                    LoginFragment()
//                }
//            }
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}

