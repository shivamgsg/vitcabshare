package com.techifuzz.team.vitsharecar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText,messageText1;
        public CircleImageView profileImage,profileImage1;
        public TextView displayName,messageTime,displayName1,messageTime1;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
//            messageTime=(TextView) view.findViewById(R.id.time_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
//            displayName = (TextView) view.findViewById(R.id.name_text_layout);

            messageText1 = (TextView) view.findViewById(R.id.message_text_layout_sender);
//            messageTime1=(TextView) view.findViewById(R.id.time_text_layout_sender);
            profileImage1 = (CircleImageView) view.findViewById(R.id.message_profile_layout_sender);
//            displayName1 = (TextView) view.findViewById(R.id.name_text_layout_sender);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, final int i) {

        final Messages c = mMessageList.get(i);

        String from_user = c.getFrom();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                if(mMessageList!=null) {
                    if (mMessageList.get(i).getFrom().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                        viewHolder.displayName.setVisibility(View.INVISIBLE);
//                        viewHolder.messageTime.setVisibility(View.INVISIBLE);
                        viewHolder.messageText.setVisibility(View.INVISIBLE);
                        viewHolder.profileImage.setVisibility(View.INVISIBLE);

//                        viewHolder.displayName1.setVisibility(View.VISIBLE);
                        viewHolder.messageText1.setVisibility(View.VISIBLE);
//                        viewHolder.messageTime1.setVisibility(View.VISIBLE);
                        viewHolder.profileImage1.setVisibility(View.VISIBLE);

//                        viewHolder.displayName1.setText(name);
                        Picasso.with(viewHolder.profileImage1.getContext()).load(image).placeholder(R.drawable.cool).into(viewHolder.profileImage1);
                        viewHolder.messageText1.setText(c.getMessage());
//                        viewHolder.messageTime1.setText(String.valueOf(c.getTime()));
                    } else {
//                        viewHolder.displayName.setVisibility(View.VISIBLE);
                        viewHolder.messageText.setVisibility(View.VISIBLE);
//                        viewHolder.messageTime.setVisibility(View.VISIBLE);
                        viewHolder.profileImage.setVisibility(View.VISIBLE);

//                        viewHolder.displayName1.setVisibility(View.INVISIBLE);
//                        viewHolder.messageTime1.setVisibility(View.INVISIBLE);
                        viewHolder.messageText1.setVisibility(View.INVISIBLE);
                        viewHolder.profileImage1.setVisibility(View.INVISIBLE);

//                        viewHolder.displayName.setText(name);
                        Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.cool).into(viewHolder.profileImage);
                        viewHolder.messageText.setText(c.getMessage());
//                        viewHolder.messageTime.setText(String.valueOf(c.getTime()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}