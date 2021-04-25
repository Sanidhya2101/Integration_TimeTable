package com.example.timetable_integration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.IAuthenticationProvider; //Imports the Graph sdk Auth interface
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.identity.client.AuthenticationCallback; // Imports MSAL auth methods
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.*;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import okhttp3.Request;


public class OutlookLoginActivity extends AppCompatActivity {

    private final static String[] SCOPES = {"User.Read"};
    /* Azure AD v2 Configs */
    final static String AUTHORITY = "https://login.microsoftonline.com/850aa78d-94e1-4bc6-9cf3-8c11b530701c";
    private ISingleAccountPublicClientApplication mSingleAccountApp;

    private static final String TAG = OutlookLoginActivity.class.getSimpleName();

    /* UI Variables */
    Button signInButton;
    TextView signingInText;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = OutlookLoginActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String user_email = sharedPref.getString("email", "");
        if(!user_email.equals("")){
            Log.d(TAG, user_email);
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_outlook_login);

        initializeUI();

/*        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.timetable_integration", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/

        PublicClientApplication.createSingleAccountPublicClientApplication(getApplicationContext(),
                R.raw.auth_config_single_account, new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        Log.d(TAG, "mSingleAcc.. = application");
                        mSingleAccountApp = application;
                        loadAccount();
                    }
                    @Override
                    public void onError(MsalException exception) {
                        Log.d(TAG, "mSingleAcc.. error");
                        displayError(exception);
                    }
                });


    }

    //When app comes to the foreground, load existing account to determine if user is signed in
    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.

                Log.d(TAG, "Account Loaded");
                updateUI(activeAccount);
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    final String signOutText = "Signed Out.";
                    Toast.makeText(getApplicationContext(), signOutText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                displayError(exception);
            }
        });
    }

    private void initializeUI(){
        signInButton = findViewById(R.id.signIn);
        signingInText = findViewById(R.id.signing_in);

        //Sign in user
        signInButton.setOnClickListener(v -> {
            Log.d(TAG, "Clicked Sign-in button");
            if (mSingleAccountApp == null) {
                return;
            }
            mSingleAccountApp.signIn(OutlookLoginActivity.this, null, SCOPES, getAuthInteractiveCallback());

            mSingleAccountApp.acquireToken(OutlookLoginActivity.this, SCOPES, getAuthInteractiveCallback());
        });

    }


    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                /* Update UI */
                updateUI(authenticationResult.getAccount());
                /* call graph */
                callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);
            }
            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /*private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d(TAG, "Successfully authenticated");
                callGraphAPI(authenticationResult);
            }
            @Override
            public void onError(MsalException exception) {
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);
            }
        };
    }*/

    private void callGraphAPI(IAuthenticationResult authenticationResult) {

        final String accessToken = authenticationResult.getAccessToken();

        IAuthenticationProvider iAuthenticationProvider = new IAuthenticationProvider() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Nonnull
            @Override
            public CompletableFuture<String> getAuthorizationTokenAsync(@Nonnull URL requestUrl) {
                return CompletableFuture.completedFuture(accessToken);
            }
        };

        Thread thread = new Thread(() -> {
            try  {
                GraphServiceClient<Request> graphClient =
                        GraphServiceClient
                                .builder()
                                .authenticationProvider(iAuthenticationProvider)
                                .buildClient();

                final User user = graphClient.me()
                        .buildRequest()
                        .get();

                //Log.d(TAG, user.department);

                Context context = OutlookLoginActivity.this;
                sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("programme", user.jobTitle);
                editor.putString("name",user.displayName);
                editor.putString("roll_no",user.surname);
                editor.putString("email",user.mail);
                editor.apply();
                Log.d(TAG, "All done!");

                startMainActivity();

            } catch (Exception e) {
                Log.d(TAG, "error in user");
                e.printStackTrace();
            }
        });
        thread.start();

    }

    private void updateUI(@Nullable final IAccount account) {
        if (account != null) {
            //signInButton.setVisibility(View.GONE);
            signingInText.setVisibility(View.VISIBLE);
            Log.d(TAG, account.getUsername());
        } else {
            signInButton.setVisibility(View.VISIBLE);
            signingInText.setVisibility(View.GONE);
        }
    }

    private void displayError(@NonNull final Exception exception) {
        Log.d(TAG, exception.toString());
    }

    private void performOperationOnSignOut() {
        if (mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                updateUI(null);
                final String signOutText = "Signed Out.";
                Toast.makeText(getApplicationContext(), signOutText, Toast.LENGTH_SHORT)
                        .show();
            }
            @Override
            public void onError(@NonNull MsalException exception){
                displayError(exception);
            }
        });

    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}