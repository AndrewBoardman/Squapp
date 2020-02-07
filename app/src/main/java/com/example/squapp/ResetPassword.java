package com.example.squapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobile.auth.userpools.ForgotPasswordActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;

public class ResetPassword extends AppCompatActivity {

    private final String TAG = ResetPassword.class.getSimpleName();
    private ForgotPasswordContinuation resultContinuation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }

    final ForgotPasswordHandler callback = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            Log.i(TAG, "password changed successfully");
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation continuation) {
            Log.i(TAG, "in getreset code....");

            //A code will be sent. use continutation object to continue
            //with the forgot password process

            //This will indicate where the code was sent:
            CognitoUserCodeDeliveryDetails codeSentHere = continuation.getParameters();
            Log.i(TAG, "code sent here: " + codeSentHere.getDestination());

            //create a field so we can use the continuation object in our reset password button
            resultContinuation = continuation;
        }

        @Override
        public void onFailure(Exception exception) {
            Log.i(TAG, "password change failed: " + exception.getLocalizedMessage());
        }
     };

   public void getNewCodePwordBtn(View view){

       final EditText new_pword_username = findViewById(R.id.new_pword_username);

        CognitoSettings cognitoSettings = new CognitoSettings(ResetPassword.this);
        CognitoUser thisUser = cognitoSettings.getUserPool()
                .getUser(String.valueOf(new_pword_username.getText()));

        Log.i(TAG, "calling forgot password to get confirmation codee.....");
        thisUser.forgotPasswordInBackground(callback);
    }

    public void reset_password(View view){

        final EditText new_pword_enter_code = findViewById(R.id.new_pword_enter_code);
        final EditText new_pword_enter_new_password = findViewById(R.id.new_pword_enter_new_password);
        Log.i(TAG, "got code & password, setting continuation object");

        resultContinuation.setPassword(String.valueOf(new_pword_enter_new_password.getText()));
        resultContinuation.setPassword(String.valueOf(new_pword_enter_code.getText()));

        Log.i(TAG, "got code & password, calling continueTask()");

        //let the forget password process continue
        resultContinuation.continueTask();

    }
    }
