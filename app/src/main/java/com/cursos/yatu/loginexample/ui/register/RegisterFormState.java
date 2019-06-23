package com.cursos.yatu.loginexample.ui.register;

import android.support.annotation.Nullable;

/*
    Author: Yatu
    Time: 8:56 PM
    Date: 6/11/2019
*/public class RegisterFormState {
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer birthdayError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer repeatPasswordError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer firstNameError, @Nullable Integer emailError,
                      @Nullable Integer birthdayError, @Nullable Integer passwordError,
                      @Nullable Integer repeatPasswordError) {
        this.firstNameError = firstNameError;
        this.emailError = emailError;
        this.birthdayError = birthdayError;
        this.passwordError = passwordError;
        this.repeatPasswordError = repeatPasswordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.firstNameError = null;
        this.emailError = null;
        this.birthdayError = null;
        this.passwordError = null;
        this.repeatPasswordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    protected Integer getFirstNameError() {return firstNameError;}
    @Nullable
    protected Integer getEmailError() {
        return emailError;
    }
    @Nullable
    protected Integer getBirthdayError() { return birthdayError; }
    @Nullable
    protected Integer getPasswordError() {
        return passwordError;
    }
    @Nullable
    protected Integer getRepeatPasswordError() {
        return repeatPasswordError;
    }

    protected boolean isDataValid() {
        return isDataValid;
    }

}
