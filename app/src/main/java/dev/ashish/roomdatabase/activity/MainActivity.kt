package dev.ashish.roomdatabase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ashish.roomdatabase.R
import dev.ashish.roomdatabase.adapter.UserDetailsAdapter
import dev.ashish.roomdatabase.adapter.UserDetailsAdapterListener
import dev.ashish.roomdatabase.databinding.ActivityMainBinding
import dev.ashish.roomdatabase.entity.UserDetails
import dev.ashish.roomdatabase.viewmodel.UserDetailsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UserDetailsAdapterListener {

    // Obtain ViewModel from ViewModelProviders
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
    }

    private lateinit var userDetailsAdapter: UserDetailsAdapter
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.handler = this

        setRecyclerViewProperties()
        observer()

    }

    private fun setRecyclerViewProperties() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        userDetailsAdapter = UserDetailsAdapter(this)
        recyclerView.adapter = userDetailsAdapter
    }

    private fun observer(){
        userViewModel.getAllDetails().observe(this, Observer {
            userDetailsAdapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menuDeleteAll) {
            userViewModel.deleteAll()
            Toast.makeText(this, "All deleted", Toast.LENGTH_SHORT).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // this func invoke when user click on FAB button
    fun addNew() {
        val intent = Intent(this, AddEditDetailsActivity::class.java)
        startActivity(intent)
    }

    // this func invoke when user click on delete icon from recyclerview
    override fun onItemDelete(userDetails: UserDetails) {
        userViewModel.delete(userDetails)
        Toast.makeText(
            this,
            "Details deleted", Toast.LENGTH_SHORT
        ).show()
    }

    // this func invoke when user click on cardview from recyclerview
    override fun onItemEdit(userDetails: UserDetails) {
        val intent = Intent(this, AddEditDetailsActivity::class.java)
        intent.putExtra("userDetails", userDetails)
        startActivity(intent)
    }

}
