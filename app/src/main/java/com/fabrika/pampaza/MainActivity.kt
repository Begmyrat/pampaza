package com.fabrika.pampaza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.databinding.ActivityMainBinding
import com.fabrika.pampaza.login.ui.LoginActivity
import com.fabrika.pampaza.newpost.ui.NewPostActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    lateinit var binding: ActivityMainBinding
    lateinit var viewmodel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]

        addListeners()
        addObservers()
    }

    private fun addObservers() {

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