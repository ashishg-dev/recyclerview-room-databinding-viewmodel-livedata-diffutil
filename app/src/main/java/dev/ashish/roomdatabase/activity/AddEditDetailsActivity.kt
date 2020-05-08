package dev.ashish.roomdatabase.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import dev.ashish.roomdatabase.R
import dev.ashish.roomdatabase.databinding.ActivityAddDetailsBinding
import dev.ashish.roomdatabase.entity.UserDetails
import dev.ashish.roomdatabase.viewmodel.UserDetailsViewModel
import kotlinx.android.synthetic.main.activity_add_details.*
import java.util.*

class AddEditDetailsActivity : AppCompatActivity() {

    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
    }

    private lateinit var userDetails: UserDetails
    private lateinit var activityAddDetailsBinding: ActivityAddDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_details)

        activityAddDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_add_details
        )
        activityAddDetailsBinding.addDetailsActivity = this

        setActionBarTitle("Add Details")

        readDetailsFromIntent()

    }

    private fun readDetailsFromIntent() {
        if (intent.hasExtra("userDetails")) {
            userDetails = intent.getParcelableExtra("userDetails")
            activityAddDetailsBinding.userDetails = userDetails
            setActionBarTitle("Edit Details")
        }
    }

    private fun setActionBarTitle(titleName : String){
        title = titleName
    }

    fun saveDetails() {

        if (validate()) {
            val userDetails = UserDetails(
                if (this::userDetails.isInitialized) userDetails.userId else
                    Random().nextInt(100),
                textInputEditTextFirstName.text.toString(),
                textInputEditTextLastName.text.toString(),
                textInputEditTextEmail.text.toString(),
                textInputEditTextMobile.text.toString()
            )

            if (this::userDetails.isInitialized) {
                userViewModel.update(userDetails)
                Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.insert(userDetails)
                Toast.makeText(this, "Save successfully", Toast.LENGTH_SHORT).show()
            }
            finish() // redirect to user listing activity to view the changes
        }

    }

    private fun validate(): Boolean {

        if (textInputEditTextFirstName.text.toString().trim().isBlank()) {
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (textInputEditTextLastName.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (textInputEditTextEmail.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (textInputEditTextMobile.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Mobile is required", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}
