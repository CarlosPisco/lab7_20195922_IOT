package com.example.lab7.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lab7.R;
import com.example.lab7.databinding.FragmentCitasBinding;
import com.example.lab7.databinding.FragmentDatosBinding;
import com.example.lab7.entities.Gestor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class DatosFragment extends Fragment {


    FragmentDatosBinding binding;

    FirebaseFirestore db;



    //Guardar Formulario de datos
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDatosBinding.inflate(inflater,container,false);

        db = FirebaseFirestore.getInstance();

        binding.button7.setOnClickListener(view ->{

            String correo = binding.editTextText4.getText().toString();
            String username = binding.editTextText5.getText().toString();

            List<String> listaServicios = new ArrayList<>();

            if(binding.checkBox.isChecked()){
                listaServicios.add("Corte de pelo");
            }
            if(binding.checkBox2.isChecked()){
                listaServicios.add("alisado");
            }
            if(binding.checkBox3.isChecked()){
                listaServicios.add("Tenido de pelo");
            }
            if(binding.checkBox4.isChecked()){
                listaServicios.add("Manicura/Pedicura");
            }

            //Se ha tomado los emails para poder tomar a los gestores
            //UID de gmail ya registrado
            Gestor gestor = new Gestor(correo,username, listaServicios);
            String uid;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                uid = user.getUid();
            }else{
                uid = "0000000";//cuando no se encuentra el usuario
            }

            db.collection("gestores")
                            .document(uid)
                            .set(gestor)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(getActivity(),"Los cambios han sido guardados exitosamente",Toast.LENGTH_SHORT).show();
                                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getActivity(),"No se pudieron guardar los cambios",Toast.LENGTH_SHORT).show();
                                                    });

        });



        return binding.getRoot();
    }
}