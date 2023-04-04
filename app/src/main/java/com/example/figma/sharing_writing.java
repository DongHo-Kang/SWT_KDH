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


public class sharing_writing extends Activity {

    Button btn, backButton;
    EditText edit1, edit2;
    String title, content;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing_writing);

        btn = findViewById(R.id.button); //버튼 아이디 연결
        edit1 = findViewById(R.id.editTextTextPersonName); // 제목 적는 곳
        edit2 = findViewById(R.id.editTextTextPersonName1); // 내용 적는 곳

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getCurrentUser().getUid();

                Intent intent = new Intent(getApplicationContext(), sharing_board.class);
                startActivity(intent);

//                add_sharing(edit1.getText().toString(), edit2.getText().toString());

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


                            DatabaseReference boardRef = databaseReference.child("sharing Board").push();
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

        // 뒤로가기 버튼
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), sharing_board.class);
                startActivity(intent);
            }
        });
    }



    //    public void add_sharing(String title,String content){
//        sharing_DB sharing_db = new sharing_DB(title, content);
//
//        DatabaseReference sharingRef = databaseReference.child("sharing Board").push();
//        sharingRef.setValue(sharing_db);
//
//        Intent i = new Intent(sharing_writing.this, sharing_board.class);
//        startActivity(i);
//        finish();
//    }
}
    //uid 불러오기.
//    public String uid = null ;
//    public Timestamp timestamp;
//    //파이어베이스 데이터베이스 연동
//    private FirebaseDatabase database = FirebaseDatabase.getInstance();
//    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
//    //현재 연결은 데이터베이스에만 딱 연결해놓고
//    //키값(데이블 또는 속성)의 위치까지는 들어가지는 않는 모습이다.
//    private DatabaseReference databaseReference = database.getReference();
//
//    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    {
//        this.uid = user.getUid();
//    }
//
//    Button btn;
//    EditText edit1, edit2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sharing_writing);
//
//        btn = findViewById(R.id.button); //버튼 아이디 연결
//        edit1 = findViewById(R.id.editTextTextPersonName); // 제목 적는 곳
//        edit2 = findViewById(R.id.editTextTextPersonName1); // 내용 적는 곳
//
//        //버튼 누르면 값을 저장
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getApplicationContext(), sharing_board.class);
//                startActivity(intent);
//
//                //에딧 텍스트 값을 문자열로 바꾸어 함수에 넣어줍니다.
//                add_sharing(edit1.getText().toString(),edit2.getText().toString());
//
//            }
//        });
//
//
//    }
//    //값을 파이어베이스 Realtime database로 넘기는 함수
//    public void add_sharing(String title, String content) {
//        //여기에서 직접 변수를 만들어서 값을 직접 넣는것도 가능합니다.
//        // ex) 갓 태어난 동물만 입력해서 int age=1; 등을 넣는 경우
//        //sharing_DB.java에서 선언했던 함수.
//        Timestamp timestamp = Timestamp.now();
//        sharing_DB sharing_db = new sharing_DB(title, content, uid);
//
//        DatabaseReference sharingRef = databaseReference.child("sharing Board").push();
//        sharingRef.setValue(sharing_db);
//
////        Calendar expiration = Calendar.getInstance();
////        expiration.add(Calendar.HOUR_OF_DAY, 12);
////        long expirationTimestamp = expiration.getTimeInMillis();
////        //child는 해당 키 위치로 이동하는 함수입니다.
////        //키가 없는데 "sharing Board"와 title,content 같이 값을 지정한 경우 자동으로 생성합니다.
////        databaseReference.child("User").push().child(uid).child(sharingRef.getKey()).setValue(expirationTimestamp);
//
//        Intent i = new Intent(sharing_writing.this , sharing_board.class);
//        startActivity(i);
//        finish();
//    }
//}

//        sharingRef.addListenerForSingleValueEvent(new ValueEventListener() {

//        databaseReference.child("User").child(uid).orderByValue().equalTo(sharingRef.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String userName = dataSnapshot.child("username").getValue(String.class);
//                String studentNumber = dataSnapshot.child("studentNumber").getValue(String.class);
//
//                sharing_DB sharing_db = new sharing_DB(title, content, uid);
//                sharing_db.getUserName(userName);
//                sharing_db.getStudentNumber(studentNumber);
//
//                DatabaseReference sharingRef = databaseReference.child("sharing Board").push();
//                sharingRef.setValue(sharing_db);
//            }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


                        //        Calendar expiration = Calendar.getInstance();
                        //        expiration.add(Calendar.HOUR_OF_DAY, 12);
                        //        long expirationTimestamp = expiration.getTimeInMillis();
                        //        //child는 해당 키 위치로 이동하는 함수입니다.
                        //        //키가 없는데 "sharing Board"와 title,content 같이 값을 지정한 경우 자동으로 생성합니다.
                        //        databaseReference.child("User").push().child(uid).child(sharingRef.getKey()).setValue(expirationTimestamp);

//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    private Button uploadButton;
//
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sharing_writing);
//
//        // 뒤로가기 버튼
//        ImageButton backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), sharing_board.class);//뒤로 가기 클릭시 나눔 목록으로 이동
//                startActivity(intent);
//            }
//        });
//
//        //이미지 업로드 버튼
//        ImageButton picture_upload = findViewById(R.id.imageButton7);
//        picture_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //갤러리에서 이미지 선택 인텐트 실행
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
//            }
//        });
//    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
//            Uri imageUri = data.getData();
//
//            //파이어베이스 스토리지에 이미지 업로드
//            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/"+UUID.randomUUID().toString());
//            UploadTask uploadTask = storageRef.putFile(imageUri);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    //이미지 업로드 성공
//                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                    //TODO: 이미지 URL을 게시물 데이터에 추가하고, 파이어베이스 데이터베이스에 게시물 저장
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    Log.e("ImageUpload", "Image upload failed:"+exception.getMessage());
//                    Toast.makeText(sharing_writing.this, "Image upload failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//}

