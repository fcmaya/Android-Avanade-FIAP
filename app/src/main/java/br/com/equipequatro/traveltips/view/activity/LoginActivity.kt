package br.com.equipequatro.traveltips.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.equipequatro.traveltips.view.fragment.LoginFragment
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.view.fragment.RegisterFragment
import br.com.equipequatro.traveltips.databinding.ActivityLoginBinding
import br.com.equipequatro.traveltips.viewmodel.LoginViewModel
import com.google.android.material.tabs.TabLayoutMediator

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val viewLogin = binding.root

        setContentView(viewLogin)

        mViewModel = ViewModelProvider.NewInstanceFactory().create(LoginViewModel::class.java)

        // Observer
        mViewModel.numeroTab.observe(this,{
            binding.tabLayoutLogin.setSelectedTabIndicator(it)
        })

        // Listener do Tab
        binding.tabLayoutLogin.setOnClickListener {
            mViewModel.mudarTab()
        }

        binding.viewPagerLogin.adapter = FragTypeAdapter(supportFragmentManager, lifecycle)

        val tabela = arrayListOf(getString(R.string.tab_login), getString(R.string.tab_registrar))

        TabLayoutMediator(binding.tabLayoutLogin, binding.viewPagerLogin){
                tab, posicao -> tab.text = tabela[posicao]
        }.attach()

    }

    class FragTypeAdapter(fragmentmanager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentmanager, lifecycle){
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {

            if (position == 0) {
                return LoginFragment()
            } else {
                return RegisterFragment()
            }

        }
    }

}
