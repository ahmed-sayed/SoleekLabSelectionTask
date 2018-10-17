package com.example.android.soleeklabselectiontask;

import android.app.ProgressDialog;
import android.content.Context;
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


public class MainActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mSign_in;
    private Button mRegister;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;



    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // .
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSign_in = (Button) findViewById(R.id.email_sign_in_button);
        mRegister = (Button) findViewById(R.id.email_register_button);


        // initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        // user clicked Sign in btn
        mSign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Check if email & Password valid or not
                if (validateUserInput()) {
                    User mUser = new User(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    signin_user(mUser);
                }
            }
        });

        // user clicked Register btn
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(reg);
            }
        });

    }

    private void signin_user(User mUser) {
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(mUser.getmEmail(), mUser.getmPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "sign In With Email:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "sign In With Email:failure", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity.this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
            valid = false;
        }

        String password = mPasswordView.getText().toString();
        // if Password is null then return false
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, " Empty Password, " + getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity.this, " Empty Email, " + getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();

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
        Intent intent = new Intent(MainActivity.this, HomeScreen.class);
        MainActivity.this.startActivity(intent);

    }

}
