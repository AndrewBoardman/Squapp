package com.example.squapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;

public class VerifyActivity extends AppCompatActivity {

    public static final String TAG = "Cognito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        final EditText verify_username1 = findViewById(R.id.verify_username);
        Intent i = getIntent();
        verify_username1.setText(i.getStringExtra("sName"));
        //verify_username1.setEnabled(false);
    }

     public void verifyBtn(View view){

        final EditText verify_code = findViewById(R.id.verify_code);
        final EditText verify_username = findViewById(R.id.verify_username);

        new ConfirmTask().execute(String.valueOf(verify_code.getText()),
        String.valueOf(verify_username.getText()));
    }

    private class ConfirmTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings){
            final String[] result = new String[1];

            final GenericHandler confirmationCallback = new GenericHandler() {
                @Override
                public void onSuccess() {
                    result[0] = "Succeeded";
                    Intent intent = new Intent(VerifyActivity.this, LoginActivity.class);
                    startActivity(intent);
               // startActivity(intent);
            }
                @Override
                public void onFailure(Exception exception) {
                    result[0]="Failed" + exception.getMessage();
                }
            };

        CognitoSettings cognitoSettings= new CognitoSettings(VerifyActivity.this);

        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(strings[1]);

        thisUser.confirmSignUp(strings[0], false, confirmationCallback);

        return result[0];
    }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            Log.i(TAG, "Confirmation result: " + result);
        }
    }
    public void verifyGoBackBtn(View view){
        Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
    }
}
