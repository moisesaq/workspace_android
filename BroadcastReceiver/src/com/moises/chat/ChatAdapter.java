package com.moises.chat;

import java.util.List;

import com.moises.broadcastreceiver.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<ChatMessage>{
	
	public Activity activity;
    public MediaPlayer mediaPlayer = null;
    public Handler myHandler = new Handler();
    public UpdateskSound updateskSound;

    public ChatAdapter(Activity activity, List<ChatMessage> list){
        super(activity, R.layout.chat_message_item, list);
        this.activity = activity;
    }

    public static class ViewHolder{
        LinearLayout lyViewMessage;
        LinearLayout lyViewAudioMessage;
        ImageButton ibtnPlay;
        ImageButton ibtnPause;
        SeekBar sbSound;
        TextView tvDurationSound;
        ImageView ivImage;
        TextView tvMessage;
        TextView tvTime;
    }

    @Override
    public ChatMessage getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if(view==null){
            view = activity.getLayoutInflater().inflate(R.layout.chat_message_item, null);
            holder = new ViewHolder();
            holder.lyViewAudioMessage = (LinearLayout)view.findViewById(R.id.lyViewAudioMessage);
            holder.ibtnPlay = (ImageButton)view.findViewById(R.id.ibtnPlay);
            holder.ibtnPause = (ImageButton)view.findViewById(R.id.ibtnPause);
            holder.sbSound = (SeekBar)view.findViewById(R.id.sbSound);
            holder.tvDurationSound = (TextView)view.findViewById(R.id.tvDurationSound);
            holder.ivImage = (ImageView)view.findViewById(R.id.ivImage);
            holder.lyViewMessage = (LinearLayout)view.findViewById(R.id.lyViewMessage);
            holder.tvMessage = (TextView)view.findViewById(R.id.tvMessage);
            holder.tvTime = (TextView)view.findViewById(R.id.tvTime);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        final ChatMessage chatMessage = this.getItem(position);

        if(chatMessage.getAudio()!=0){

            holder.lyViewAudioMessage.setVisibility(View.VISIBLE);
            holder.tvMessage.setVisibility(View.GONE);

            MediaPlayer mp = MediaPlayer.create(activity, chatMessage.getAudio());
            holder.tvDurationSound.setText(getHRM(mp.getDuration()));
            holder.sbSound.setMax(mp.getDuration());
            holder.sbSound.setProgress(mp.getCurrentPosition());

            holder.ibtnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	if(mediaPlayer==null){
                		mediaPlayer = MediaPlayer.create(activity, chatMessage.getAudio());
                	}else{
                		if(chatMessage.getAudio()==mediaPlayer.getAudioSessionId()){
                            if(!mediaPlayer.isPlaying()){
                                //updateskSound = new UpdateskSound(holder);
                                myHandler.postDelayed(new Runnable() {
        							
        							@Override
        							public void run() {
        								holder.sbSound.setProgress(mediaPlayer.getCurrentPosition());
        					            holder.tvDurationSound.setText(getHRM(mediaPlayer.getDuration()) + " - " + getHRM(mediaPlayer.getCurrentPosition()));
        					            myHandler.postDelayed(this, 1000);
        							}
        						}, 1000);
                                mediaPlayer.start();
                                holder.ibtnPlay.setVisibility(View.GONE);
                                holder.ibtnPause.setVisibility(View.VISIBLE);
                            }
                    	}else{
                    		mediaPlayer.stop();
                    		mediaPlayer.release();
                    		mediaPlayer = MediaPlayer.create(activity, chatMessage.getAudio());
                    		myHandler.postDelayed(new Runnable() {
    							
    							@Override
    							public void run() {
    								holder.sbSound.setProgress(mediaPlayer.getCurrentPosition());
    					            holder.tvDurationSound.setText(getHRM(mediaPlayer.getDuration()) + " - " + getHRM(mediaPlayer.getCurrentPosition()));
    					            myHandler.postDelayed(this, 1000);
    							}
    						}, 1000);
                            mediaPlayer.start();
                            holder.ibtnPlay.setVisibility(View.GONE);
                            holder.ibtnPause.setVisibility(View.VISIBLE);
                    	}
                	}
                	
                	
                }
            });

            holder.ibtnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        holder.ibtnPlay.setVisibility(View.VISIBLE);
                        holder.ibtnPause.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            holder.lyViewAudioMessage.setVisibility(View.GONE);
            holder.tvMessage.setVisibility(View.VISIBLE);
        }



        LayoutParams lp = (LayoutParams)holder.lyViewMessage.getLayoutParams();

        if(chatMessage.isMine){
            holder.lyViewMessage.setBackgroundResource(R.drawable.bubble_issuing);
            lp.gravity = Gravity.RIGHT;
        }else{
            holder.lyViewMessage.setBackgroundResource(R.drawable.bubble_receiver);
            lp.gravity = Gravity.LEFT;
        }
        holder.lyViewMessage.setLayoutParams(lp);

        if(!chatMessage.getPahtImage().equals("")){
            holder.ivImage.setVisibility(View.VISIBLE);
        }
        holder.tvMessage.setText(chatMessage.getMessage());
        holder.tvTime.setText(chatMessage.getTime());
        return view;
    }


    public class UpdateskSound implements Runnable{

        public ViewHolder holder;

        public UpdateskSound(ViewHolder holder){
            this.holder = holder;
        }

        @Override
        public void run() {
            holder.sbSound.setProgress(mediaPlayer.getCurrentPosition());
            holder.tvDurationSound.setText(getHRM(mediaPlayer.getDuration()) + " - " + getHRM(mediaPlayer.getCurrentPosition()));
            myHandler.postDelayed(this, 1000);
        }
    }

    private String getHRM(int milliseconds ) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        return ((hours<10)?"0"+hours:hours) + ":" +
                ((minutes<10)?"0"+minutes:minutes) + ":" +
                ((seconds<10)?"0"+seconds:seconds);
    }
	
	

}
