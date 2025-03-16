package com.example.club_futbol_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.club_futbol_1.databinding.ActivityMainBinding
import com.example.club_futbol_1.model.Usuario
import com.example.club_futbol_1.ui.UserActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class   MainActivity : AppCompatActivity() {
    //atributos
    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initAtributes()
        loginUsuario()
    }



    private fun loginUsuario() {

        val currentUser = auth.currentUser
        Log.d("login","inicio")

        if (currentUser != null) {
            currentUser.reload()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {//usuario registrado
                        //    currentUser.displayName
                        abrirActividadUsuario(currentUser)
                        Log.d("login","registrado")

                    } else {// Usuario no válido (probablemente eliminado)
                        //registrarUsuario()
                        binding.contenedorRegistrarse.visibility=View.VISIBLE
                        Log.d("login","eliminado")
                        binding.btnRegistrarseGoogle.setOnClickListener {
                            Log.d("login","iniciar registro")

                            registrarUsuario()
                        }
                    }
                }
        } else {//usuario no logeado null
            binding.contenedorRegistrarse.visibility=View.VISIBLE
            binding.btnRegistrarseGoogle.setOnClickListener {
                Log.d("login","iniciar registro")

                registrarUsuario()
            }
        }

    }

    private fun abrirActividadUsuario(currentUser: FirebaseUser) {
        val intent = Intent(this,UserActivity::class.java)
        var usuario = Usuario(
            id = currentUser.uid,
            nombre = currentUser.displayName,
            urlImagen = currentUser.photoUrl.toString()
        )
        intent.putExtra("usuario",usuario)
        startActivity(intent)
    }

    private fun registrarUsuario() {
        val configuracion = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("518764826382-54eippm5r1un0ttap2pamo5g586e58jo.apps.googleusercontent.com") // Reemplaza con tu client ID
            .requestEmail()
            .build()
        var googleSignInClient = GoogleSignIn.getClient(this, configuracion)
        googleSignInClient.signOut().addOnCompleteListener {//Permite mostrar las cuentas con que el usuario puede acceder
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }
    }


    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            Log.d("login","ok")

        }else{//no cambia de actividad
            Log.d("login","canceled")
        }
    }
    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {//hubo problemas con el login
            val account = task.getResult(ApiException::class.java)!!
           firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w("login", "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in exitoso
                    auth.currentUser
                    abrirActividadUsuario(auth.currentUser!!)
                } else {
                    // Error en la autenticación
                    Log.w("login", "signInWithCredential:failure", task.exception)

                }
            }
    }

    private fun initAtributes() {
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

    }

}