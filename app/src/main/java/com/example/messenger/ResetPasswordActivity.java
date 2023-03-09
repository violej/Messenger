package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {
    
    private TextView textViewResPas;
    private EditText editTextEmailForReset;
    private Button buttonSendReset;

    private ResetPasswordViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();
        buttonSendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailForReset.getText().toString().trim();
                viewModel.resetPassword(email);
            }
        });

    }

    private void observeViewModel(){
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                    if (errorMessage != null){
                        Toast.makeText(ResetPasswordActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
                    }
            }
        });
        viewModel.isSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                    if (success){
                        Toast.makeText(ResetPasswordActivity.this
                                ,"The reset link has been sent to your email"
                                ,Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void initViews(){

        textViewResPas = findViewById(R.id.textViewResPas);
        editTextEmailForReset = findViewById(R.id.editTextEmailForReset);
        buttonSendReset = findViewById(R.id.buttonSendReset);
    }

    public static Intent newIntent(Context context){
        return new Intent(context,ResetPasswordActivity.class);

    }
}