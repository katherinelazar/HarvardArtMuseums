package hu.bme.aut.bottomnavfragmentsdemo

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.aut.bottomnavfragmentsdemo.fragments.FragmentFriends
import hu.bme.aut.bottomnavfragmentsdemo.fragments.FragmentGallery
import hu.bme.aut.bottomnavfragmentsdemo.fragments.FragmentFavorites



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.app_title)

        navigation.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_gallery

        showFragmentByTag(FragmentGallery.TAG, true)
    }

    public fun showFragmentByTag(tag: String, toBackStack: Boolean) {
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            if (FragmentFavorites.TAG == tag) {
                fragment = FragmentFavorites()
            } else if (FragmentGallery.TAG == tag) {
                fragment = FragmentGallery()
            } else if (FragmentFriends.TAG == tag) {
                fragment = FragmentFriends()
            }
        }

        if (fragment != null) {
            val ft = supportFragmentManager
                .beginTransaction()
            ft.replace(R.id.fragmentContainer, fragment, tag)
            if (toBackStack) {
                ft.addToBackStack(null)
            }
            ft.commit()
        }
    }

    private val myOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showFragmentByTag(FragmentFavorites.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gallery -> {
                showFragmentByTag(FragmentGallery.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                showFragmentByTag(FragmentFriends.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
