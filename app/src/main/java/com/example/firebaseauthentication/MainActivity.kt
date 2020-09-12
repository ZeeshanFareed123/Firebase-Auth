package com.example.firebaseauthentication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var providers: List<AuthUI.IdpConfig>
    private  val MY_REQUEST_CODE: Int= 7117

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
          //  AuthUI.IdpConfig.FacebookBuilder().build(),                   //don't know why facebook login is not working.....
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        showSignInOption()

        btnSignOut.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener{
                btnSignOut.isEnabled = false
            }.addOnFailureListener{
                e ->  Toast.makeText(this, ""+e.message , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser //get current user
                Toast.makeText(this, ""+user!!.email , Toast.LENGTH_LONG).show()
                btnSignOut.isEnabled = true
            }else{
                Toast.makeText(this, ""+response!!.error!!.message , Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showSignInOption() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().
                setAvailableProviders(providers).setTheme(R.style.MyTheme).build(), MY_REQUEST_CODE)
    }
}