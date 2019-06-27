package com.cursos.yatu.loginexample.ui.register;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.util.Patterns;

import com.cursos.yatu.loginexample.R;

import java.util.regex.Pattern;

/*
    Author: Yatu
    Time: 7:49 PM
    Date: 6/11/2019
*/public class RegisterFormViewModel extends ViewModel {

    private final String TAG = "RegisterFormViewModel";
    // Create a LiveData with a String
    private MutableLiveData<RegisterFormState> registerFormState;

    public MutableLiveData<RegisterFormState> getLiveData() {
        if (registerFormState == null) {
            registerFormState = new MutableLiveData<RegisterFormState>();
        }
        return registerFormState;
    }

    public void registerDataChanged(String firstName, String email, String birthday, String password,
                                    String repeatPassword) {
        Log.d (TAG, "Entra en registerDataChanged");
        if (!isFirstNameValid(firstName)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_firstName,
                    null, null, null, null));
        }else if (!isEmailValid(email)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email,
                    null,null, null));
        } else if (!isBirthdayValid(birthday)) {
            registerFormState.setValue(new RegisterFormState(null,
            null, R.string.invalid_birthday, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, null,
                    null, R.string.invalid_password, null));
        } else if (!isPasswordValid(repeatPassword)) {
            registerFormState.setValue(new RegisterFormState(null, null,
                    null, null, R.string.invalid_password));
        } else if (!password.equals(repeatPassword)) {
            registerFormState.setValue(new RegisterFormState(null, null,
                    null, null, R.string.password_not_match));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder firstName validation check
    private boolean isFirstNameValid(String firstName) {
        return !firstName.isEmpty();
    }

    // A placeholder birthday validation check
    private boolean isBirthdayValid(String birthday) {
        if (birthday == null) {
            return false;
        }
        if (birthday.length() == 10) {
            return Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}", birthday);
        } else {
            return false;
        }
    }

    // A placeholder email validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
