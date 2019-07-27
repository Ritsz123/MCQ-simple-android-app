package com.epizy.riteshk.managementques;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epizy.riteshk.managementques.Model.Rawdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancyaboutpagelib.FancyAboutPage;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class endActivity extends AppCompatActivity {

    FancyAboutPage aboutPage;
    DatabaseReference mDatabase;
    Rawdata rawdata;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        aboutPage = findViewById(R.id.fancyaboutpage);
        imageView = findViewById(R.id.circularImageView);

        getfromdb();
        updateDesc();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                System.exit(0);
            }
        },20000);

         }



    public void getfromdb(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child(String.valueOf("primarydata"));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rawdata = dataSnapshot.getValue(Rawdata.class);


                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/managementapp-51d50.appspot.com/o/mypic.jpg?alt=media&token=33e76661-9fc1-4375-be96-f818590e1a06").into(imageView);
                aboutPage.setDescription(rawdata.getMyDescription());
                aboutPage.setAppDescription(rawdata.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(endActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateDesc() {
        aboutPage.setCover(R.drawable.fdg);
        aboutPage.setName("Ritesh Khadse");

        aboutPage.setAppIcon(R.drawable.icn); //Pass your app icon image
        aboutPage.setAppName("Management App");

        aboutPage.addEmailLink("riteshkhadse12@gmail.com");     //Add your email id
        aboutPage.addFacebookLink("https://www.facebook.com/Ritsm");
    }

    @Override
    public void onBackPressed() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }
}
