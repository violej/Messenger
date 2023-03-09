package com.example.messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

public class ResetPasswordViewModel extends ViewModel {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> success = new MutableLiveData<>();

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> isSuccess() {
        return success;
    }

    public void resetPassword(String email){

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                    success.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) {
                    error.setValue(e.getMessage());
            }
        });
    }

}
