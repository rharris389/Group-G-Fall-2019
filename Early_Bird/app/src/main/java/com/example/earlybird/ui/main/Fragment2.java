package com.example.earlybird.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.earlybird.R;


public class Fragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View View = inflater.inflate(R.layout.fragment2_layout, container, false);

        //get username from sharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Username", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", null);
        Log.i("Username: ",username);

        return View;
    }
}
