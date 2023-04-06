package com.example.figma;

import android.app.Activity;

import android.os.Bundle;

//public class sharing_writing extends Activity {
//
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sharing_writing);
//    }

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class notice_writing extends Activity {

    Button btn, backButton;
    EditText edit1, edit2;
    String title, content;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_writing);

        btn = findViewById(R.id.button17); //버튼 아이디 연결
        edit1 = findViewById(R.id.editTextTextPersonName2); // 제목 적는 곳
        edit2 = findViewById(R.id.editTextTextPersonName3); // 내용 적는 곳

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getCurrentUser().getUid();

                Intent intent = new Intent(getApplicationContext(), notice_list.class);
                startActivity(intent);

                Query query = databaseReference.child("sign_up").orderByChild("idToken").equalTo(uid);

                query.addListenerForSingleValueEvent(new ValueEventListener() { //sign_up 노드 불러오기
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String emailId = dataSnapshot.child("emailId").getValue(String.class);
                            String idToken = dataSnapshot.child("idToken").getValue(String.class);
                            String studentNumber = dataSnapshot.child("studentNumber").getValue(String.class);
                            String username = dataSnapshot.child("userName").getValue(String.class);


                            String title = edit1.getText().toString();
                            String content = edit2.getText().toString();


                            DatabaseReference boardRef = databaseReference.child("notice Board").push();
                            boardRef.child("emailId").setValue(emailId);
                            boardRef.child("idToken").setValue(idToken);
                            boardRef.child("studentNumber").setValue(studentNumber);
                            boardRef.child("userName").setValue(username);
                            boardRef.child("title").setValue(title);
                            boardRef.child("content").setValue(content);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), notice_list.class);
                startActivity(intent);
            }
        });
    }
}
