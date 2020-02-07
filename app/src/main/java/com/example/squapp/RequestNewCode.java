package com.example.squapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;

public class RequestNewCode extends AppCompatActivity {

    private static final String TAG = "Cognito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_new_code);

    }
    final EditText new_code_username = findViewById(R.id.new_code_username);

    public void getNewCodeBtn(View view){
        CognitoSettings cognitoSettings = new CognitoSettings(RequestNewCode.this);
        CognitoUser thisUser = cognitoSettings.getUserPool()
                .getUser(String.valueOf(new_code_username.getText()));

        new ResendConfirmationCodeAsyncTask().execute(thisUser);

    }

    private class ResendConfirmationCodeAsyncTask extends AsyncTask<CognitoUser, Void, String>{

        @Override
        protected String doInBackground(CognitoUser... cognitoUsers){
            final String[] result = new String[1];

            VerificationHandler resendConfHandler = new VerificationHandler() {
                @Override
                public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                    result[0] = "Confirmation code successfuly sent to: "
                            + cognitoUserCodeDeliveryDetails.getDestination();
                }

                @Override
                public void onFailure(Exception exception) {
                    result[0] = exception.getLocalizedMessage();
                }
            };

            cognitoUsers[0].resendConfirmationCode(resendConfHandler);

            return result[0];
        }

        @Override
        protected void onPostExecute(String result){ //print to logcat
            super.onPostExecute(result);
            Log.i(TAG, "Resend verification result: " + result);
        }
    }
}

