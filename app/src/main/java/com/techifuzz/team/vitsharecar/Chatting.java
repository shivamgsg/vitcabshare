package com.techifuzz.team.vitsharecar;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chatting extends Fragment {
    private TextView textView;
    private DatabaseReference databaseReference,getDatabaseReference,reference;
    private RecyclerView recyclerView;


    public Chatting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_chatting, container, false);
        final FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        getDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Chat").child(uid);
//        getDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    if (dataSnapshot.exists()){
//                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String naame = dataSnapshot.child("name").getValue().toString();
//                                String image = dataSnapshot.child("image").getValue().toString();
//                                Map hashMap = new HashMap();
//                                hashMap.put("name", naame);
//                                hashMap.put("image", image);
//                                getDatabaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
//                                    @Override
//                                    public void onSuccess(Object o) {
//                                        Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
       textView=(TextView) root.findViewById(R.id.nochat);
        recyclerView=(RecyclerView) root.findViewById(R.id.recyclerview_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        getDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                    FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                            User.class,
                            R.layout.user_single_item_chat,
                            UserViewHolder.class,
                            getDatabaseReference

                    ) {
                        @Override
                        protected void populateViewHolder(final UserViewHolder viewHolder, final User user, final int position) {

                            final String list_user_id = getRef(position).getKey();
                            final String name = user.getName();
                            final String image=user.getImage();


                            viewHolder.setName(user.getName());
                            viewHolder.setImage(user.getImage(), getContext());
                            viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    CharSequence options[] = new CharSequence[]{"Call", "Send message"};
//
//                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//                                    builder.setTitle("Select Options");
//                                    builder.setItems(options, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                                            if (i == 1) {
//
//                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
//                                                chatIntent.putExtra("user_id", list_user_id);
//                                                chatIntent.putExtra("user_name", name);
//                                                startActivity(chatIntent);
//                                            }
//                                        }
//                                    });
//
//                                    builder.show();
                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                chatIntent.putExtra("user_id", list_user_id);
                                                chatIntent.putExtra("user_name", name);
                                                chatIntent.putExtra("user_image",image);
                                                startActivity(chatIntent);
                                }
                            });
                            viewHolder.mview.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    viewHolder.mview.setSelected(true);
                                    CharSequence options[] = new CharSequence[]{"Delete"};
//
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    builder.setTitle("Select Options");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                            if (i == 0) {
                                                getDatabaseReference.child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            viewHolder.mview.setSelected(false);
                                        }
                                    });

                                    builder.show();
                                    return false;
                                }
                            });

//                viewHolder.setThumb_image(user.image(), getContext());
                        }
                    };
                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static  class UserViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public UserViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public  void setName(String name){

            TextView username=(TextView) mview.findViewById(R.id.name_chat);
            username.setText(name);
        }
        public void setImage(String image, Context context)
        {

            CircleImageView userimage=(CircleImageView) mview.findViewById(R.id.circleImageView_chat);
            Picasso.with(context).load(image).placeholder(R.drawable.cool).into(userimage);
        }

    }
}
