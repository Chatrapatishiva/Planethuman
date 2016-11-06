package bootcamplearning.humanplanet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class BloodDonationHealth extends AppCompatActivity {
    ArrayList<String> data=new ArrayList<>();
    ArrayList<String>dataname=new ArrayList<>();
    ArrayList<String>dataid=new ArrayList<>();
    ArrayList<String>datamyname=new ArrayList<>();
    ArrayList<String>datalocation=new ArrayList<>();
    DatabaseReference ref;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    ViewGroup parent;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donation_health);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
progressBar=(ProgressBar)findViewById(R.id.progressBar3);
progressBar.setVisibility(View.VISIBLE);

        recyclerView=(RecyclerView)findViewById(R.id.rv);
        RecyclerViewLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        RecyclerView.Adapter adapter=new RecyclerViewAdapter(data,dataname,dataid,datamyname,getIntent().getExtras().getString("stuff"),datalocation);

        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    for (DataSnapshot single : child.getChildren()) {
                        final Map<String, Object> map = (Map<String, Object>) single.getValue();


                       String title = (String) map.get("Titlebdah");
                       String desc = (String) map.get("Descbdah");
                        String image = (String) map.get("Imagebdah");
                        String Id=(String)map.get("Datebdah");
                        String location=(String)map.get("Citybdah");

                        if (title == null) {

                        } else {

                            data.add(title);
                            dataname.add(desc);
                            dataid.add(image);
                            datamyname.add(Id);
                            datalocation.add(location);
                            //recyclerView.notifyAll();
                            progressBar.setVisibility(View.INVISIBLE);


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
