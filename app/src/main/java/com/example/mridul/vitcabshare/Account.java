package com.example.mridul.vitcabshare;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment {


    public Account() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private TextView textView;
    private DatabaseReference mdatabase;
    private FirebaseUser currentuser;
    private static final int GALLERY_PICK = 1;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        circleImageView = (CircleImageView) root.findViewById(R.id.profile_image1);
        textView = (TextView) root.findViewById(R.id.name_display);
        button = (Button) root.findViewById(R.id.button);
        storageReference = FirebaseStorage.getInstance().getReference();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = currentuser.getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("travel").child(current_uid);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
//                String email = dataSnapshot.child("email-id").getValue().toString();
//                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                textView.setText(name);
                if(!image.equals("default")) {
                    Picasso.with(getContext()).load(image).placeholder(R.drawable.cool).into(circleImageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("images/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Image"), GALLERY_PICK);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageuri = data.getData();
            CropImage.activity(imageuri).setMinCropWindowSize(1000,1000).setMaxCropResultSize(1500,1500).setAspectRatio(2,2).start(getContext(), Account.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                progressDialog=new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading Image...");
                progressDialog.setMessage("Please wait while we are uploading the images.....");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);


                Uri resultUri = result.getUri();
                File thumbnil_file=new File(resultUri.getPath());
                String current_user_id = currentuser.getUid();
                Bitmap thumbnil= null;
                try {
                    thumbnil = new Compressor(getContext()).setMaxHeight(200).setMaxWidth(200).setQuality(75).compressToBitmap(thumbnil_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbnil.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();
                StorageReference filepath = storageReference.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filefath=storageReference.child("profile_images").child("thumb").child(current_user_id + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Working", Toast.LENGTH_SHORT).show();
                            final String download_url=task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumb_filefath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_url = thumb_task.getResult().getDownloadUrl().toString();
                                    if (thumb_task.isSuccessful()) {

                                        Map upadethashmap = new HashMap<String, String>();
                                        upadethashmap.put("image", download_url);
                                        upadethashmap.put("thumb_image", thumb_url);
                                        mdatabase.updateChildren(upadethashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "Success Uploading", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Error uploading Thumbnil", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "Error.....", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}