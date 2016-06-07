package com.moises.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.moises.broadcastreceiver.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

public class ChatFragment extends Fragment implements OnClickListener, OnTouchListener{

	private View view;
    private ListView lvChatContainer;
    private EditText etWriteMessage;
    private FrameLayout flyContainerButtons;
    private ImageButton ibtnRecord, ibtnSend;

    private ChatAdapter adapter;

    int test = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);
        initComponents(view);
        initChat();
        return view;
    }

    private void initComponents(View v) {
        lvChatContainer = (ListView)v.findViewById(R.id.lvChatContainer);
        etWriteMessage = (EditText)v.findViewById(R.id.etWriteMessage);
        etWriteMessage.addTextChangedListener(MessageWatcher);
        flyContainerButtons = (FrameLayout)v.findViewById(R.id.flyContainerButtons);
        ibtnSend = (ImageButton)v.findViewById(R.id.ibtnSendMessage);
        ibtnSend.setOnClickListener(this);
        ibtnRecord = (ImageButton)v.findViewById(R.id.ibtnRecordMessage);
        ibtnRecord.setOnTouchListener(this);
    }

    public void initChat(){
        adapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>());
        lvChatContainer.setAdapter(adapter);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ibtnSendMessage:
                //addChild(true, etWriteMessage.getText().toString());
                sendMessage(new ChatMessage(etWriteMessage.getText().toString(), getCurrentTime(), "", 0,getRandomBoolean()));
                break;
        }
    }

    public void sendMessage(ChatMessage chatMessage){
        adapter.add(chatMessage);
        adapter.notifyDataSetChanged();
        lvChatContainer.setSelection(lvChatContainer.getCount()-1);
        etWriteMessage.getText().clear();
    }

    public String getCurrentTime(){
        Date date = new Date();
        String time = date.getHours()+":"+date.getMinutes();
        return time;
    }

    private final TextWatcher MessageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()>=1){
                ibtnSend.setVisibility(View.VISIBLE);
                ibtnRecord.setVisibility(View.GONE);
            }else{
                ibtnRecord.setVisibility(View.VISIBLE);
                ibtnSend.setVisibility(View.GONE);
            }
		}
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startAnimationRecord();
                break;
            case MotionEvent.ACTION_UP:
                endAnimationRecord();
                if(test==1){
                    test=0;
                    sendMessage(new ChatMessage("", getCurrentTime(), "", R.raw.los_simpsons_a_cargo_de_la_seguridad, getRandomBoolean()));
                }else{
                    test=1;
                    sendMessage(new ChatMessage("", getCurrentTime(), "", R.raw.detective, getRandomBoolean()));
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                endAnimationRecord();
                break;
        }
        return false;
    }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public void startAnimationRecord(){
        Animation animRecord = AnimationUtils.loadAnimation(getActivity(), R.anim.begin_record);
        animRecord.setFillAfter(true);
        //ibtnRecord.startAnimation(animRecord);
        flyContainerButtons.startAnimation(animRecord);
    }

    public void endAnimationRecord(){
        Animation animEnd = AnimationUtils.loadAnimation(getActivity(), R.anim.end_record);
        animEnd.setFillAfter(true);
        //ibtnRecord.startAnimation(animEnd);
        flyContainerButtons.startAnimation(animEnd);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null){
            if(adapter.mediaPlayer!=null){
                adapter.mediaPlayer.stop();
                adapter.mediaPlayer.release();
            }
        }
    }
	
	
}
