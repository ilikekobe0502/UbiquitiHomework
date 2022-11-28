package com.example.ubiquitihomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.example.ubiquitihomework.databinding.ActivityMainBinding
import com.example.ubiquitihomework.ui.preview.PreviewFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val expandListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
            return true
        }

        override fun onMenuItemActionExpand(item: MenuItem): Boolean {
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PreviewFragment.newInstance())
                .commitNow()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.search)?.let { it ->
            val searchView = it.actionView as SearchView
            searchView.queryHint = getString(R.string.menu_search_hint)
            it.setOnActionExpandListener(expandListener)
        }

        return super.onCreateOptionsMenu(menu)
    }
}