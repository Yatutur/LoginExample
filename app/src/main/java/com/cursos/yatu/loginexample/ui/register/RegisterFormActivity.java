package com.cursos.yatu.loginexample.ui.register;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cursos.yatu.loginexample.R;
import com.cursos.yatu.loginexample.data.DialogSelectorDate;
import com.cursos.yatu.loginexample.data.MySingleton;
import com.cursos.yatu.loginexample.data.model.Connecting;
import com.cursos.yatu.loginexample.data.model.LoggedInUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "RegisterFormActivity";
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etRegEmail)
    EditText etEmail;
    @BindView(R.id.etBirthday)
    EditText etBirthday;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etRegPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;

    private Calendar mCalendar;
    LoggedInUser mUser;
    private MenuItem mMenuItem;

    private RegisterFormViewModel registerFormViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Begin onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        ButterKnife.bind(this);

        configActionBar();
        configUser();
        configCalendar();
        configListeners();

        // Get the ViewModel
        registerFormViewModel = getViewModelProvider().get(RegisterFormViewModel.class);

        // Create the observer which updates the UI.
        final Observer<RegisterFormState> observer = new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable final RegisterFormState registerFormState) {
                // Update the UI, in this case, a TextView.
                Log.d(TAG, "Entra en onChanged del observer");
                if (registerFormViewModel == null) {
                    return;
                }
                // Set enabled when data is valid
                if (mMenuItem != null) {
                    // TODO When turn screen is == null
                    mMenuItem.setEnabled(registerFormState.isDataValid());
                }
                if (registerFormState.getBirthdayError() != null) {
                    etBirthday.setError(getString(registerFormState.getBirthdayError()));
                }
                if (registerFormState.getEmailError() != null) {
                    etEmail.setError(getString(registerFormState.getEmailError()));
                }
                if (registerFormState.getFirstNameError() != null) {
                    etFirstName.setError(getString(registerFormState.getFirstNameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    etPassword.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getRepeatPasswordError() != null) {
                    etRepeatPassword.setError(getString(registerFormState.getRepeatPasswordError()));
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        registerFormViewModel.getLiveData().observe(this, observer);
        Log.d(TAG, "End onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "Begin onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_register, menu);

        MenuItem itActionSave = menu.findItem(R.id.btActionSave);
        if (itActionSave != null) {
            AppCompatButton button = (AppCompatButton) itActionSave.getActionView();
        }
        mMenuItem = itActionSave;
        mMenuItem.setEnabled(false);
        Log.d(TAG, "Before return onCreateOptionsMenu");
        return super.onCreateOptionsMenu(menu);
    }

    private void configUser() {
        mUser = new LoggedInUser();
        mUser.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void configActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configListeners() {
        etFirstName.addTextChangedListener((afterTextChangedListener));
        etEmail.addTextChangedListener(afterTextChangedListener);
        etBirthday.addTextChangedListener(afterTextChangedListener);
        etPassword.addTextChangedListener(afterTextChangedListener);
        etRepeatPassword.addTextChangedListener(afterTextChangedListener);
    }

    public ViewModelProvider getViewModelProvider() {
        ViewModelProvider.Factory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(getViewModelStore(), factory);
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
            registerFormViewModel.registerDataChanged(etFirstName.getText().toString(),
                    etEmail.getText().toString(), etBirthday.getText().toString(),
                    etPassword.getText().toString(), etRepeatPassword.getText().toString());
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btActionSave:
                saveUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUser() {
        Log.d(TAG, "Begin saveUser");
        StringRequest stringRequest;
        final String SERVER_URL =
                "https://yatuandroidserver.ddns.net:4546/login_example/userRegister.php";

        mUser.setFirstName(etFirstName.getText().toString().trim());
        mUser.setLastName(etLastName.getText().toString().trim());
        mUser.setEmail(etEmail.getText().toString().trim());
        mUser.setBirthday(etBirthday.getText().toString().trim());
        mUser.setPassword(etPassword.getText().toString().trim());
        mUser.setAddress(etAddress.getText().toString().trim());
        Date objDate = new Date();
        mUser.setNotes("Registered on " + objDate.toString());

        stringRequest = new StringRequest(Request.Method.POST, SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        Pattern pat = Pattern.compile("^1062: Duplicate entry .+ for key 'email'$");
                        Matcher matcher = pat.matcher(response.trim());
                        if (matcher.matches()) {
                            Toast.makeText(RegisterFormActivity.this,
                                    "Email '"+etEmail.getText().toString()+"' already exists",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterFormActivity.this,
                                    response, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Connecting.onErrorResponseGeneral(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstName", mUser.getFirstName());
                params.put("lastName", mUser.getLastName());
                params.put("birthday", String.valueOf(mUser.getBirthday()));
                params.put("email", mUser.getEmail());
                params.put("address", mUser.getAddress());
                params.put("password", mUser.getPassword());
                params.put("notes", mUser.getNotes());
                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(RegisterFormActivity.this).addToRequestQueue(stringRequest);
    }

    private void configCalendar() {
        mCalendar = Calendar.getInstance(Locale.ROOT);
        etBirthday.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(
                System.currentTimeMillis()));
        etBirthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @OnClick(R.id.etBirthday)
    public void onClickBirthday() {
        Log.d(TAG, "Entra en onClickbirthday");
        DialogSelectorDate selectorFecha = new DialogSelectorDate();
        selectorFecha.setListener(RegisterFormActivity.this);

        Bundle args = new Bundle();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(mUser.getBirthday());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        args.putLong(DialogSelectorDate.FECHA, date.getTime());
        selectorFecha.setArguments(args);
        selectorFecha.show(getSupportFragmentManager(), DialogSelectorDate.SELECTED_DATE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        etBirthday.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(
                mCalendar.getTimeInMillis()));
        mUser.setBirthday(etBirthday.getText().toString().trim());
    }
}
