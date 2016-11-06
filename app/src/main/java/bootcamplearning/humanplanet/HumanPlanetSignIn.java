package bootcamplearning.humanplanet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HumanPlanetSignIn extends AppCompatActivity {
Button button;
    String FIREBASE_STORAGE="gs://human-planet-4a5a5.appspot.com/";
    private static final int SELECT_PICTURE = 100;
    StorageReference imagesRef;
    StorageReference storageRef;
    FirebaseStorage storage;
    Uri downloadUrl;
    //String FIREBASE_URL="https://human-planet-4a5a5.firebaseio.com/";
    EditText email,name;
    String filename = "planethuman";
    DatabaseReference postRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_planet_sign_in);

        FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String country=getIntent().getExtras().getString("country");

        if (country==null)
        {
            Toast.makeText(HumanPlanetSignIn.this, "No Network Connection", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(i);
            finish();
        }


        postRef = ref.child("Users");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(FIREBASE_STORAGE);
        imagesRef = storageRef.child("images");
        button=(Button)findViewById(R.id.hpsignupb);
        email=(EditText)findViewById(R.id.email);
        name=(EditText)findViewById(R.id.name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (email.getText().toString()==null)
             {
                 Toast.makeText(HumanPlanetSignIn.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
             }
               else if (name.getText().toString()==null)
                {
                    Toast.makeText(HumanPlanetSignIn.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }
             else if (name.getText().toString().length()<4)
             {
                 Toast.makeText(HumanPlanetSignIn.this, "Name Must Be Atleast 5 Letters", Toast.LENGTH_SHORT).show();
             }
             else if (email.getText().toString().length()<12)
             {
                 Toast.makeText(HumanPlanetSignIn.this, "Please Enter Valid Mail Address", Toast.LENGTH_SHORT).show();
             }





                else {

                 Intent intent = new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 startActivityForResult(intent, SELECT_PICTURE);
                 try {


                     FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                     //assert Id != null;
                     outputStream.write(email.getText().toString().trim().getBytes());
                     outputStream.close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }


             }
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        String path = getPathFromURI(selectedImageUri);
                        //Log.i(TAG, "Image Path : " + path);
                        // Set the image in ImageView
                        //imageView.setText(path);
                        Uri file = Uri.fromFile(new File(path));

                        // Create the file metadata
                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("image/jpeg")
                                .build();

                        // Upload file and metadata to the path 'images/mountains.jpg'
                        UploadTask uploadTask;
                        uploadTask = imagesRef.child(file.getLastPathSegment()).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                //downloadUrl = taskSnapshot.getDownloadUrl();
                                Toast.makeText(getApplicationContext(), "Upload is " + progress + "% done", Toast.LENGTH_LONG).show();
                                System.out.println("Upload is " + progress + "% done");
                            }
                        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "Upload is Paused", Toast.LENGTH_LONG).show();

                                System.out.println("Upload is paused");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Handle successful uploads on complete
                                downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                                // Toast.makeText(getApplicationContext(), ""+downloadUrl, Toast.LENGTH_SHORT).show();
                               // String stuff = getIntent().getExtras().getString("stuff");

                                String address=getIntent().getExtras().getString("address");
                                String state=getIntent().getExtras().getString("state");
                                String city=getIntent().getExtras().getString("city");
                                String knownplace=getIntent().getExtras().getString("knownplace");
                                String country=getIntent().getExtras().getString("country");
                                String postalcode=getIntent().getExtras().getString("postalcode");
                                final Map<String, String> text = new HashMap<String, String>();
                                text.put("Profilemage", "" + downloadUrl);
                                text.put("Name", "" +name.getText().toString().trim());
                                text.put("Email", "" +email.getText().toString().trim());
                                text.put("Address", "" + address);
                                text.put("State", "" + state);
                                text.put("Country", "" + country);
                                text.put("City", "" + city);
                                text.put("Postalcode", "" + postalcode);
                                text.put("Knownplace", "" + knownplace);
                                postRef.push().setValue(text);
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.putExtra("stuff",email.getText().toString());
                                startActivity(i);
                                finish();


                            }
                        });


                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}
