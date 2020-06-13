package com.mnn.wallpapex


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.view.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController?=null
    private var root: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_register, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setup navController
        navController = Navigation.findNavController(view)

        //check if user already logged in
        if (firebaseAuth.currentUser==null){
            //not logged in, create new account
            root!!.register_text.text = "Creating new account"
            firebaseAuth.signInAnonymously().addOnCompleteListener{
                if (it.isSuccessful){
                    //account created, go to home
                    root!!.register_text.text="Account created, logging in"
                    navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
                }else{
                    //error
                    root!!.register_text.text="Error: ${it.exception!!.message}"
                }
            }

        }else{
            //logged in, send back to homepage
            navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }
}
