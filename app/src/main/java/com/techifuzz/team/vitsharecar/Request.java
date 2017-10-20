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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Request extends Fragment {


    private RecyclerView recyclerView;
    private TextView textView;


    public Request() {
        // Required empty public constructor
    }

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_secondfrag, container, false);
        final FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();
        textView=(TextView) root.findViewById(R.id.norequest);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("show").child(uid);
        recyclerView=(RecyclerView) root.findViewById(R.id.userlist1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                    FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                            User.class,
                            R.layout.user_single_item_request,
                            UserViewHolder.class,
                            databaseReference

                    ) {
                        @Override
                        protected void populateViewHolder(UserViewHolder viewHolder, final User user, int position) {

                            final String list_user_id = getRef(position).getKey();
                            final String name=user.getName();
                            final String number=user.getNumber();

                            viewHolder.setName(user.getName());
                            viewHolder.setEmail(user.getEmail());
                            viewHolder.setNumber(user.getNumber());
                            viewHolder.setImage(user.getImage(),getContext());
                            viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CharSequence options[] = new CharSequence[]{"Call", "Send message","Delete"};

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    builder.setTitle("Select Options");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            if(i==0){

                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                callIntent.setData(Uri.parse("tel:"+number));
                                                startActivity(callIntent);
                                            }

                                                if(i == 1){

                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                chatIntent.putExtra("user_id", list_user_id);
                                                chatIntent.putExtra("user_name", name);
                                                startActivity(chatIntent);
                                                }
                                                if(i == 3){
                                                    databaseReference.child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getContext(),"Request Successfully Deleted",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                        }
                                    });

                                    builder.show();
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

            TextView username=(TextView) mview.findViewById(R.id.name2);
            username.setText(name);
        }
        public void setEmail(String email){
            TextView email_u=(TextView) mview.findViewById(R.id.textView2_email);
            email_u.setText(email);
        }
        public void setNumber(String number){
            TextView number_u=(TextView) mview.findViewById(R.id.number12);
            number_u.setText(" "+number);
            number_u.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dial, 0, 0, 0);
        }
        public void setImage(String image, Context context)
        {
            CircleImageView userimage=(CircleImageView) mview.findViewById(R.id.circleImageView);
            Picasso.with(context).load(image).placeholder(R.drawable.cool).into(userimage);
        }

    }
}
