package com.mohaa.mazaya.dashboard.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.ExpandableTextView;
import com.mohaa.mazaya.dashboard.Utils.FormatterUtil;
import com.mohaa.mazaya.dashboard.Utils.GetTimeAgo;
import com.mohaa.mazaya.dashboard.models.Comment;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Mohamed El Sayed
 */
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comment> commentsList;

    public Context context;



    public CommentsRecyclerAdapter(List<Comment> commentsList ){

        this.commentsList = commentsList;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();

        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String data = prefs.getString("blog_post_id", ""); //no id: default value
        final int user_id = commentsList.get(position).getId();
        String userName = commentsList.get(position).getUsr_name();

        long time = commentsList.get(position).getCreated_at();
        holder.setTime(time);

        //final String blogPostId = comment_.getBlog_post_id();
        final String blogPostId = data;
        String commentMessage = commentsList.get(position).getContent();
        holder.setComment_message(commentMessage);

        Comment c = commentsList.get(position);
        holder.fillComment(userName, c, holder.commentTextView, holder.comment_time_stamp);
        holder.ratingBar.setRating(commentsList.get(position).getRate());

    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;

        private TextView blogUserName;
        //private CircleImageView blogUserImage;
        private TextView comment_time_stamp;
        private final ExpandableTextView commentTextView;
        private RatingBar ratingBar;

        // reply
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            commentTextView = (ExpandableTextView) itemView.findViewById(R.id.commentText);

            //blogUserImage = mView.findViewById(R.id.Comments_image);
            blogUserName = mView.findViewById(R.id.Comments_username);
            comment_message = mView.findViewById(R.id.Comments_message);
            comment_time_stamp = mView.findViewById(R.id.comment_time_stamp);
            ratingBar = mView.findViewById(R.id.ratingBar);

            //

        }
        private void fillComment(String userName, Comment comment, ExpandableTextView commentTextView, TextView dateTextView) {
            Spannable contentString = new SpannableStringBuilder(userName + "   " + comment.getContent());
            contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                    0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            commentTextView.setText(contentString);

            CharSequence date = FormatterUtil.getRelativeTimeSpanString(context, comment.getCreated_at());
            dateTextView.setText(date);
        }

        public void setComment_message(String message){


            comment_message.setText(message);

        }


        public void setTime(long time) {

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            comment_time_stamp.setText(lastSeenTime);

        }
    }
}
