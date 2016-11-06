package bootcamplearning.humanplanet;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vinay on 27-10-2016.
 */
public class MyFirebaseApp extends android.app.Application{

        @Override
        public void onCreate() {
            super.onCreate();
    /* Enable disk persistence  */
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
}
