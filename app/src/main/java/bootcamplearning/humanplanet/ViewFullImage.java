package bootcamplearning.humanplanet;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewFullImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_image);
        ImageView imageView=(ImageView)findViewById(R.id.fullviewimage);
        String stuff=getIntent().getExtras().getString("stuff");
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Please wait");
        Glide.with(this).load(stuff).into(imageView);
        progressDialog.dismiss();
    }
}
