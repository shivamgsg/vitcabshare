package com.example.mridul.vitcabshare;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Showall extends Fragment {

    private DatabaseReference databaseReference;
    private DatabaseReference mdatabseref;
    private FirebaseUser currentuser;
    private Button button;

    public Showall() {
        // Required empty public constructor
    }

private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_showall, container, false);
        final FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("travel");
        recyclerView=(RecyclerView) root.findViewById(R.id.userlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<User,UserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
        R.layout.users_single_item,
                UserViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User user, int position) {

                viewHolder.setName(user.getName());
                    viewHolder.setTo(user.getTo());
                    viewHolder.setFrom(user.getFrom());
                    viewHolder.setDate(user.getDate());
                    viewHolder.setTime(user.getTime());
                    viewHolder.setThumb_image(user.getThumb_image(), getContext());
                    final String user_id = getRef(position).getKey();
                    viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profile = new Intent(getContext(), Profile.class);
                            profile.putExtra("user_id", user_id);
                            startActivity(profile);

                        }
                    });
                }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static  class UserViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public UserViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }

    public  void setName(String name){

        TextView username=(TextView) mview.findViewById(R.id.naame);
        username.setText(name);
    }
    public void setTo(String to)
    {
        TextView username1=(TextView) mview.findViewById(R.id.to_display);
        username1.setText(to);
    }
        public void setFrom(String from)
        {
            TextView username2=(TextView) mview.findViewById(R.id.from_display);
            username2.setText(from);
        }
        public void setDate(String date)
        {
            TextView username3=(TextView) mview.findViewById(R.id.date_display);
            username3.setText(date);
        }
        public void setTime(String time)
        {
            TextView username4=(TextView) mview.findViewById(R.id.time_display);
            username4.setText(time);
        }
        public void setThumb_image(String thumb_image, Context context)
        {
            CircleImageView userimage=(CircleImageView) mview.findViewById(R.id.circleImageView);
            Picasso.with(context).load(thumb_image).placeholder(R.drawable.cool).into(userimage);
        }

    }
}
