package com.example.messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> otherUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSent = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUser = firebaseDatabase.getReference("Users");
    private DatabaseReference referenceMessages = firebaseDatabase.getReference("Messages");

    private String currentUserId;
    private String otherUserId;

    public ChatViewModel(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        referenceUser.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    otherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
        referenceMessages.child(currentUserId).child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                List<Message> messageList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messages.setValue(messageList);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
    }

    public MutableLiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getOtherUser() {
        return otherUser;
    }

    public LiveData<Boolean> getMessageSent() {
        return messageSent;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void setUserOnline(boolean isOnline){

        referenceUser.child(currentUserId).child("online").setValue(isOnline);

    }

    public void sendMessage(Message message){
        referenceMessages
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                referenceMessages
                        .child(message.getReceiverId())
                        .child(message.getSenderId())
                        .push()
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                        messageSent.setValue(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NotNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) {
                error.setValue(e.getMessage());
            }
        });
    }
}
