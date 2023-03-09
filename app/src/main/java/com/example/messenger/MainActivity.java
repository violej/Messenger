package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private TextView textViewTitle;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonResetPassword;

    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setupCLickListeners();

    }

    private void setupCLickListeners(){

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextLogin.getText().length() == 0 || editTextPassword.getText().length() ==0){
                    Toast.makeText(MainActivity.this,"Пожалуйста заполните поля для ввода",Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = editTextLogin.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                viewModel.login(email,password);

            }
        });
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = ResetPasswordActivity.newIntent(MainActivity.this);
                intent.putExtra("enteredEmail",editTextLogin.getText().toString());
                startActivity(intent);


            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = RegActivity.newIntent(MainActivity.this);
                startActivity(intent);

            }
        });


    }

    private void observeViewModel(){
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage!=null){
                    Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser !=null){
                    Intent intent = UsersActivity.newIntent(MainActivity.this,firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initView(){
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

    }

    public static Intent newIntent (Context context){
        return new Intent(context, MainActivity.class);
    }
}