package com.moises.chat;
/*"data": [
{
  "bytes": "",
  "id": "568c28edf72d5fdb4d814b27",
  "type": 1,
  "type_message": 0,
  "rtaxi_id": 16887,
  "sender_name": "Support Technorides",
  "sender_id": 46222,
  "filename": "operator1452026031600.mp3",
  "created_at": "2016-01-05T20:34:53.649Z",
  "recipients_ack": [],
  "recipients": [
    34474
  ],
  "sender": {
    "id": 46222,
    "first_name": "Support",
    "last_name": "Technorides",
    "phone": "213123123",
    "email": "support@suempresa.com",
    "type_user": "OPERADOR"
  }
}
]*/

//CLASE MODIFICADO PARA EL CHAT OPERADOR DE TECHNORIDES
public class ChatMessageM {
	private String bytes, id, fileName, createdAt;
	private int type, typeMessage, rtaxiId;
	private int[] recipientsAck;
	private int[] recipients;
	private ChatUser sender;
	
	public String getBytes() {
		return bytes;
	}
	public ChatMessageM setBytes(String bytes) {
		this.bytes = bytes;
		return this;
	}
	
	public String getId() {
		return id;
	}
	public ChatMessageM setId(String id) {
		this.id = id;
		return this;
	}
	
	public String getFileName() {
		return fileName;
	}
	public ChatMessageM setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	public ChatMessageM setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
		return this;
	}
	
	public int getType() {
		return type;
	}
	public ChatMessageM setType(int type) {
		this.type = type;
		return this;
	}
	
	public int getTypeMessage() {
		return typeMessage;
	}
	public ChatMessageM setTypeMessage(int typeMessage) {
		this.typeMessage = typeMessage;
		return this;
	}
	
	public int getRtaxiId() {
		return rtaxiId;
	}
	public ChatMessageM setRtaxiId(int rtaxiId) {
		this.rtaxiId = rtaxiId;
		return this;
	}
	
	public int[] getRecipientsAck() {
		return recipientsAck;
	}
	public ChatMessageM setRecipientsAck(int[] recipientsAck) {
		this.recipientsAck = recipientsAck;
		return this;
	}
	
	public int[] getRecipients() {
		return recipients;
	}
	public ChatMessageM setRecipients(int[] recipients) {
		this.recipients = recipients;
		return this;
	}
	
	public ChatUser getSender() {
		return sender;
	}
	public ChatMessageM setSender(ChatUser sender) {
		this.sender = sender;
		return this;
	}
	
}
