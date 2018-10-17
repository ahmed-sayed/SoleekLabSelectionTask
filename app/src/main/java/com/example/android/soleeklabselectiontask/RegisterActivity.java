package com.example.android.soleeklabselectiontask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Button mSign_in;
    private Button mRegister;
    public ProgressDialog mProgressDialog;


    private FirebaseAuth mAuth;

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.reg_email);
        mPasswordView = (EditText) findViewById(R.id.reg_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.reg_confirm_password);
        mSign_in = (Button) findViewById(R.id.reg_email_sign_in_button);
        mRegister = (Button) findViewById(R.id.reg_email_register_button);


        // User clicked Register btn
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Check if email address valid or not
                String email_input = new String(mEmailView.getText().toString());

                if (validateUserInput()) {
                    User mUser = new User(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    RegisterUser(mUser);

                    // navigate to home screen
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                }
            }
        });


        // User Clicked Sign in btn
        mSign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_in = new Intent(RegisterActivity.this, MainActivity.class);
                RegisterActivity.this.startActivity(log_in);
            }
        });


    }

    private void RegisterUser(User mUser) {

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(mUser.getmEmail(), mUser.getmPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "sign Up With Email:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            //updateUI(user);
                            Intent intent = new Intent(RegisterActivity.this, HomeScreen.class);
                            RegisterActivity.this.startActivity(intent);

                        } else {
                            // If Register fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "sign Up With Email:failure", Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean validateUserInput() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        // if email is null Or inValid then return false
        if (isValidEmailForm(email) == false) {
            Toast.makeText(RegisterActivity.this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
            valid = false;
        }

        String password = mPasswordView.getText().toString();
        // Check Password is not null && password.length() > 6 characters
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, " Empty Password, " + getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (password.length() <= 6) {
            Toast.makeText(RegisterActivity.this, getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
            valid = false;
        }


        String confirm_password = mConfirmPasswordView.getText().toString();
        // Check confirm Password is not  null && confirm Password == password
        if (TextUtils.isEmpty(confirm_password)) {
            Toast.makeText(RegisterActivity.this, " Empty Confirm Password, " + getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (TextUtils.equals(password, confirm_password) == false) {
            Toast.makeText(RegisterActivity.this, getString(R.string.error_incorrect_confirm_password), Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private boolean isValidEmailForm(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, " Empty Email, " + getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();

            return false;
        }
        return pat.matcher(email).matches();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true); // "loading amount" is not measured
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void updateUI(FirebaseUser User) {
        Intent intent = new Intent(RegisterActivity.this, HomeScreen.class);
        RegisterActivity.this.startActivity(intent);

    }
}
