package com.fabrika.pampaza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.fabrika.pampaza.databinding.ActivityMainBinding
import com.fabrika.pampaza.login.ui.LoginActivity
import com.fabrika.pampaza.newpost.ui.NewPostActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    companion object{
        lateinit var viewmodel: MainViewModel
    }

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavBar, navController)

        addListeners()
        addObservers()

        viewmodel.getUser("GmBegmyrat", "123123")
    }

    private fun addObservers() {
//        viewmodel.userEntity.observe(this) {
//            if(viewmodel.isSplash)
//                navController.navigate(R.id.action_splashFragment_to_homeFragment2)
//        }
    }

    private fun addListeners() {
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, 0, 0)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)

        binding.toolbar.setOnMenuItemClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            startActivity(intent)
            return@setOnMenuItemClickListener true
        }

        binding.iNewPost.setOnClickListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.i_newPost -> {
                val intent = Intent(this, NewPostActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
            }
        }
    }
}