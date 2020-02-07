package com.example.squapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

public class SignUpActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
   //private final String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }
    public void signUpbtn(View view){

    final EditText signup_name = findViewById(R.id.signup_name);
    final EditText signup_username = findViewById(R.id.signup_username);
    final EditText signup_email = findViewById(R.id.signup_email);
    final EditText signup_password = findViewById(R.id.signup_password);
    final EditText signup_repeat_password = findViewById(R.id.signup_repeat_password);

    final Intent intent = new Intent(SignUpActivity.this, VerifyActivity.class);

    final Intent intent1 = new Intent(getApplicationContext(), VerifyActivity.class);
       // intent1.putExtra("sName", signup_username.getText().toString());
       // startActivity(intent1);

        String signName = signup_username.getText().toString();
        intent1.putExtra("sName", signName);
        startActivity(intent1);

    final CognitoUserAttributes userAttributes = new CognitoUserAttributes();

    final SignUpHandler signupCallback = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            //signup_name.setText("");

            startActivity(intent);

        }

        @Override
        public void onFailure(Exception exception) {

        }
    };
        userAttributes.addAttribute("name", String.valueOf(signup_name.getText()));
        //userAttributes.addAttribute("surname", String.valueOf(signup_username.getText()));
        userAttributes.addAttribute("email", String.valueOf(signup_email.getText()));

        CognitoSettings cognitoSettings = new CognitoSettings(SignUpActivity.this);

        cognitoSettings.getUserPool().signUpInBackground(String.valueOf(signup_username.getText()),
                String.valueOf(signup_password.getText()), userAttributes, null, signupCallback);

    }

    public void goback(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


    }
 }



