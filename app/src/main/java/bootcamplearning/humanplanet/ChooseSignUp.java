package bootcamplearning.humanplanet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseSignUp extends AppCompatActivity {
Button google,facebook,humanplanet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_up);
        final String state=getIntent().getExtras().getString("state");
        final String country=getIntent().getExtras().getString("country");
        final String postalcode=getIntent().getExtras().getString("postalcode");
        final String knownplace=getIntent().getExtras().getString("knownplace");
        final String address=getIntent().getExtras().getString("address");
        final String city=getIntent().getExtras().getString("city");

        google=(Button)findViewById(R.id.google);
        facebook=(Button)findViewById(R.id.facebook);
        humanplanet=(Button)findViewById(R.id.customsignup);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),GoogleSignIn.class);
                intent.putExtra("state",state);
                intent.putExtra("country",country);
                intent.putExtra("postalcode",postalcode);
                intent.putExtra("knownplace",knownplace);
                intent.putExtra("address",address);
                intent.putExtra("city",city);

                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FacebookSignIn.class);
                intent.putExtra("state",state);
                intent.putExtra("country",country);
                intent.putExtra("postalcode",postalcode);
                intent.putExtra("knownplace",knownplace);
                intent.putExtra("address",address);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });

        humanplanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),HumanPlanetSignIn.class);
                intent.putExtra("state",state);
                intent.putExtra("country",country);
                intent.putExtra("postalcode",postalcode);
                intent.putExtra("knownplace",knownplace);
                intent.putExtra("address",address);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });

    }
}
