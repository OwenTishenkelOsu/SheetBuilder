package com.example.sheetbuilder.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import com.example.sheetbuilder.model.Element;
import com.example.sheetbuilder.model.Sheet;
import com.example.sheetbuilder.model.SheetLiveData;
import com.example.sheetbuilder.model.User;
import com.example.sheetbuilder.repository.ElementRepository;
import com.example.sheetbuilder.repository.SheetRepository;
import com.example.sheetbuilder.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    public final UserRepository mRepository;

    public UserViewModel(Application app){
        super(app);
        mRepository = new UserRepository(app);
    }
    public List<User> getAllUsers(){
        List<User> mAllUsersList = mRepository.getAllUsers();
        return mAllUsersList;
    }
}
