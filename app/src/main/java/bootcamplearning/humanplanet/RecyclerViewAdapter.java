package bootcamplearning.humanplanet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.graphics.Target;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shiva on 31-10-2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    ArrayList<String> data;
    ArrayList<String>dataname;
    ArrayList<String>dataid;
    ArrayList<String>datamyname;
    ArrayList<String>datalocation;
    View view1;
    String tempname;
    String description;
Context context;
    DatabaseReference ref;
    DatabaseReference postRef;
    String stuff;
    String lati,longi;

    public RecyclerViewAdapter(ArrayList<String> data1,ArrayList<String> data2,ArrayList<String>data3,ArrayList<String>data4,String stuff,ArrayList<String>datalocation) {
        this.data = data1;
        this.dataname = data2;
        this.dataid = data3;
        this.datamyname = data4;
        this.stuff=stuff;
        this.datalocation=datalocation;

    }

        @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
            context=parent.getContext();
            ref = FirebaseDatabase.getInstance().getReference();
            postRef=ref.child("Contributions");

            return new ViewHolder(view1);


    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {
       holder.tv1.setText(data.get(position));
        holder.tv2.setText(dataname.get(position));
        tempname=dataid.get(position);

       Glide.with(context).load(tempname).into(holder.imag);

                if(Geocoder.isPresent()) {
                    try {

                        Geocoder gc = new Geocoder(context);
                        List<Address> addresses = gc.getFromLocationName(datalocation.get(position), 5); // get the found Address Objects

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

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog=new ProgressDialog(context);
                progressDialog.setMessage("Please Wait....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            try {
                                URL url = new URL(tempname);
                                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT,dataname.get(position));
                                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "", null);
                                Uri screenshotUri = Uri.parse(path);

                                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                intent.setType("image/*");
                                context.startActivity(Intent.createChooser(intent, "Share image via..."));
                                progressDialog.dismiss();
                            }catch (Exception e)
                            {
                                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }
        });

               holder.location.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent i=new Intent(context,ViewInMap.class);
                       i.putExtra("Lati",lati);
                       i.putExtra("Longi",longi);
                       context.startActivity(i);
                   }
               });
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,UserViewProfile.class);
                i.putExtra("stuff",stuff);
                context.startActivity(i);
            }
        });

      /*  holder.unsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            for (DataSnapshot single : child.getChildren()) {
                                final Map<String, Object> map = (Map<String, Object>) single.getValue();


                                String email1 = (String) map.get("Contriemail");
                                String contribution1 = (String) map.get("Contribution");

                                  if (contribution1==null) {


                                      final Map<String, String> text = new HashMap<String, String>();
                                      text.put("Contriemail", email1);
                                      text.put("Contribution", "true");
                                      postRef.push().setValue(text);
                                      holder.unsupport.setImageResource(R.drawable.support);
                                  }
                                    else if (contribution1=="true")
                                    {
                                        holder.unsupport.setImageResource(R.drawable.support);
                                    }



                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });





            }
        }); */

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1,tv2;
        ImageView imag,location,user,chat,unsupport,share;
        public ViewHolder(View itemView) {
            super(itemView);
             tv1=(TextView)itemView.findViewById(R.id.eventtitle);
            tv2=(TextView)itemView.findViewById(R.id.eventdesc);
            imag=(ImageView)itemView.findViewById(R.id.eventimage);
            location=(ImageView)itemView.findViewById(R.id.location);
            user=(ImageView)itemView.findViewById(R.id.user);
            chat=(ImageView)itemView.findViewById(R.id.chat);
            unsupport=(ImageView)itemView.findViewById(R.id.unsupport);
            share=(ImageView)itemView.findViewById(R.id.share);

        }
    }
}
