package com.example.lab7;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab7.databinding.ActivityLoginBinding;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.ui.idp.AuthMethodPickerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private final static String TAG = "msg-test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            if(currentUser.isEmailVerified()){
                goToMainActivity();
            }
        }

        binding.loginBtn.setOnClickListener(view -> {

            binding.loginBtn.setEnabled(false);

            AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.custom_login)
                    .setGoogleButtonId(R.id.btn_login_google)
                    .setEmailButtonId(R.id.btn_login_mail)
                    .build();

            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.Base_Theme_Lab7)
                    .setIsSmartLockEnabled(false)
                    .setAuthMethodPickerLayout(authMethodPickerLayout)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new  AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .build();

            signInLauncher.launch(intent);

        });




    }


    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if(result.getResultCode() == RESULT_OK){

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();


                    //una vez el usuario ha sido logueado correctamente
                    if (user != null) {//verificar si el usuario salio de la app y ya no sigue logueado
                        Log.d(TAG, "Firebase uid: " + user.getUid());//para identificar al usuario
                        Log.d(TAG, "Display name: " + user.getDisplayName());
                        Log.d(TAG, "Email: " + user.getEmail());

                        //verificar si ya esta autenticado
                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()) {//verificamos la autenticacion del correo
                                goToMainActivity();
                            } else {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(LoginActivity.this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_SHORT).show();
                                    //
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, "user == null");
                    }
                } else {//si el usuario le pone atras sale del login
                    Log.d(TAG, "Canceló el Log-in");
                }
                binding.loginBtn.setEnabled(true);
            }



    );


    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, GestorActivity.class);
        startActivity(intent);
        finish();
    }
}