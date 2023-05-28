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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.fabrika.pampaza.common.ui.loadImageUrl
import com.fabrika.pampaza.databinding.ActivityMainBinding
import com.fabrika.pampaza.databinding.NavHeaderBinding
import com.fabrika.pampaza.login.ui.LoginActivity
import com.fabrika.pampaza.newpost.ui.NewPostActivity
import com.fabrika.pampaza.profile.ui.ProfileActivity
import com.fabrika.pampaza.settings.ui.SettingsActivity
import com.fabrika.pampaza.utils.SharedPref
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bindingNavView: NavHeaderBinding

    companion object{
        lateinit var viewmodel: MainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingNavView = NavHeaderBinding.bind(binding.navView.getHeaderView(0))
        setContentView(binding.root)
        SharedPref.init(applicationContext)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavBar, navController)
        addListeners()
        addObservers()

        binding.tVersion.text = getString(R.string.version, "1.0.0")

        viewmodel.getUser(SharedPref.read(SharedPref.USER_ID, "") ?: "", SharedPref.read(SharedPref.PASSWORD, "") ?: "")
    }

    override fun onResume() {
        super.onResume()
        setLoginTitle()
    }

    private fun setLoginTitle(){
        binding.tLogin.text = if(SharedPref.read(SharedPref.IS_LOGGED_IN, false)) getString(R.string.logout) else getString(R.string.login)
    }

    private fun addObservers() {
        viewmodel.userEntity.observe(this){
            bindingNavView.iAvatar.loadImageUrl(it.authorAvatarUrl)
            bindingNavView.tFullname.text = it.username
            bindingNavView.tUsername.text = "@${it.userId}"
            bindingNavView.tFollowersCount.text = "${it.followersCount}"
            bindingNavView.tFollowingCount.text = "${it.followingCount}"
        }
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
        binding.tLogin.setOnClickListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
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
            R.id.t_login -> {
                if(!SharedPref.read(SharedPref.IS_LOGGED_IN, false)){
                    startActivity(Intent(this, LoginActivity::class.java))
                    overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    SharedPref.write(SharedPref.IS_LOGGED_IN, false)
                    setLoginTitle()
                }
            }
        }
    }
}