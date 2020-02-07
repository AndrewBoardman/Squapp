package com.example.squapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.FederatedSignInOptions;
import com.amazonaws.mobile.client.IdentityProvider;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = VerifyActivity.class.getSimpleName();
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    public String server_client_id = "611515675807-tnh75dhbam7ove774n3iaf3q797unbq1.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //build a google sign in client with tthe options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart(){
        super.onStart();
        final Intent intent = new Intent(this, MainScreen.class);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        startActivity(intent);
    }

    public void signInButton(View view){

        final EditText login_username = findViewById(R.id.login_username);
        final EditText login_password = findViewById(R.id.login_password);

        final Intent intent = new Intent(this, MainScreen.class);

        final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(TAG, "Login successful, can get tokens here!");


             startActivity(intent);

            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                Log.i(TAG, "in getAuthenticationDetails()...");

                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId,
                        String.valueOf(login_password.getText()), null);

                authenticationContinuation.setAuthenticationDetails(authenticationDetails);

                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.i(TAG, "In authentication challenge.... " );
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, "Login failed: " + exception.getLocalizedMessage());
            }
        };

        CognitoSettings cognitoSettings = new CognitoSettings(LoginActivity.this);

        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(String.valueOf(login_username.getText()));

        Log.i(TAG, "in button clicked.....");

        thisUser.getSessionInBackground(authenticationHandler);

    }

    public void signUpbtn(View view) {

            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
    }

    public void forgotPwordBtn(View view){

            Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
            startActivity(intent);

    }

    public void google_sign_in(View view){
        switch (view.getId()) {
            case R.id.login_google:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            final Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
