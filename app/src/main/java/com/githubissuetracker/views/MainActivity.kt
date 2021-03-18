package com.githubissuetracker.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.githubissuetracker.R
import com.githubissuetracker.views.fragments.IssueFeedFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createFragments(IssueFeedFragment())
    }

    fun createFragments(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.javaClass.simpleName)
        transaction.add(R.id.container_body, fragment)
        transaction.commit()
    }
}