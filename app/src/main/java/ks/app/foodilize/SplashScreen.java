package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.e("SPLASH", "Inside UPDATE UI");
        final ImageView splash = (ImageView) findViewById(R.id.iV_splash_screen_logo);


        if(account!=null) {
            Log.e("SPLASH", "Account Not Null");
            Utils.db.collection("users")
                    .whereEqualTo("emailId", account.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            //splash.setAnimation(null);
                            Log.e("SPLASH", "Inside OnComplete");
                            if (task.isSuccessful()) {
                                Log.e("SPLASH", "Task Successfull");
                                if(!task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Utils.currentUser = document.toObject(ObjectNC.class);
                                        startActivity(new Intent(SplashScreen.this, Ngo_Dashboard.class));
                                        finish();
                                    }
                                } else {
                                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                    finish();
                                }

                            } else {
                                Log.e("SPLASH", "Task Not Successfull");
                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                finish();
                            }
                        }
                    });

        } else {
            //splash.setAnimation(null);
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }
}