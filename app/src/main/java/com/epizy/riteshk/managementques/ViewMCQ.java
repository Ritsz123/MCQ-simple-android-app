package com.epizy.riteshk.managementques;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.epizy.riteshk.managementques.Model.Question;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;
import java.util.Vector;


public class ViewMCQ extends AppCompatActivity implements View.OnClickListener{

    AdView mAdView;
    TextView score,que,totalattempted;
    RadioButton option1,option2,option3,option4,selectedbtn;
    RadioGroup group;
    Button submit;
    int correct=0,wrong = 0,total =0,questnNo;
    DatabaseReference mDatabase;
    Question question;
    LinearLayout start,questionspace;
    Vector v = new Vector ();
    Random r =new Random();
    InterstitialAd add;
    String bannerid = "ca-app-pub-9126488655395558/9434142638";
    String interstrialid = "ca-app-pub-9126488655395558/5491399324";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        if (!isNetworkConnected()){
            Toast.makeText(this, "Internet Connection is Required..!", Toast.LENGTH_LONG).show();
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },2000);
        }

        questionspace=findViewById(R.id.main_question_layout);
        start = findViewById(R.id.startlayout);
        score = findViewById(R.id.tv_score);
        que = findViewById(R.id.tv_question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submit = findViewById(R.id.btn_submit);
        group = findViewById(R.id.rg);
        totalattempted=findViewById(R.id.total_tv);
        mAdView = findViewById(R.id.ad1_bottom);
        submit.setOnClickListener(this);

        start.setVisibility(View.VISIBLE);
        questionspace.setVisibility(View.INVISIBLE);


        MobileAds.initialize(this,bannerid);


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        add = new InterstitialAd(this);
        add.setAdUnitId(interstrialid);

        updateQuestions();
    }
    private void updateQuestions() {

       questnNo = generateNo();


        group.clearCheck();
        if (total<1980){
            if (total%5 ==0){
                loadIntestrialad();
            }

            mDatabase = FirebaseDatabase.getInstance().getReference().child(String.valueOf(questnNo));
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    question = dataSnapshot.getValue(Question.class);

                    que.setText(total+1 + " ) " + question.getQuestion());
                    option1.setText(question.getOption1());
                    option2.setText(question.getOption2());
                    option3.setText(question.getOption3());
                    option4.setText(question.getOption4());

                    option1.setBackgroundColor(Color.WHITE);
                    option2.setBackgroundColor(Color.WHITE);
                    option3.setBackgroundColor(Color.WHITE);
                    option4.setBackgroundColor(Color.WHITE);

                    start.setVisibility(View.INVISIBLE);
                    questionspace.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewMCQ.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            if (add.isLoaded()){
                add.show();
            }
        }else
        {
            //show result
            showresult();

        }
    }

    private void showresult() {


    }

    @Override
    public void onClick(View v) {
        Handler handler = new Handler();
        if (v.getId()==R.id.btn_submit ){

            try {
                selectedbtn = findViewById(group.getCheckedRadioButtonId());
                String selectedAns = selectedbtn.getText().toString();

                if (!selectedAns.equalsIgnoreCase(question.getAnswer())) {
                    selectedbtn.setBackgroundColor(Color.RED);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            total++;
                            checkcorrect();
                            totalattempted.setText("Total Attempted:- " + total);
                            updateQuestions();
                        }
                    }, 1200);

                } else {
                    correct++;
                    selectedbtn.setBackgroundColor(Color.GREEN);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            total++;
                            totalattempted.setText("Total Attempted:- " + total);
                            updateQuestions();
                        }
                    }, 1200);

                }
                score.setText("Score: " + correct);
            }catch (NullPointerException e){
                Toast.makeText(this, "select answer and click submit", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void checkcorrect(){
        if (option1.getText().toString().equalsIgnoreCase(question.getAnswer())){
            option1.setBackgroundColor(Color.GREEN);
        }else if (option2.getText().toString().equalsIgnoreCase(question.getAnswer())){
            option2.setBackgroundColor(Color.GREEN);
        }else if (option3.getText().toString().equalsIgnoreCase(question.getAnswer())){
            option3.setBackgroundColor(Color.GREEN);
        }else if (option4.getText().toString().equalsIgnoreCase(question.getAnswer())){
            option4.setBackgroundColor(Color.GREEN);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public void exitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Exit?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),endActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        if (add.isLoaded()){
            add.show();
        }
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }
    public int generateNo(){
        int n = r.nextInt(1980);
        v.add(questnNo);
        if (v.contains(n)){
            n = generateNo();
        }
        return  n;
    }
    public void loadIntestrialad(){

        add.loadAd(new AdRequest.Builder().build());

    }
}
