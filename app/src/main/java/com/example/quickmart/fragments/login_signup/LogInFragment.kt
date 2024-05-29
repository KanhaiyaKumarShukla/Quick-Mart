package com.example.quickmart.fragments.login_signup

import android.content.Intent
import android.content.IntentSender
import android.net.Credentials
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quickmart.R
import com.example.quickmart.databinding.FragmentLogInBinding
import com.example.quickmart.utils.AppConstant
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

// for backstack https://www.youtube.com/watch?v=V_LCfFVjs4c&list=PLRKyZvuMYSIMO2ebTldbwMTnDCn5klzjS&index=4
class LogInFragment : Fragment() {

    private var _binding:FragmentLogInBinding? =null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    //private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    //private var showOneTapUI = true
    private lateinit var callbackManager:CallbackManager

    // RC_SIGN_IN  is a request code used to identify the result of an activity started for a specific purpose. When you start an activity for a result using startActivityForResult(), you can specify a request code that you will use later to distinguish the result from other activities.
    // the value can be anything
    private val RC_SIGN_IN = 9001
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentLogInBinding.inflate(inflater, container, false)

        FacebookSdk.sdkInitialize(getApplicationContext());
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()

        oneTapClient = Identity.getSignInClient(requireContext())
        callbackManager = CallbackManager.Factory.create()

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.signUpNavigation.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        binding.googleLoginButton.setOnClickListener{
            signInWithGoogle()
        }
        binding.facebookLoginButton.setFragment(this)
        binding.facebookLoginButton.setPermissions("email", "public_profile")
        binding.facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onCancel() {
                Log.d("facebook login", "Facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("facebook login", "Facebook:onError ${error.message.toString()}")
            }

            override fun onSuccess(result: LoginResult) {
                Log.d("facebook login", "Facebook:onSuccess ${result.toString()}")
                handleFacebookAccessToken(result.accessToken)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("Facebook login token", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Facebook login token", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Facebook login token", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun login() {
        val email=binding.emailAddressEt.text.toString()
        val password=binding.passwordEt.text.toString()

        val verifyEmail= AppConstant.verifyEmail(email)
        val verifyPassword= AppConstant.verifyPassword(password)
        if(!verifyEmail.first and !verifyPassword.first){
            binding.emailTextInputLayout.error=verifyEmail.second.toString()
            binding.passwordTextInputLayout.error=verifyPassword.second.toString()
            return
        }else if(!verifyEmail.first){
            binding.emailTextInputLayout.error=verifyEmail.second
            return
        }else if(!verifyPassword.first){
            binding.passwordTextInputLayout.error=verifyPassword.second
            return
        }
/*
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("https://yourapp.page.link/reset?email=" + emailAddress)
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.yourcompany.yourapp")
            .setAndroidPackageName(
                "com.yourcompany.yourapp",
                true, /* installIfNotAvailable */
                "12" /* minimumVersion */
            )
            .build()
            */

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    verifyEmail()
                }else{
                    Toast.makeText(requireContext(), "Error creating user: ${it.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun verifyEmail(){
        //Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show()
        if(firebaseAuth.currentUser!!.isEmailVerified){
            // navigate to next fragment
            Toast.makeText(requireContext(), "Log In successfully. Please verify your email id", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_logInFragment_to_userProfileFragment)
        }else{
            Toast.makeText(requireContext(), "Please verify your email id", Toast.LENGTH_LONG).show()
        }
    }
    private fun signInWithGoogle() {
        //val signInIntent = googleSignInClient.signInIntent
        //startActivityForResult(signInIntent, RC_SIGN_IN)
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, RC_SIGN_IN,
                        null, 0, 0, 0, null
                    )
                    //googleSignInResultLauncher.launch(result.pendingIntent)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("GoogleSignIn", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("GoogleSignIn", "Sign-in failed: ${e.localizedMessage}")
            }
        //googleSignInResultLauncher.launch(signInIntent)
    }
//    private val googleSignInResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == AppCompatActivity.RESULT_OK) {
//            val data: Intent? = result.data
//            try {
//                val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)!!
//                Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                Log.w("GoogleSignIn", "Google sign in failed", e)
//                Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                Log.w("GoogleSignIn", "Google sign in failed", e)
//                Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT).show()
//            }
//        }
        when (requestCode) {
            RC_SIGN_IN -> {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate with Firebase.
                        Log.d("OneTapSignIn", "Got ID token.")
                        firebaseAuthWithGoogle(idToken)
                    }

                    else -> {
                        // Shouldn't happen.
                        Log.d("OneTapSignIn", "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                // Handle the error
                Log.e("OneTapSignIn", e.localizedMessage)
            }
            }
        }
         // Handle Facebook Sign-In result
         callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("GoogleSignIn", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            // redirect to the main fragment
            findNavController().navigate(R.id.action_logInFragment_to_userProfileFragment)
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            findNavController().navigate(R.id.action_logInFragment_to_userProfileFragment)
        }
    }
}
