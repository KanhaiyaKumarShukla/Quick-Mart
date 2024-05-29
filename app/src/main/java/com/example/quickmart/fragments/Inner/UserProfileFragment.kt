package com.example.quickmart.fragments.Inner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.quickmart.R
import com.example.quickmart.databinding.FragmentUserProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UserProfileFragment : Fragment() {

    private var _binding:FragmentUserProfileBinding ?=null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()

        binding.updateEmail.setOnClickListener {

            showReauthenticationDialog {
                val mail: String = "7645992680s@gmail.com"

                firebaseAuth.currentUser?.verifyBeforeUpdateEmail(mail)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Email is successfully updated",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("email update", "Email is successfully updated")

                        } else {
                            Log.d("email update", "Email update failed")
                        }
                    }
                    ?.addOnFailureListener {
                        Log.d("email update", it.message.toString())
                    }
            }
        }
        binding.updateEmail.text=firebaseAuth.currentUser?.email.toString() + firebaseAuth.currentUser?.displayName.toString()

    }
    private fun showReauthenticationDialog(onSuccess: () -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reauthenticate, null)
        val passwordEditText = dialogView.findViewById<EditText>(R.id.password_EditText)

        AlertDialog.Builder(requireContext())
            .setTitle("Reauthenticate")
            .setMessage("Please enter your password to continue")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val password = passwordEditText.text.toString()
                reauthenticateUser(password, onSuccess)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun reauthenticateUser(password: String, onSuccess: () -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && currentUser.email != null) {
            val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
            currentUser.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        Log.d("reauthentication", "Reauthentication failed")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("reauthentication", exception.message.toString())
                }
        }
    }
}