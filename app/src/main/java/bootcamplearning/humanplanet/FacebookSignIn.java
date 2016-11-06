package bootcamplearning.humanplanet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FacebookSignIn extends AppCompatActivity {
    private CallbackManager callbackManager;
    String filename = "planethuman";
    String text,name,image;
    DatabaseReference postRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_sign_in);
        FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String country1=getIntent().getExtras().getString("country");

        if (country1==null)
        {
            Toast.makeText(FacebookSignIn.this, "No Network Connection", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(i);
            finish();
        }

        postRef = ref.child("Users");
        final String address=getIntent().getExtras().getString("address");
        final String state=getIntent().getExtras().getString("state");
        final String city=getIntent().getExtras().getString("city");
        final String knownplace=getIntent().getExtras().getString("knownplace");
        final String country=getIntent().getExtras().getString("country");
        final String postalcode=getIntent().getExtras().getString("postalcode");

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                try {
                                    if (object != null) {
                                       // text  = object.getString("gender");
                                        name=object.getString("first_name")+" "+object.getString("last_name");
                                        URL img_value;
                                       String imageid=object.getString("id");
                                        try {
                                            img_value = new URL("http://graph.facebook.com/"+imageid+"/picture");
                                            image= String.valueOf(img_value);
                                           // Toast.makeText(FacebookSignIn.this, image, Toast.LENGTH_SHORT).show();
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }


                                        //Toast.makeText(FacebookSignIn.this, text, Toast.LENGTH_SHORT).show();

                                        try {
                                            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                            //assert Id != null;
                                            outputStream.write(name.getBytes());
                                            outputStream.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    final Map<String, String> text = new HashMap<String, String>();
                                    text.put("Profilemage", "" + image);
                                    text.put("Email", "" +name);
                                    text.put("Name", "" +name);
                                    text.put("Address", "" + address);
                                    text.put("State", "" + state);
                                    text.put("Country", "" + country);
                                    text.put("City", "" + city);
                                    text.put("Postalcode", "" + postalcode);
                                    text.put("Knownplace", "" + knownplace);
                                    postRef.push().setValue(text);
                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                   i.putExtra("stuff",name);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email, first_name, last_name, gender");
                request.setParameters(parameters);
                request.executeAsync();
                //Toast.makeText(FacebookSignIn.this, Id, Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookSignIn.this, "Login was interupted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(FacebookSignIn.this, "Error while logging in", Toast.LENGTH_SHORT).show();
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
