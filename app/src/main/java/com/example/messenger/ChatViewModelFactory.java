package com.example.messenger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;



public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private String currentUserId;
    private String otherUserId;

    public ChatViewModelFactory(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {


        return (T) new ChatViewModel(currentUserId,otherUserId);
    }
}
