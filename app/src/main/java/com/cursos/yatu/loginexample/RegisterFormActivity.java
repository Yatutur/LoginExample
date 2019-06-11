package com.cursos.yatu.loginexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cursos.yatu.loginexample.data.DialogSelectorDate;
import com.cursos.yatu.loginexample.data.model.LoggedInUser;
import com.cursos.yatu.loginexample.ui.login.LoggedInUserView;
import com.cursos.yatu.loginexample.ui.login.LoginFormState;
import com.cursos.yatu.loginexample.ui.login.LoginResult;
import com.cursos.yatu.loginexample.ui.login.LoginViewModel;
import com.cursos.yatu.loginexample.ui.login.LoginViewModelFactory;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "RegisterFormActivity";
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etBirthdate)
    EditText etBirthdate;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.llData)
    LinearLayout llData;

    LoggedInUser mUser;
    private Calendar mCalendar;
    private MenuItem mMenuItem;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Comienza onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        ButterKnife.bind(this);

        configActionBar();
        configUser();
        configCalendar();

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                Log.d(TAG, "Entra en onChanged de FormState");
                if (loginFormState == null) {
                    return;
                }
                mMenuItem.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    etEmail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    etPassword.setError(getString(loginFormState.getPasswordError()));
                    etRepeatPassword.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                Log.d(TAG, "Entra en onChanged de Result");
                if (loginResult == null) {
                    return;
                }
//                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
//                finish();
            }
        });
    }

    TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            loginViewModel.registerDataChanged(etEmail.getText().toString(),
                    etPassword.getText().toString(), etRepeatPassword.getText().toString());
        }
    };

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + " " + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        mMenuItem = menu.findItem(R.id.action_save);
        mMenuItem.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void configUser() {
        mUser = new LoggedInUser();
        mUser.setBirthdate(System.currentTimeMillis());
    }

    private void configActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUser() {
        Log.d(TAG, "Entra en saveUser");
        mUser.setFirstName(etFirstName.getText().toString().trim());
        mUser.setLastName(etLastName.getText().toString().trim());
        mUser.setPassword(etPassword.getText().toString().trim());
        mUser.setEmail(etEmail.getText().toString().trim());
        mUser.setAddress(etAddress.getText().toString().trim());

        // Convertir en JSON
        JSONObject jsonUser;

    }

    private void configCalendar() {
        mCalendar = Calendar.getInstance(Locale.ROOT);
        etBirthdate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(
                System.currentTimeMillis()));
        etBirthdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @OnClick(R.id.etBirthdate)
    public void onClickBirthdate() {
        Log.d(TAG, "Entra en onClickBirthdate");
        DialogSelectorDate selectorFecha = new DialogSelectorDate();
        selectorFecha.setListener(RegisterFormActivity.this);

        Bundle args = new Bundle();
        args.putLong(DialogSelectorDate.FECHA, mUser.getBirthdate());
        selectorFecha.setArguments(args);
        selectorFecha.show(getSupportFragmentManager(), DialogSelectorDate.SELECTED_DATE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        etBirthdate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(
                mCalendar.getTimeInMillis()));
        mUser.setBirthdate(mCalendar.getTimeInMillis());
    }
}
