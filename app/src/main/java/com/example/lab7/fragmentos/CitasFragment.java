package com.example.lab7.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab7.R;
import com.example.lab7.databinding.FragmentCitasBinding;


public class CitasFragment extends Fragment {

    FragmentCitasBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCitasBinding.inflate(inflater,container,false);



        return binding.getRoot();
    }
}