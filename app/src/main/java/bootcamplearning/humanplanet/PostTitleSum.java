package bootcamplearning.humanplanet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostTitleSum extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
EditText title,desc,details;
    Button button;
    String FIREBASE_STORAGE="gs://human-planet-4a5a5.appspot.com/";
    private static final int SELECT_PICTURE = 100;
    StorageReference imagesRef;
    StorageReference storageRef;
    FirebaseStorage storage;
    Uri downloadUrl;
    DatabaseReference postRef;
    DatabaseReference ref;
    String Firebasechild;
    String address,state,country,postalcode,knownname,city;
    ProgressBar progress;
    String item,Mainname;
    ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_title_sum);
        FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(FIREBASE_STORAGE);
        imagesRef = storageRef.child("images");
        final Spinner spinner = (Spinner) findViewById(R.id.eventspinner);
         title=(EditText)findViewById(R.id.eventtitle);
        desc=(EditText)findViewById(R.id.eventdesc);
        details=(EditText)findViewById(R.id.eventdetails);
        button=(Button)findViewById(R.id.eventpostbutton);
        spinner.setOnItemSelectedListener(this);
        mprogress=new ProgressDialog(this);

        final String state1=getIntent().getExtras().getString("state");
        final String country1=getIntent().getExtras().getString("country");
        final String postalcode1=getIntent().getExtras().getString("postalcode");
        final String knownplace1=getIntent().getExtras().getString("knownplace");
        final String address1=getIntent().getExtras().getString("address");
        final String city1=getIntent().getExtras().getString("city");

        address=address1;
        state=state1;
        country=country1;
        postalcode=postalcode1;
        knownname=knownplace1;
        city=city1;




        List<String> categories = new ArrayList<String>();
        categories.add("--Categories--");
        categories.add("Swach_Bharat");
        categories.add("Plant_A_tree");
        categories.add("Support_Animal_Cause");
        categories.add("Support_Child_Education");
        categories.add("Donate_Cloths_Food");
        categories.add("Orpange_Support");
        categories.add("Support_Girl_Child");
        categories.add("Support_Street_Children");
        categories.add("Support_Education");
        categories.add("Support_For_Greener_Planet");
        categories.add("Contribute_Your_Skills");
        categories.add("Support_An_NGO");
        categories.add("Blood_Donation_And_Health");
        categories.add("Form_Social_Collaborations");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    for (DataSnapshot single : child.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) single.getValue();
                        String name = (String) map.get("Name");
                        String email = (String) map.get("Email");
                        if ( email==null) {
                           // Toast.makeText(PostTitleSum.this, "Error in Name", Toast.LENGTH_SHORT).show();
                        } else if (email.equals(getIntent().getExtras().getString("stuff"))){
                            Mainname=name;
                            //Toast.makeText(PostTitleSum.this, Mainname, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (title.getText().toString().trim().length()<14)
                {
                    Toast.makeText(PostTitleSum.this, "Title Must Be Atleast 15 Letters", Toast.LENGTH_SHORT).show();
                }
               else if (title.getText().toString().trim().length()>38)
                {
                    Toast.makeText(PostTitleSum.this, "Title Must Be Within 38 Letters", Toast.LENGTH_SHORT).show();
                }

                else if (desc.getText().toString().trim().length()<25)
                {
                    Toast.makeText(PostTitleSum.this, "Description Must Be Atleast 25 Letters", Toast.LENGTH_SHORT).show();
                }
                else if (details.getText().toString().trim().length()<20)
                {
                    Toast.makeText(PostTitleSum.this, "Details Must Be Atleast 20 Letters", Toast.LENGTH_SHORT).show();
                }
                else if (item.equals("--Categories--"))
                {
                    Toast.makeText(PostTitleSum.this, "Please Select A Category", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {


                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_PICTURE);
                        progress = (ProgressBar) findViewById(R.id.eventprogress);
                        progress.setVisibility(View.VISIBLE);
                        title.setVisibility(View.INVISIBLE);
                        desc.setVisibility(View.INVISIBLE);
                        details.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        mprogress.setMessage("Please Wait");
                        mprogress.setTitle("Loading....");
                        mprogress.show();
                    }catch (Exception e)
                    {
                        Toast.makeText(PostTitleSum.this, "Could'nt Upload Image,Please Try Again", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });









    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i).toString();

        if (i==0)
        {
            Toast.makeText(adapterView.getContext(), "Please Select A Category", Toast.LENGTH_LONG).show();
        }
        else if (i==1)
        {
           Firebasechild=item;
        }
        else if (i==2)
        {
            Firebasechild=item;
        }
        else if (i==3)
        {
            Firebasechild=item;
        }
        else if (i==4)
        {
            Firebasechild=item;
        }
        else if (i==5)
        {
            Firebasechild=item;
        }
        else if (i==6)
        {
            Firebasechild=item;
        }
        else if (i==7)
        {
            Firebasechild=item;
        }
        else if (i==8)
        {
            Firebasechild=item;
        }
        else if (i==9)
        {
            Firebasechild=item;
        }
        else if (i==10)
        {
            Firebasechild=item;
        }
        else if (i==11)
        {
            Firebasechild=item;
        }
        else if (i==12)
        {
            Firebasechild=item;
        }
        else if (i==13)
        {
            Firebasechild=item;
        }
        else if (i==14)
        {
            Firebasechild=item;
        }
        else if (i==15)
        {
            Firebasechild=item;
        }


        //Toast.makeText(adapterView.getContext(), "You Selected: " + item, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                                Toast.makeText(getApplicationContext(), "Couldn't Upload,Please Try Again", Toast.LENGTH_LONG).show();

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Handle successful uploads on complete
                                postRef = ref.child("Pendingevents");
                                downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                                // Toast.makeText(getApplicationContext(), ""+downloadUrl, Toast.LENGTH_SHORT).show();


                                    //String stuff = getIntent().getExtras().getString("stuff");
                                    final Map<String, String> text = new HashMap<String, String>();
                                    text.put("Eventimage", "" + downloadUrl);
                                    text.put("EventerName", "" + Mainname);
                                    text.put("Eventaddress", "" + address);
                                    text.put("Eventstate", "" + state);
                                    text.put("Eventcity", "" + city);
                                    text.put("Eventpostalcode", "" + postalcode);
                                    text.put("Eventcountry", "" + country);
                                    text.put("Eventknownname", "" + knownname);
                                    text.put("Eventdesc", "" + desc.getText().toString().trim());
                                    text.put("Eventtitle", "" + title.getText().toString().trim());
                                    text.put("Eventdetails", "" + details.getText().toString().trim());
                                    text.put("Firebasechild", "" + Firebasechild);
                                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                                    Date todayDate = new Date();
                                    String thisDate = currentDate.format(todayDate);
                                    text.put("EventDate", "" + thisDate);
                                try {


                                    postRef.push().setValue(text);
                                    progress.setVisibility(View.INVISIBLE);
                                    mprogress.dismiss();
                                    AlertDialog.Builder alert = new AlertDialog.Builder(PostTitleSum.this);
                                    alert.setTitle("Info");
                                    alert.setMessage("Your Event Has Been Posted");
                                    alert.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                           // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            //intent.putExtra("stuff", getIntent().getExtras().getString("stuff"));
                                            //startActivity(intent);
                                            finish();
                                        }
                                    });
                                    AlertDialog showalert = alert.create();
                                    showalert.show();
                                }catch (Exception e)
                                {
                                    Toast.makeText(PostTitleSum.this, "Couldn't Upload Your Image Please Try Again", Toast.LENGTH_SHORT).show();
                                }

                                }



                        });


                    }
                }
            }
        }catch (Exception e)
        {
            Toast.makeText(PostTitleSum.this, "Please Select Image In Gallery,From Navigation Bar!", Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed() {

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            // builder.setCancelable(false);
            //builder.setTitle("Rate Us if u like this");
            builder.setMessage("Do you want to leave this page!");
          builder.setTitle("Message");
           builder.setCancelable(false);
            builder.setPositiveButton("Leave",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(getApplicationContext(), "Yes i wanna exit", Toast.LENGTH_LONG).show();

                    finish();
                }
            });
            builder.setNegativeButton("Stay",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getApplicationContext(), "i wanna stay on this page", Toast.LENGTH_LONG).show();
                    dialog.cancel();

                }
            });

            AlertDialog alert=builder.create();
            alert.show();



            //super.onBackPressed();
        }
    }




