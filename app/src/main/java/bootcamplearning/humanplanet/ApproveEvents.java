package bootcamplearning.humanplanet;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApproveEvents extends AppCompatActivity{
    TextView visiblename, visibleeventdate, visibletitle, visibledescription;
    ImageView visibleeventerimage, visibleeventimage;
    Button takeaction,viewinmap;
    DatabaseReference ref;
    DatabaseReference postRef,postRefR;
    String Name;
    private GoogleMap mMap;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    String lati, longi;
    Map<String,String>text1=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_events);
        FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        postRefR=ref.child("Recent_Events");
        visiblename = (TextView) findViewById(R.id.visiblename);
        visibleeventdate = (TextView) findViewById(R.id.visibleeventdate);
        visibletitle = (TextView) findViewById(R.id.visibletitle);
        visibledescription = (TextView) findViewById(R.id.visibledescription);
        visibleeventimage = (ImageView) findViewById(R.id.visibleeventimage);

        viewinmap=(Button)findViewById(R.id.viewinmapb);
        takeaction=(Button)findViewById(R.id.actionbutton);


        final String Title = getIntent().getExtras().getString("stufftitle");
        Name = getIntent().getExtras().getString("stuffname");
        final String Desc = getIntent().getExtras().getString("stuffdesc");
        final String Image = getIntent().getExtras().getString("stuffimage");
        final String Date = getIntent().getExtras().getString("stuffdate");
        final String State = getIntent().getExtras().getString("stuffstate");
        final String Postalcode = getIntent().getExtras().getString("stuffpostalcode");
        final String Knownplace = getIntent().getExtras().getString("stuffknownplace");
        final String City = getIntent().getExtras().getString("stuffcity");
        final String Details = getIntent().getExtras().getString("stuffdetails");
        final String Address = getIntent().getExtras().getString("stuffaddress");
        final String Country = getIntent().getExtras().getString("stuffcountry");
        final String Firebasechild = getIntent().getExtras().getString("stufffirebasechild");

        visiblename.setText(Name);
        visibleeventdate.setText(Date);
        visibletitle.setText(Title);
        visibledescription.setText(Desc + "\n\n" + Details + "\n\n" + "Approximate Location :" + "\n\n" + City + "\n" + Knownplace + "\n" + Address + "\n" + State + "\n" + Postalcode + "\n" + Country);

        Glide.with(this).load(Image).into(visibleeventimage);

        visibleeventimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ViewFullImage.class);
                i.putExtra("stuff",Image);
                startActivity(i);
            }
        });

        if(Geocoder.isPresent()) {
            try {

                Geocoder gc = new Geocoder(ApproveEvents.this);
                List<android.location.Address> addresses = gc.getFromLocationName(City, 5); // get the found Address Objects

                List<Double> ll = new ArrayList<Double>(addresses.size()); // A list to save the coordinates if they are available
                for (android.location.Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(a.getLatitude());
                        ll.add(a.getLongitude());
                        lati = String.valueOf(ll.get(0));
                        longi = String.valueOf(ll.get(1));
                        //Toast.makeText(ApproveEvents.this, lati + longi, Toast.LENGTH_SHORT).show();


                    }

                }
            } catch (IOException e) {
                // handle the exception
            }
        }
        viewinmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ViewInMap.class);

                i.putExtra("Lati",lati);
                i.putExtra("Longi",longi);
                startActivity(i);


            }
        });


        takeaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String,String>text=new HashMap<String,String>();

                AlertDialog.Builder alert=new AlertDialog.Builder(ApproveEvents.this);
                alert.setTitle("Take Action");
                alert.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
if (Firebasechild==null)
{
    Toast.makeText(ApproveEvents.this, "This Event Cannot Be Approved", Toast.LENGTH_SHORT).show();
}

               else if (Firebasechild.equals("Swach_Bharat")) {
try{

                    postRef = ref.child(Firebasechild);
                    text.put("Namesb", "" +Name);
                    text.put("Titlesb", "" +Title);
                    text.put("Descsb", "" +Desc);
                    text.put("Imagesb", "" +Image);
                    text.put("Datesb", "" +Date);
                    text.put("Statesb", "" +State);
                    text.put("Postalcodesb", "" +Postalcode);
                    text.put("Knownplacesb", "" +Knownplace);
                    text.put("Citysb", "" +City);
                    text.put("Detailssb", "" +Details);
                    text.put("Addresssb", "" +Address);
                    text.put("Countrysb", "" +Country);
                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);

    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
               else if (Firebasechild.equals("Plant_A_tree")) {
try{

                    postRef = ref.child(Firebasechild);
                    text.put("Namepat", "" +Name);
                    text.put("Titlepat", "" +Title);
                    text.put("Descpat", "" +Desc);
                    text.put("Imagepat", "" +Image);
                    text.put("Datepat", "" +Date);
                    text.put("Statepat", "" +State);
                    text.put("Postalcodepat", "" +Postalcode);
                    text.put("Knownplacepat", "" +Knownplace);
                    text.put("Citypat", "" +City);
                    text.put("Detailspat", "" +Details);
                    text.put("Addresspat", "" +Address);
                    text.put("Countrypat", "" +Country);
                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);
    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Support_Animal_Cause")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namesac", "" +Name);
                    text.put("Titlesac", "" +Title);
                    text.put("Descsac", "" +Desc);
                    text.put("Imagesac", "" +Image);
                    text.put("Datesac", "" +Date);
                    text.put("Statesac", "" +State);
                    text.put("Postalcodesac", "" +Postalcode);
                    text.put("Knownplacesac", "" +Knownplace);
                    text.put("Citysac", "" +City);
                    text.put("Detailssac", "" +Details);
                    text.put("Addresssac", "" +Address);
                    text.put("Countrysac", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Support_Child_Education")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namesce", "" +Name);
                    text.put("Titlesce", "" +Title);
                    text.put("Descsce", "" +Desc);
                    text.put("Imagesce", "" +Image);
                    text.put("Datesce", "" +Date);
                    text.put("Statesce", "" +State);
                    text.put("Postalcodesce", "" +Postalcode);
                    text.put("Knownplacesce", "" +Knownplace);
                    text.put("Citysce", "" +City);
                    text.put("Detailssce", "" +Details);
                    text.put("Addresssce", "" +Address);
                    text.put("Countrysce", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Donate_Cloths_Food")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namedcf", "" +Name);
                    text.put("Titledcf", "" +Title);
                    text.put("Descdcf", "" +Desc);
                    text.put("Imagedcf", "" +Image);
                    text.put("Datedcf", "" +Date);
                    text.put("Statedcf", "" +State);
                    text.put("Postalcodedcf", "" +Postalcode);
                    text.put("Knownplacedcf", "" +Knownplace);
                    text.put("Citydcf", "" +City);
                    text.put("Detailsdcf", "" +Details);
                    text.put("Addressdcf", "" +Address);
                    text.put("Countrydcf", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Orpange_Support")) {
try{

                    postRef = ref.child(Firebasechild);
                    text.put("Nameos", "" +Name);
                    text.put("Titleos", "" +Title);
                    text.put("Descos", "" +Desc);
                    text.put("Imageos", "" +Image);
                    text.put("Dateos", "" +Date);
                    text.put("Stateos", "" +State);
                    text.put("Postalcodeos", "" +Postalcode);
                    text.put("Knownplaceos", "" +Knownplace);
                    text.put("Cityos", "" +City);
                    text.put("Detailsos", "" +Details);
                    text.put("Addressos", "" +Address);
                    text.put("Countryos", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Support_Girl_Child")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namesgc", "" +Name);
                    text.put("Titlesgc", "" +Title);
                    text.put("Descsgc", "" +Desc);
                    text.put("Imagesgc", "" +Image);
                    text.put("Datesgc", "" +Date);
                    text.put("Statesgc", "" +State);
                    text.put("Postalcodesgc", "" +Postalcode);
                    text.put("Knownplacesgc", "" +Knownplace);
                    text.put("Citysgc", "" +City);
                    text.put("Detailssgc", "" +Details);
                    text.put("Addresssgc", "" +Address);
                    text.put("Countrysgc", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Support_Street_Children")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namessc", "" +Name);
                    text.put("Titlessc", "" +Title);
                    text.put("Descssc", "" +Desc);
                    text.put("Imagessc", "" +Image);
                    text.put("Datessc", "" +Date);
                    text.put("Statessc", "" +State);
                    text.put("Postalcodessc", "" +Postalcode);
                    text.put("Knownplacessc", "" +Knownplace);
                    text.put("Cityssc", "" +City);
                    text.put("Detailsssc", "" +Details);
                    text.put("Addressssc", "" +Address);
                    text.put("Countryssc", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);
    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Support_Education")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namese", "" +Name);
                    text.put("Titlese", "" +Title);
                    text.put("Descse", "" +Desc);
                    text.put("Imagese", "" +Image);
                    text.put("Datese", "" +Date);
                    text.put("Statese", "" +State);
                    text.put("Postalcodese", "" +Postalcode);
                    text.put("Knownplacese", "" +Knownplace);
                    text.put("Cityse", "" +City);
                    text.put("Detailsse", "" +Details);
                    text.put("Addressse", "" +Address);
                    text.put("Countryse", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Support_For_Greener_Planet")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namesfgp", "" +Name);
                    text.put("Titlesfgp", "" +Title);
                    text.put("Descsfgp", "" +Desc);
                    text.put("Imagesfgp", "" +Image);
                    text.put("Datesfgp", "" +Date);
                    text.put("Statesfgp", "" +State);
                    text.put("Postalcodesfgp", "" +Postalcode);
                    text.put("Knownplacesfgp", "" +Knownplace);
                    text.put("Citysfgp", "" +City);
                    text.put("Detailssfgp", "" +Details);
                    text.put("Addresssfgp", "" +Address);
                    text.put("Countrysfgp", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Contribute_Your_Skills")) {
try{

                    postRef = ref.child(Firebasechild);
                    text.put("Namecys", "" +Name);
                    text.put("Titlecys", "" +Title);
                    text.put("Desccys", "" +Desc);
                    text.put("Imagecys", "" +Image);
                    text.put("Datecys", "" +Date);
                    text.put("Statecys", "" +State);
                    text.put("Postalcodecys", "" +Postalcode);
                    text.put("Knownplacecys", "" +Knownplace);
                    text.put("Citycys", "" +City);
                    text.put("Detailscys", "" +Details);
                    text.put("Addresscys", "" +Address);
                    text.put("Countrycys", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Support_An_NGO")) {
try{

                    postRef = ref.child(Firebasechild);
                    text.put("Namesan", "" +Name);
                    text.put("Titlesan", "" +Title);
                    text.put("Descsan", "" +Desc);
                    text.put("Imagesan", "" +Image);
                    text.put("Datesan", "" +Date);
                    text.put("Statesan", "" +State);
                    text.put("Postalcodesan", "" +Postalcode);
                    text.put("Knownplacesan", "" +Knownplace);
                    text.put("Citysan", "" +City);
                    text.put("Detailssan", "" +Details);
                    text.put("Addresssan", "" +Address);
                    text.put("Countrysan", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Blood_Donation_And_Health")) {

try{
                    postRef = ref.child(Firebasechild);
                    text.put("Namebdah", "" +Name);
                    text.put("Titlebdah", "" +Title);
                    text.put("Descbdah", "" +Desc);
                    text.put("Imagebdah", "" +Image);
                    text.put("Datebdah", "" +Date);
                    text.put("Statebdah", "" +State);
                    text.put("Postalcodebdah", "" +Postalcode);
                    text.put("Knownplacebdah", "" +Knownplace);
                    text.put("Citybdah", "" +City);
                    text.put("Detailsbdah", "" +Details);
                    text.put("Addressbdah", "" +Address);
                    text.put("Countrybdah", "" +Country);


                    postRef.push().setValue(text);
    text1.put("RName", "" +Name);
    text1.put("RTitle", "" +Title);
    text1.put("RDesc", "" +Desc);
    text1.put("RImage", "" +Image);
    text1.put("RDate", "" +Date);
    text1.put("RState", "" +State);
    text1.put("RPostalcode", "" +Postalcode);
    text1.put("RKnownplace", "" +Knownplace);
    text1.put("RCity", "" +City);
    text1.put("RDetails", "" +Details);
    text1.put("RAddress", "" +Address);
    text1.put("RCountry", "" +Country);

    postRefR.push().setValue(text1);
    try {
        final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().removeValue();

                    Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }catch (Exception e)
    {
        Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
    }

                }catch (Exception e)
                        {

                        }
                }
                else if (Firebasechild.equals("Form_Social_Collaborations")) {

                  try {
                      postRef = ref.child(Firebasechild);
                      text.put("Namefsc", "" + Name);
                      text.put("Titlefsc", "" + Title);
                      text.put("Descfsc", "" + Desc);
                      text.put("Imagefsc", "" + Image);
                      text.put("Datefsc", "" + Date);
                      text.put("Statefsc", "" + State);
                      text.put("Postalcodefsc", "" + Postalcode);
                      text.put("Knownplacefsc", "" + Knownplace);
                      text.put("Cityfsc", "" + City);
                      text.put("Detailsfsc", "" + Details);
                      text.put("Addressfsc", "" + Address);
                      text.put("Countryfsc", "" + Country);


                      postRef.push().setValue(text);
                      text1.put("RName", "" +Name);
                      text1.put("RTitle", "" +Title);
                      text1.put("RDesc", "" +Desc);
                      text1.put("RImage", "" +Image);
                      text1.put("RDate", "" +Date);
                      text1.put("RState", "" +State);
                      text1.put("RPostalcode", "" +Postalcode);
                      text1.put("RKnownplace", "" +Knownplace);
                      text1.put("RCity", "" +City);
                      text1.put("RDetails", "" +Details);
                      text1.put("RAddress", "" +Address);
                      text1.put("RCountry", "" +Country);
                      postRefR.push().setValue(text1);
                      try {
                          final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);

                          postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {

                                  for (DataSnapshot child : dataSnapshot.getChildren()) {
                                      child.getRef().removeValue();

                                      Toast.makeText(getApplicationContext(), "Event Approved", Toast.LENGTH_SHORT).show();
                                      Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                                      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                      startActivity(intent);
                                      finish();

                                  }


                              }

                              @Override
                              public void onCancelled(DatabaseError firebaseError) {

                              }
                          });

                      }catch (Exception e)
                      {
                          Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
                      }

                  }catch (Exception e)
                  {

                  }
                }
                else
                {
                    Toast.makeText(ApproveEvents.this, "This Post Cand Be Approved,Insufficient Details", Toast.LENGTH_SHORT).show();
                }

                    }
                });

                alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        try {
                            final Query postRef = ref.child("Pendingevents").orderByChild("Eventtitle").equalTo(Title);


                            postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        child.getRef().removeValue();

                                        Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(getApplicationContext(),PendingEvents.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                        finish();
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError firebaseError) {

                                }
                            });

                        }catch (Exception e)
                        {
                            Toast.makeText(ApproveEvents.this, "Unknown Error,please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog build=alert.create();
                build.show();
            }
        });



       /* ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    for (DataSnapshot single : child.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) single.getValue();
                        String name = (String) map.get("Name");
                        String image = (String) map.get("Profilemage");
                        if (name == null) {

                        } else if (name.equals(Name)){
                            //Glide.with(ApproveEvents.this).load(image).into(visibleeventerimage);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        }); */

    }
   }