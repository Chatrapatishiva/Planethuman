package bootcamplearning.humanplanet;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shiv on 26-10-2016.
 */
public class MyAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    int resources;
    int layt;
    private List<HashMap<String, String>> aList=new ArrayList<HashMap<String,String>>();
    int[] to;
    public MyAdapter(Context context, int resources,int layt, List<HashMap<String, String>> aList) {
        super(context, resources, aList);

        this.context = context;
        this.resources=resources;
        this.layt=layt;
        this.aList = aList;
        inflater = LayoutInflater.from(context);
    }



    HashMap<String, String> m;
    String[] aa;
    String c;
    List<String> myString = new ArrayList<String>();
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {

            convertView = inflater.inflate(R.layout.mainlistviewchild, parent,false);
        }

        TextView title1=(TextView)convertView.findViewById(R.id.maintitle);
        TextView by=(TextView)convertView.findViewById(R.id.mainby);
        TextView date=(TextView)convertView.findViewById(R.id.maindate);
        ImageView image1=(ImageView)convertView.findViewById(R.id.mainimage);
        TextView locstate=(TextView)convertView.findViewById(R.id.mainlocation);

        title1.setText(aList.get(position).get("Title").substring(0,12));
        date.setText(aList.get(position).get("Date"));
        locstate.setText(aList.get(position).get("City"));
        by.setText(aList.get(position).get("Name"));
       Glide.with(context).load(aList.get(position).get("Image")).into(image1);





        return convertView;
    }
    @Override
    public int getCount() {
        return aList.size();
    }
}


