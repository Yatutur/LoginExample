package com.cursos.yatu.loginexample.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.util.Patterns;

import com.cursos.yatu.loginexample.R;
import com.cursos.yatu.loginexample.data.LoginRepository;
import com.cursos.yatu.loginexample.data.Result;
import com.cursos.yatu.loginexample.data.model.LoggedInUser;
import com.cursos.yatu.loginexample.data.model.ServerCallback;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password, new ServerCallback() {
            @Override
            public void onSuccess(LoggedInUser result) {
                Log.d("LoginViewModel", "Login success");
                Result.Success<LoggedInUser> resultLocal = new Result.Success<>(result);
                LoggedInUser data = ((Result.Success<LoggedInUser>) resultLocal).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(),
                        data.getFirstName(), data.getLastName(), data.getBirthdate(),
                        data.getEmail(), data.getAddress(), data.getNotes())));
            }

            @Override
            public void onError() {
                Log.d("LoginViewModel", "Login failed");
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }

        });

    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void registerDataChanged(String email, String password, String repeatPassword) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if ((!isPasswordValid(password) || !isPasswordValid(repeatPassword))
                || password == repeatPassword) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
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
