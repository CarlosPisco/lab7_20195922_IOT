package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab7.databinding.ActivityGestorBinding;
import com.example.lab7.databinding.ActivityMainBinding;
import com.example.lab7.fragmentos.CitasFragment;
import com.example.lab7.fragmentos.DatosFragment;
import com.firebase.ui.auth.AuthUI;

public class GestorActivity extends AppCompatActivity {

    ActivityGestorBinding binding;

    private boolean isCitasFragmentVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGestorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logoutBtn2.setOnClickListener(view -> {
            AuthUI.getInstance().signOut(GestorActivity.this)
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(GestorActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
        });

        //cambiar de fragmentos de datos y citas
        binding.button5.setOnClickListener(v -> {

            Fragment fragmentToReplace;

            if(isCitasFragmentVisible){
                fragmentToReplace = new DatosFragment();
                binding.button5.setText("Citas del día");
            } else{
                fragmentToReplace = new CitasFragment();
                binding.button5.setText("Datos del salon");

            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, fragmentToReplace)
                    .commit();

            isCitasFragmentVisible = !isCitasFragmentVisible;


        });











    }
}