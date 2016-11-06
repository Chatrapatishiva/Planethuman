package bootcamplearning.humanplanet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public MainActivity() {
        // Required empty public constructor

    }

    DatabaseReference ref;
    DatabaseReference postRef;
    String name,date,image,address,state,country,postalcode,knownplace,city,firebasechild,desc,details,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            FirebaseDatabase.getInstance();



        ref = FirebaseDatabase.getInstance().getReference();
        final String stuff=getIntent().getExtras().getString("stuff");
        ListView listView=(ListView)findViewById(R.id.listView);
        final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();


        final MyAdapter adapter = new MyAdapter(this,
                R.layout.mainlistviewchild, R.id.maintitle,aList );
        listView.setAdapter(adapter);
        postRef=ref.child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    for (DataSnapshot single : child.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) single.getValue();
                        title = (String) map.get("Eventtitle");
                        name = (String) map.get("Eventername");
                        image = (String) map.get("Eventimage");
                        date = (String) map.get("Eventdate");
                        address=(String) map.get("Eventaddress");
                        state=(String) map.get("Eventstate");
                        country=(String) map.get("Eventcountry");
                        city=(String) map.get("Eventcity");
                        knownplace=(String) map.get("Evenyknownplace");
                        postalcode=(String) map.get("Eventpostalcode");
                        firebasechild=(String) map.get("Firebasechild");
                        desc=(String) map.get("Eventdesc");
                        details=(String) map.get("Eventdetails");


                        //Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                        if (title==null)
                        {
                            //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                        // if (name!=null && image!=null && email!=null && address!=null && country!=null && knownplace!=null && postalcode!=null) {
                        else
                        {
                            // Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                            String[] name1={name};
                            String[] address1={address};
                            String[] image1={image};
                            String[] state1={state};
                            String[] country1={country};
                            String[] city1={city};
                            String[] knownplace1={knownplace};
                            String[] postalcode1={postalcode};
                            String[] desc1={desc};
                            String[] title1={title};
                            String[] details1={details};
                            String[] date1={knownplace};

                            HashMap<String,String> data=new HashMap<String, String>();
                            for (int i=0; i<name1.length;i++) {
                                data.put("Name", name1[i]);
                                data.put("Date",date1[i]);
                                data.put("Address",address1[i]);
                                data.put("Image",image1[i]);
                                data.put("Country",country1[i]);
                                data.put("State",state1[i]);
                                data.put("City",city1[i]);
                                data.put("Knownolace",knownplace1[i]);
                                data.put("Postalcode",postalcode1[i]);
                                data.put("Desc",desc1[i]);
                                data.put("Title",title1[i]);
                                data.put("Details",details1[i]);
                            }
                            aList.add(data);
                            adapter.notifyDataSetChanged();
                            //progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),PostEventMaps.class);
                intent.putExtra("stuff",stuff);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            // builder.setCancelable(false);
            //builder.setTitle("Rate Us if u like this");
            builder.setTitle("Do you want to Exit?");
            builder.setPositiveButton("yes",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(getApplicationContext(), "Yes i wanna exit", Toast.LENGTH_LONG).show();

                    finish();
                }
            });
            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.admin) {
            Intent intent=new Intent(getApplicationContext(),Admin.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.terms) {
            Intent intent=new Intent(getApplicationContext(),TermsAndConditions.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myprofile) {
            Intent intent=new Intent(getApplicationContext(),MyProfile.class);
            startActivity(intent);
        } else if (id == R.id.mycontribution) {
            Intent intent=new Intent(getApplicationContext(),MyContribution.class);
            startActivity(intent);
        } else if (id == R.id.swachbharath) {
            Intent intent=new Intent(getApplicationContext(),SwachBharat.class);
            startActivity(intent);
        } else if (id == R.id.planttree) {
            Intent intent=new Intent(getApplicationContext(),PlantATree.class);
            startActivity(intent);
        } else if (id == R.id.supportanimal) {
            Intent intent=new Intent(getApplicationContext(),SupportAnimalCause.class);
            startActivity(intent);
        } else if (id == R.id.childedu) {
            Intent intent=new Intent(getApplicationContext(),SupportChildEducation.class);
            startActivity(intent);
        } else if (id == R.id.clothandfood) {
            Intent intent=new Intent(getApplicationContext(),DonateClothsAndFood.class);
            startActivity(intent);
        } else if (id == R.id.orphsupport) {
            Intent intent=new Intent(getApplicationContext(),OrphanageSupport.class);
            startActivity(intent);
        } else if (id == R.id.supportgirl) {
            Intent intent=new Intent(getApplicationContext(),SupportForTheGirlChild.class);
            startActivity(intent);
        } else if (id == R.id.supportschild) {
            Intent intent=new Intent(getApplicationContext(),SupprtStreetChildren.class);
            startActivity(intent);
        } else if (id == R.id.supportedulit) {
            Intent intent=new Intent(getApplicationContext(),SupportForEducation.class);
            startActivity(intent);
        } else if (id == R.id.supportplanet) {
            Intent intent=new Intent(getApplicationContext(),SupportForGreenerPlanet.class);
            startActivity(intent);
        } else if (id == R.id.yourskills) {
            Intent intent=new Intent(getApplicationContext(),ContributeYourSkills.class);
            startActivity(intent);
        } else if (id == R.id.supportngo) {
            Intent intent=new Intent(getApplicationContext(),SupportNgo.class);
            startActivity(intent);
        } else if (id == R.id.blooddonate) {
            Intent intent=new Intent(getApplicationContext(),BloodDonationHealth.class);
            intent.putExtra("stuff",getIntent().getExtras().getString("stuff"));
            startActivity(intent);
        } else if (id == R.id.socialimapact) {
            Intent intent=new Intent(getApplicationContext(),SocialCollaborations.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
