package com.example.messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel{

    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public UsersViewModel() {

        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {    
            @Override
            public void onAuthStateChanged(@NotNull FirebaseAuth firebaseAuth) {

                    user.setValue(firebaseAuth.getCurrentUser());
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null){
                    return;
                }
                List<User> usersFromDb = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user==null){
                        return;
                    }
                    if (!user.getId().equals(currentUser.getUid())){
                        usersFromDb.add(user);
                    }

                }
                users.setValue(usersFromDb);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });

    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void logout(){
        setUserOnline(false);
        auth.signOut();
    }


    public void setUserOnline(boolean isOnline){

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null){
            return;
        }

        databaseReference.child(firebaseUser.getUid()).child("online").setValue(isOnline);

    }


}
