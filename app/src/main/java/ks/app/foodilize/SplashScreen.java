package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        VideoView view = (VideoView)findViewById(R.id.videoView1);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        view.setVideoURI(Uri.parse(path));
        view.start();
        view.setOnCompletionListener(v->{
            view.start();
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        Utils.db = FirebaseFirestore.getInstance();
        if(account!=null) {
            Utils.db.collection("users")
                    .whereEqualTo("emailId", account.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Utils.currentUser = document.toObject(ObjectNC.class);
                                    startActivity(new Intent(SplashScreen.this, Ngo_Dashboard.class));
                                    finish();
                                }
                            } else {
                            }
                        }
                    });
            //TODO: Put the data in Utils

        } else {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }
}