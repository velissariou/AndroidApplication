package com.petros.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsAdapter extends ArrayAdapter<Post> {
    private List<Post> posts;
    private final LayoutInflater layoutInflater;
    private final int layoutResource;

    public PostsAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        posts = objects;
        layoutInflater = LayoutInflater.from(context);
        layoutResource = resource;
    }

    public Post getNewsEntry(int position){
        if(position < posts.size() ){
            return posts.get(position);
        }
        return new Post();
    }

    public void setPosts(@NonNull List<Post> newPosts) {
        posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostsViewHolder{
        public TextView statusTextView;
        public ImageView postMediaView;
        public ImageView socialMediaImageView;

        public PostsViewHolder(View itemView) {
            statusTextView = itemView.findViewById(R.id.statusTextView);
            postMediaView = itemView.findViewById(R.id.postImageView);
            socialMediaImageView = itemView.findViewById(R.id.socialMediaImageView);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PostsViewHolder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            holder = new PostsViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (PostsViewHolder) convertView.getTag();
        }

        Post post = posts.get(position);
        holder.statusTextView.setText(post.getCaption());
        Picasso.get().load(post.getMediaUrl()).into(holder.postMediaView);
        if(post instanceof TwitterPost){
            Picasso.get().load(R.drawable.twitter_logo_blue).into(holder.socialMediaImageView);
        } else {
            Picasso.get().load(R.drawable.instagram).into(holder.socialMediaImageView);
        }


        return convertView;
    }

    @Override
    public int getCount() {
        if(posts == null){
            return 0;
        }else{
            return posts.size();
        }
    }


}
