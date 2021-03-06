package com.example.earlybird.ui.main;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.earlybird.AccountEditActivity;
import com.example.earlybird.R;
import java.util.Objects;

public class Fragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View View = inflater.inflate(R.layout.fragment3_layout, container, false);

        //Get UserInfo from sharedPreferences
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String getUsername = sharedPreferences.getString("Username", null);
        String getEmail = sharedPreferences.getString("Email", null);
        String getGender = sharedPreferences.getString("Gender", null);
        String getFirstName = sharedPreferences.getString("FirstName", null);
        String getLastName = sharedPreferences.getString("LastName", null);

        //Initialize TextViews
        final TextView username = View.findViewById(R.id.username);
        final TextView userPassword = View.findViewById(R.id.userPassword);
        final TextView userEmail = View.findViewById(R.id.userEmail);
        final TextView userGender = View.findViewById(R.id.userGender);
        final TextView userFirstName = View.findViewById(R.id.userFirstName);
        final TextView userLastName = View.findViewById(R.id.userLastName);

        //Set text in TextViews
        username.setText(getUsername);
        userPassword.setText("******");
        userEmail.setText(getEmail);
        userGender.setText(getGender);
        userFirstName.setText(getFirstName);
        userLastName.setText(getLastName);

        final Button editAccount = View.findViewById(R.id.editAccount);
        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fragment3.this.getActivity(), AccountEditActivity.class);
                startActivity(intent);
            }
        });
        return View;
    }
}
