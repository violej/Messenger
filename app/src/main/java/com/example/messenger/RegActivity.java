package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPasswordReg;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private Button buttonSignUp;

    private RegistrationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        String emailForReg = getTrimmedValue(editTextEmail);
                        String passwordForReg = getTrimmedValue(editTextPasswordReg);
                        String nameForReg = getTrimmedValue(editTextName);
                        String lastNameForReg = getTrimmedValue(editTextLastName);
                        int ageForReg = Integer.parseInt(getTrimmedValue(editTextAge));
                        viewModel.signUp(emailForReg,passwordForReg,nameForReg,lastNameForReg,ageForReg);
            }
        });
    }

    private void observeViewModel(){
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {

                if (errorMessage != null){
                    Toast.makeText(RegActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
                }

            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null){
                    Intent intent = UsersActivity.newIntent(RegActivity.this,firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void initViews(){

         editTextEmail = findViewById(R.id.editTextEmail);
         editTextPasswordReg = findViewById(R.id.editTextPasswordReg);
         editTextName = findViewById(R.id.editTextName);
         editTextLastName = findViewById(R.id.editTextLastName);
         editTextAge = findViewById(R.id.editTextAge);
         buttonSignUp = findViewById(R.id.buttonSignUp);

    }

    private String getTrimmedValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent newIntent(Context context){
        return new Intent(context,RegActivity.class);
    }
}