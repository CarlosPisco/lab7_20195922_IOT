package com.example.lab7;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.lab7.databinding.ActivityMainBinding;
import com.example.lab7.entities.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore db;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.button2.setOnClickListener(v -> {

            String name = binding.editTextText.getText().toString();
            String dni = binding.editTextText2.getText().toString();
            Integer edad = Integer.valueOf(binding.editTextText3.getText().toString());

            Usuario usuario = new Usuario(name,edad);


            db.collection("usuarios")
                    .document(dni)
                    .set(usuario)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(MainActivity.this,"Se grabo correctamente",Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->{
                        Toast.makeText(MainActivity.this,"Hubo error",Toast.LENGTH_SHORT).show();
                    });





        });








        binding.logoutBtn.setOnClickListener(view -> {
            AuthUI.getInstance().signOut(MainActivity.this)
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
        });


        binding.button3.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            openDocumentLauncher.launch(intent);

        });

        binding.button4.setOnClickListener(v -> {

            if(checkSelfPermission((android.Manifest.permission.WRITE_EXTERNAL_STORAGE))== PackageManager.PERMISSION_GRANTED){
                descargarGuardar();
            }else {
                String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                requestReadPermissionLauncher.launch(permission);
            }


        });

        listarArchivos();
    }
    ActivityResultLauncher<String> requestReadPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    descargarGuardar();
                }
            }
    );

    ActivityResultLauncher<Intent> openDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri uri = result.getData().getData();
                    System.out.println(uri);
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference imageRef = firebaseStorage.getReference().child("img/" + "doc.pdf");

                    imageRef.putFile(uri)
                            .addOnSuccessListener(taskSnapshot -> Toast.makeText(MainActivity.this,"Archivo subido correctamente",Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Log.d("msg", "error", e.getCause()))
                            .addOnProgressListener(snapshot -> {
                                long bytesTransferred = snapshot.getBytesTransferred();
                                long totalByteCount = snapshot.getTotalByteCount();
                                double porcentajeSubida = Math.round((bytesTransferred * 1.0f / totalByteCount) * 100);

                                binding.textView4.setText(porcentajeSubida + "%");
                            });
                }
            }
    );


    private void descargarGuardar () {

       File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //File directorio = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "doc.pdf");

        File localFile = new File(directorio,"archivo.pdf");

       StorageReference docref = storage.getReference().child("img").child("doc.pdf");

       //Comprobar si el archivo existe
        docref.getMetadata()
                        .addOnSuccessListener(storageMetadata -> Log.d("msg-test","existe el archivo"))
                                .addOnFailureListener(e -> Log.d("msg-test","no existe el archivo"));
        Log.d("msg-test", "Ruta de descarga: " + directorio.getAbsolutePath());


        docref.getFile(localFile)
               .addOnSuccessListener(taskSnapshot -> {
                  Log.d("msg-test","archivo descargado");
                  Toast.makeText(MainActivity.this,"Archivo descargado correctamente",Toast.LENGTH_SHORT).show();
               })
               .addOnFailureListener(e -> {
                   Log.d("msg-test","error",e.getCause());
               });



        /*if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File localFile = new File(directorio, "golden.png");

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference docRef = firebaseStorage.getReference().child("img/" + "golden.png");

                docRef.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> Log.d("msg", "archivo descargado"))
                        .addOnFailureListener(e -> Log.d("msg", "error", e.getCause()));


            }
        }*/
    }

    public void listarArchivos(){
        StorageReference listRef = storage.getReference().child("img");

        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    //Con getPrefixes es la manera adecuada de sacar informacion
                    String[] items = new String[listResult.getItems().size()];
                    int i = 0;

                    for (StorageReference item : listResult.getItems()) {
                        // All the items under listRef.
                        Log.d("msg-test","item.getName(): " + item.getName());//nombre de los archivos
                        Log.d("msg-test","item.getPath(): " + item.getPath());// ruta de los archivos
                        items[i++] = item.getName();
                    }


                })
                .addOnFailureListener(e -> e.printStackTrace());
    }

}