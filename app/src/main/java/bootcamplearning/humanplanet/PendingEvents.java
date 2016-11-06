package bootcamplearning.humanplanet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class PendingEvents extends AppCompatActivity {

    DatabaseReference ref;
    DatabaseReference postRef;
    String name,date,image,address,state,country,postalcode,knownplace,city,firebasechild,desc,details,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_events);
        FirebaseDatabase.getInstance();




        ref = FirebaseDatabase.getInstance().getReference();

        ListView listView=(ListView)findViewById(R.id.listView);
        final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();


        final MyAdapter adapter = new MyAdapter(this,
                R.layout.mainlistviewchild, R.id.maintitle,aList );
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(getApplicationContext(),ApproveEvents.class);
                HashMap<String, String> hdata = (HashMap<String, String>)adapterView.getItemAtPosition(i);
                intent.putExtra("stuffname",""+hdata.get("Name"));
                intent.putExtra("stufftitle",""+hdata.get("Title"));
                intent.putExtra("stuffdesc",""+hdata.get("Desc"));
                intent.putExtra("stuffimage",""+hdata.get("Image"));
                intent.putExtra("stuffdate",""+hdata.get("Date"));
                intent.putExtra("stuffstate",""+hdata.get("State"));
                intent.putExtra("stuffpostalcode",""+hdata.get("Postalcode"));
                intent.putExtra("stuffknownplace",""+hdata.get("Knownplace"));
                intent.putExtra("stuffcity",""+hdata.get("City"));
                intent.putExtra("stuffdetails",""+hdata.get("Details"));
                intent.putExtra("stuffaddress",""+hdata.get("Address"));
                intent.putExtra("stuffcountry",""+hdata.get("Country"));
                intent.putExtra("stufffirebasechild",""+hdata.get("Firebasechild"));


                startActivity(intent);
            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    for (DataSnapshot single : child.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) single.getValue();
                        title = (String) map.get("Eventtitle");
                        name = (String) map.get("EventerName");
                        image = (String) map.get("Eventimage");
                        date = (String) map.get("EventDate");
                        address=(String) map.get("Eventaddress");
                        state=(String) map.get("Eventstate");
                        country=(String) map.get("Eventcountry");
                        city=(String) map.get("Eventcity");
                        knownplace=(String) map.get("Eventknownplace");
                        postalcode=(String) map.get("Eventpostalcode");
                        firebasechild=(String) map.get("Firebasechild");
                        desc=(String) map.get("Eventdesc");
                        details=(String) map.get("Eventdetails");



                        //Toast.makeText(PendingEvents.this, name11+image11, Toast.LENGTH_SHORT).show();
                        if (title==null)
                        {
                            //Toast.makeText(PendingEvents.this, "Error", Toast.LENGTH_SHORT).show();
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
                            String[] date1={date};
                            String[] firebasechild1={firebasechild};


                            HashMap<String,String> data=new HashMap<String, String>();
                            for (int i=0; i<name1.length;i++) {
                                data.put("Name", name1[i]);
                                data.put("Date",date1[i]);
                                data.put("Address",address1[i]);
                                data.put("Image",image1[i]);
                                data.put("Country",country1[i]);
                                data.put("State",state1[i]);
                                data.put("City",city1[i]);
                                data.put("Knownplace",knownplace1[i]);
                                data.put("Postalcode",postalcode1[i]);
                                data.put("Desc",desc1[i]);
                                data.put("Title",title1[i]);
                                data.put("Details",details1[i]);
                                data.put("Firebasechild",firebasechild1[i]);

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

    }
}
