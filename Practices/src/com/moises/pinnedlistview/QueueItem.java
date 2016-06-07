package com.moises.pinnedlistview;

public class QueueItem {

	
	public static final int SECTION = 0;
	public static final int ITEM_QUEUE = 1;
	public static final int ITEM_PENDING = 2;

	public int typeItem;
	public String text;

	public int sectionPosition;
	public int listPosition;
	
	public String addressFrom;
	public long timerCount;
	
	public QueueItem(){}

	public QueueItem(int typeItem, String text) {
	    this.typeItem = typeItem;
	    this.text = text;
	}
	
	public String getAddressFrom() {
		return addressFrom;
	}

	public QueueItem setAddressFrom(String addressFrom) {
		this.addressFrom = addressFrom;
		return this;
	}

	public long getTimerCount() {
		return timerCount;
	}

	public QueueItem setTimerCount(long timerCount) {
		this.timerCount = timerCount;
		return this;
	}

	public int getTypeItem() {
		return typeItem;
	}

	@Override 
	public String toString() {
		return text;
	}
}
