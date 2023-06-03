package com.example.figma.controller.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.figma.R;
import com.example.figma.controller.Login;
import com.example.figma.model.sign_up_DB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.google.android.auth.AuthResult;

public class SignUp extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 200;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser; //안드로이드와 파이어베이스 사이의 인증을 확인하기 위한 인스턴스 선언
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText spinner_answer, editTextTextPersonName4, editTextTextPassword, editTextTextPersonName, editTextTextPersonName2, editTextNumberPassword, editTextTextPersonName3; //회원가입 입력필드
    private Button finishBT; //회원가입 버튼
    private TextView spinner_question;
    private FirebaseStorage storage;
    ImageView imageView;
    private String question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        storage = FirebaseStorage.getInstance();

        mAuth = FirebaseAuth.getInstance(); //선언한 인스턴스를 초기화
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SignUp");

        editTextTextPersonName4 = findViewById(R.id.editTextTextPersonName4);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        finishBT = findViewById(R.id.finishBT);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPersonName2 = findViewById(R.id.editTextTextPersonName2);
        editTextNumberPassword = findViewById(R.id.editTextNumberPassword);
        ImageButton backButton = findViewById(R.id.backButton);// 뒤로가기 버튼
        spinner_question = findViewById(R.id.spinner_question);
        spinner_answer = findViewById(R.id.spinner_answer);

        imageView = findViewById(R.id.imageView10);
        ImageButton imageButton = findViewById(R.id.imageButton);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spinner_question.setText((CharSequence) adapterView.getItemAtPosition(position));
                question = (String) adapterView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });


        finishBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strEmail = editTextTextPersonName4.getText().toString();
                String strPwd = editTextTextPassword.getText().toString();
                String strUserName = editTextTextPersonName.getText().toString();
                String strStudentNumber = editTextTextPersonName2.getText().toString();
                String pwdCheck = editTextNumberPassword.getText().toString();
                String answer = spinner_answer.getText().toString();

                if (strUserName.length() > 0 && strStudentNumber.length() > 0 && strEmail.length() > 0 && strPwd.length() > 0 && pwdCheck.length() > 0) {
                    if (strPwd.equals(pwdCheck)) {
                        //FirebaseAuth 진행
                        mAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    sign_up_DB sign_up_db = new sign_up_DB();
                                    sign_up_db.setEmailId(firebaseUser.getEmail());
                                    sign_up_db.setPassword(strPwd);
                                    sign_up_db.setIdToken(firebaseUser.getUid());
                                    sign_up_db.setUserName(strUserName);
                                    sign_up_db.setStudentNumber(strStudentNumber);
                                    sign_up_db.setAnswer(answer);
                                    sign_up_db.setQuestion(question);


                                    //setValue는 database에 insert 행휘
                                    mDatabaseRef.child(firebaseUser.getUid()).setValue(sign_up_db);

                                    Toast.makeText(SignUp.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), SignUpEmail.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(SignUp.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUp.this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                    String strEmail1 = editTextTextPersonName4.getText().toString();

                    StorageReference storageRef = storage.getReference();
                    StorageReference riversRef = storageRef.child("SignUp/" + strEmail1);
                    UploadTask uploadTask = riversRef.putFile(uri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SignUp.this, "성공", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }
}