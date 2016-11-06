package bootcamplearning.humanplanet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GoogleSignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "SignInTestActivity";


    private static final int OUR_REQUEST_CODE = 49404;
    private GoogleApiClient mGoogleApiClient;
    String filename = "planethuman";
    StringBuilder total1 = new StringBuilder();
    private ProgressDialog mConnectionProgressDialog;
    ProgressBar progressBar;
    DatabaseReference postRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);
        FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        postRef = ref.child("Users");
        SignInButton btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        String country=getIntent().getExtras().getString("country");

if (country==null)
{
    Toast.makeText(GoogleSignIn.this, "Couldn't Fetch Your Location,Check Network Connection", Toast.LENGTH_SHORT).show();
    Intent i=new Intent(getApplicationContext(),MapsActivity.class);
    startActivity(i);
    finish();
}


        btnSignIn.setOnClickListener(this);
        // First we need to configure the Google Sign In API to ensure we are retrieving
        // the server authentication code as well as authenticating the client locally.
        // String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // We pass through three "this" arguments to the builder, specifying the:
        // 1. Context
        // 2. Object to use for resolving connection errors
        // 3. Object to call onConnectionFailed on
        // We also add the Google Sign in API we previously created.

        mGoogleApiClient = new GoogleApiClient.Builder(this /* Context */)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Connect our sign in, sign out and disconnect buttons.


        // Configure the ProgressDialog that will be shown if there is a
        // delay in presenting the user with the next sign in step.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
    }



    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly. We can try and retrieve an
            // authentication code.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Checking sign in state...");
            progressDialog.show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    progressDialog.dismiss();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // When we get here in an automanager activity the error is likely not
        // resolvable - meaning Google Sign In and other Google APIs will be
        // unavailable.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {

        super.onActivityResult(requestCode, responseCode, intent);
        Log.v(TAG, "ActivityResult: " + requestCode);

        if (requestCode == OUR_REQUEST_CODE) {
            // Hide the progress dialog if its showing.
            mConnectionProgressDialog.dismiss();

            // Resolve the intent into a GoogleSignInResult we can process.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                Log.v(TAG, "Tapped sign in");
                // Show the dialog as we are now signing in.
                mConnectionProgressDialog.show();
                progressBar.setVisibility(View.VISIBLE);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, OUR_REQUEST_CODE);
                mConnectionProgressDialog.dismiss();
                break;

            default:
                // Unknown id.
                Log.v(TAG, "Unknown button press");
        }
    }

    /**
     * Helper method to trigger retrieving the server auth code if we've signed in.
     */
    private void handleSignInResult(GoogleSignInResult result ) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            // If you don't already have a server session, you can now send this code to your
            // server to authenticate on the backend.

            assert acct != null;
            String email = acct.getEmail();
            String name=acct.getDisplayName();
            String image=acct.getPhotoUrl().toString();

            //String outputString = Name.getText().toString();


            try {
                FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                assert email != null;
                outputStream.write(email.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String address=getIntent().getExtras().getString("address");
            String state=getIntent().getExtras().getString("state");
            String city=getIntent().getExtras().getString("city");
            String knownplace=getIntent().getExtras().getString("knownplace");
            String country=getIntent().getExtras().getString("country");
            String postalcode=getIntent().getExtras().getString("postalcode");
            final Map<String, String> text = new HashMap<String, String>();
            text.put("Profilemage", "" + image);
            text.put("Name", "" +name);
            text.put("Email", "" +email);
            text.put("Address", "" + address);
            text.put("State", "" + state);
            text.put("Country", "" + country);
            text.put("City", "" + city);
            text.put("Postalcode", "" + postalcode);
            text.put("Knownplace", "" + knownplace);
            postRef.push().setValue(text);
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            i.putExtra("stuff",email);
            startActivity(i);
            finish();

            // Hide the sign in buttons, show the sign out button.

        }
    }

}

