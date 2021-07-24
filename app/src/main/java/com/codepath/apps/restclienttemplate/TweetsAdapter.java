package com.codepath.apps.restclienttemplate;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweetList;

    public TweetsAdapter(Context context, List<Tweet> tweetList) {
        this.context = context;
        this.tweetList = tweetList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Tweet tweet = tweetList.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweetList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvTime;
        ImageView verified;
        ImageView ivImage;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            verified = itemView.findViewById(R.id.badge);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            new PatternEditableBuilder().
                    addPattern(Pattern.compile("\\@(\\w+)"), Color.parseColor("#ff1370a9"),
                            new PatternEditableBuilder.SpannableClickedListener() {
                                @Override
                                public void onSpanClicked(String text) {
                                    Log.i("TweetsAdapter", "onSpanClicked");
                                }
                            }).into(tvBody);

            tvScreenName.setText("@" + tweet.user.screenName);
            tvTime.setText(" •  " + tweet.getFormattedTimestamp());
            tvName.setText(tweet.user.name);

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.onhover_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    layoutParams.dimAmount = 0;
                    layoutParams.gravity = getAdapterPosition();
                    window.setAttributes(layoutParams);

                    ImageView ivProfileImage = dialog.findViewById(R.id.ivProfileImage);
                    TextView tvName = dialog.findViewById(R.id.tvName);
                    TextView tvScreenName = dialog.findViewById(R.id.tvScreenName);
                    TextView tvDescription = dialog.findViewById(R.id.tvDescription);
                    TextView tvFollowing = dialog.findViewById(R.id.tvFollowing);
                    TextView tvFollowers = dialog.findViewById(R.id.tvFollowers);


                    Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
                    tvName.setText(tweet.user.name);
                    tvScreenName.setText("@" + tweet.user.screenName);
                    tvDescription.setText(tweet.user.description);

                    if(tweet.user.followerCount >= 10000 ){
                        int foll = tweet.user.followerCount / 10000;
                        tvFollowers.setText(String.format(String.valueOf(foll) + "%s", "K"));
                    }else if (tweet.user.followerCount >= 100000 ){
                        int foll = tweet.user.followerCount / 100000;
                        tvFollowers.setText(String.format(String.valueOf(foll) + "%s", "K"));
                    }

                    tvFollowing.setText(String.valueOf(tweet.user.friends));
                    dialog.show();

                }
            });



            if(tweet.user.verified == true){
                verified.setVisibility(View.VISIBLE);
                verified.setImageResource(R.drawable.twitter_verified);
            }else if(tweet.user.verified == false){
                verified.setVisibility(View.GONE);
            }

//            long thumb = getLayoutPosition()*1000;
//            RequestOptions options = new RequestOptions().frame(thumb);
//            Glide.with(context).load(tweet.URL).apply(options).into(ivImage);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);

        }
    }



}
