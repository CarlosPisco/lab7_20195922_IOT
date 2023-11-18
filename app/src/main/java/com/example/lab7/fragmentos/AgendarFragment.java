package com.example.lab7.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.lab7.R;
import com.example.lab7.databinding.FragmentAgendarBinding;
import com.example.lab7.databinding.FragmentDatosBinding;


public class AgendarFragment extends Fragment {

    FragmentAgendarBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgendarBinding.inflate(inflater,container,false);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>();

        String[] items = new String[4];

        items[0] = "Corte de Pelo";
        items[1] = "Aliasado";
        items[2] = "Teñido de pelo";
        items[3] = "Manicura/Pedicura";




        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,items);
        binding.spinner.setAdapter(adapter);



        return binding.getRoot();
    }
}