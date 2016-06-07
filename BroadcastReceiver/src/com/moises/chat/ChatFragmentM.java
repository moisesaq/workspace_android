package com.moises.chat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

//ESTA CLASE FUE PARA HACER ALGUNAS MODIFICACIONES DE CHAT OPERADOR DE TECHNORIDES
public class ChatFragmentM extends Fragment implements OnClickListener, OnTouchListener{

	public static final String FRAGMENT_TAG = "tagChatFrag";
    public static final String CHAT_DATA_BUNDLE = "chatData";

    private View view;
    private ListView chatContainer;
    private EditText writeMessage;
    private FrameLayout containerButtons;
    private ImageButton record, send;

    private ChatAdapter adapter;
    private ChatListItem chatListItem;

    int test = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);
        setup();

        return view;
    }

    private void setup() {
//        chatListItem = (ChatListItem)getArguments().getSerializable(CHAT_DATA_BUNDLE);
//        chatContainer = (ListView) view.findViewById(R.id.chatContainer);
//        writeMessage = (EditText) view.findViewById(R.id.writeMessage);
//        //writeMessage.addTextChangedListener(MessageWatcher);
//        //writeMessage.setText(String.valueOf(chatDriverItem.getDriver())+ "sender-"+chatDriverItem.getSenderId()+" user-"+Global.getUser().getId());
//        containerButtons = (FrameLayout) view.findViewById(R.id.containerButtons);
//        send = (ImageButton) view.findViewById(R.id.sendMessage);
//        send.setOnClickListener(this);
//        record = (ImageButton) view.findViewById(R.id.recordMessage);
//        record.setOnTouchListener(this);
//
//        adapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>());
//        chatContainer.setAdapter(adapter);
//        getListMessages(0);
    }

    @Override
    public void onClick(View v){
//        switch (v.getId()){
//            case R.id.sendMessage:
//                sendMessage(
//                        new ChatMessage()
//                                .setMessage(writeMessage.getText().toString())
//                                .setTime(getCurrentTime())
//                                .setPathImage("")
//                                .setAudio(0)
//                                .setIsMine(getRandomBoolean())
//                );
//                break;
//        }
    }

    public void sendMessage(ChatMessage chatMessage){
        adapter.add(chatMessage);
        adapter.notifyDataSetChanged();
        chatContainer.setSelection(chatContainer.getCount()-1);
        writeMessage.getText().clear();
    }

    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
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
            if (s.length() >= 1) {
                send.setVisibility(View.VISIBLE);
                record.setVisibility(View.GONE);
            } else {
                record.setVisibility(View.VISIBLE);
                send.setVisibility(View.GONE);
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
//                if (test == 1) {
//                    test = 0;
//                    sendMessage(
//                            new ChatMessage()
//                                    .setMessage("")
//                                    .setTime(getCurrentTime())
//                                    .setPathImage("")
//                                    .setAudio(R.raw.los_simpsons_a_cargo_de_la_seguridad)
//                                    .setIsMine(getRandomBoolean())
//
//                    );
//                } else {
//                    test = 1;
//                    sendMessage(
//                            new ChatMessage()
//                                    .setMessage("")
//                                    .setTime(getCurrentTime())
//                                    .setPathImage("")
//                                    .setAudio(R.raw.detective)
//                                    .setIsMine(getRandomBoolean())
//
//                    );
//                }
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

    public void startAnimationRecord() {
        Animation animRecord = AnimationUtils.loadAnimation(getActivity(), R.anim.begin_record);
        animRecord.setFillAfter(true);
        containerButtons.startAnimation(animRecord);
    }

    public void endAnimationRecord() {
        Animation animEnd = AnimationUtils.loadAnimation(getActivity(), R.anim.end_record);
        animEnd.setFillAfter(true);
        containerButtons.startAnimation(animEnd);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Global.getAudio().stopAudioPlaying();
    }

    /*public void getListMessages(int page){
        Global.getModernApiClient().getConversation(String.valueOf(chatListItem.getReceiver().getId()), page, new ApiClientResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject responseBody) {
                try {
                    JSONArray messages = responseBody.getJSONArray("data");
                    for (int i=0; i<messages.length(); i++){
                    	JSONObject message = messages.getJSONObject(i);
                        String bytes = message.getString("bytes");
                        String id = message.getString("id");
                        Integer type = message.getInt("type");
                        Integer typeMessage = message.getInt("type_message");
                        String fileName = message.getString("filename");
                        String createdAt = message.getString("created_at");
                        Integer[] recipientsAck = message.getInt("recipients_ack");//TODO ojo aca
                        Integer[] recipients = message.getInt("recipients");//TODO ojo aca

                        JSONObject sender = message.getJSONObject("sender");
                        Integer senderId       = sender.getInt("id");
                        String senderFirstName = sender.getString("first_name");
                        String senderLastName  = sender.getString("last_name");
                        String senderPhone     = sender.getString("phone");
                        String senderEmail     = sender.getString("email");
                        String senderTypeUser  = sender.getString("type_user");

                        ChatUser senderUser = new ChatUser()
                                                .setId(senderId)
                                                .setFirstName(senderFirstName)
                                                .setLastName(senderLastName)
                                                .setPhone(senderPhone)
                                                .setEmail(senderEmail)
                                                .setTypeUser(senderTypeUser);
                        
                        adapter.add(
                        		new ChatMessageM()
                            	.setBytes(bytes)
                            	.setId(id)
                            	.setType(type)
                            	.setTypeMessage(typeMessage)
                            	.setFileName(fileName)
                            	.setCreatedAt(createdAt)
                            	.setRecipientsAck(recipientsAck)
                            	.setRecipients(recipients)
                            	.setSender(senderUser));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, JSONObject responseBody) {

            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }*/
}
