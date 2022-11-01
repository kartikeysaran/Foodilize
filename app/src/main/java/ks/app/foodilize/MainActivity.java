package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(v->{
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        findViewById(R.id.btn_main_donate).setOnClickListener(v->{
            Uri uri = Uri.parse("https://donatenow.wfp.org/wfp1027/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        findViewById(R.id.tV_main_team_food).setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, AboutUs.class));
        });

    }

    private void updateUI(GoogleSignInAccount account) {
        Log.e("GOOGLESIGNIN", "Update UI");
        if(account!=null) {
            Log.e("GOOGLESIGNIN", "Account Not null");
            Utils.db.collection("users")
                    .whereEqualTo("emailId", account.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.e("GOOGLESIGNIN", "Task Complete");
                            //splash.setAnimation(null);
                            if (task.isSuccessful()) {
                                Log.e("GOOGLESIGNIN", "Update UI");
                                if(!task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Utils.currentUser = document.toObject(ObjectNC.class);
                                        startActivity(new Intent(MainActivity.this, Ngo_Dashboard.class));
                                        finish();
                                    }
                                } else {
                                    Log.e("GOOGLESIGNIN", "Result Empty");
                                    Intent i = new Intent(MainActivity.this, sing_in_form.class);
                                    i.putExtra("account", account);
                                    startActivity(i);
                                    finish();
                                }

                            } else {
                                Log.e("GOOGLESIGNIN", "Task unsuccesful");
                                Intent i = new Intent(MainActivity.this, sing_in_form.class);
                                i.putExtra("account", account);
                                startActivity(i);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.e("SINGINFAILURE", e.getLocalizedMessage());
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            Log.e("GOOGLESIGNIN", "RESULT CODE: "+resultCode);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.e("GOOGLESIGNIN", e.getMessage());
            updateUI(null);
        }
    }
}