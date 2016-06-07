package com.moises.pinnedlistview;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.moises.pinnedlistview.PinnedSectionListView.PinnedSectionListAdapter;
import com.moises.practices.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class ListHeaderCustom extends Fragment implements OnItemClickListener{
	
	public static final String TAG = "tagListViewHeader";
	private boolean isFastScroll;
	
	public PinnedSectionListView listTrips;
	public View view;
	
	private boolean addPadding;
	
	public QueueAdapter adapter;
	final int sectionsNumber = 2;
	public int sectionPosition;
	public int listPosition;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.custom_list_header, container, false);
		listTrips = (PinnedSectionListView)view.findViewById(R.id.listTrips);
		listTrips.setOnItemClickListener(this);
		//initializeHeaderAndFooter();
		setupAdapter();
		
		initializeAdapter();
		getListQueue(8, "QUEUE");
		getListQueue(15, "PENDING");
		getListQueue(10, "OTHERS");
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		QueueItem item = (QueueItem) parent.getAdapter().getItem(position);
	    if (item != null) {
	        Toast.makeText(getActivity(), "Item " + position + ": " + item.text + " Section " + item.sectionPosition, Toast.LENGTH_SHORT).show();
	    } else {
	        Toast.makeText(getActivity(), "Item " + position, Toast.LENGTH_SHORT).show();
	    }
	}
	
    private void initializeAdapter() {
        listTrips.setFastScrollEnabled(isFastScroll);
        if (isFastScroll) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                listTrips.setFastScrollAlwaysVisible(true);
            }
            adapter = new FastScrollAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
            listTrips.setAdapter(adapter);
        } else {
        	adapter = new QueueAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
            listTrips.setAdapter(adapter);
        }
    }
	
    public void setupAdapter(){
    	if(adapter != null) adapter.clear();
    	sectionPosition = 0;
    	listPosition = 0;
    }
    public void getListQueue(int countItem, String titleHeader){   
    	QueueItem section = new QueueItem(QueueItem.SECTION, titleHeader);
        section.sectionPosition = sectionPosition;
        section.listPosition = listPosition++;
        adapter.onSectionAdded(section, sectionPosition);
        adapter.add(section);
    	for(int i = 0; i < countItem; i++){
    		QueueItem item = new QueueItem(QueueItem.ITEM_QUEUE, "Address " + i + " Section "+sectionPosition);
            item.sectionPosition = sectionPosition;
            item.listPosition = listPosition++;
            adapter.add(item);
    	}
    	sectionPosition++;
    }
	static class QueueAdapter extends ArrayAdapter<QueueItem> implements PinnedSectionListAdapter {

        private static final int[] COLORS = new int[] {R.color.green_light, R.color.orange_light,
            											R.color.blue_light, R.color.red_light };

        public QueueAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
            //generateDataset('A', 'Z', false);
        }

        public void generateDataset(char from, char to, boolean clear) {
        	
        	if (clear) clear();
        	
            final int sectionsNumber = to - from + 1;
            //prepareSections(sectionsNumber);

            int sectionPosition = 0, listPosition = 0;
            for (char i=0; i<sectionsNumber; i++) {
            	QueueItem section = new QueueItem(QueueItem.SECTION, String.valueOf((char)('A' + i)));
                section.sectionPosition = sectionPosition;
                section.listPosition = listPosition++;
                //onSectionAdded(section, sectionPosition);
                add(section);

                final int itemsNumber = (int) Math.abs((Math.cos(2f*Math.PI/3f * sectionsNumber / (i+1f)) * 25f));
                for (int j=0;j<itemsNumber;j++) {
                	QueueItem item = new QueueItem(QueueItem.ITEM_QUEUE, section.text.toUpperCase(Locale.ENGLISH) + " - " + j);
                    item.sectionPosition = sectionPosition;
                    item.listPosition = listPosition++;
                    add(item);
                }

                sectionPosition++;
            }
        }
        
        protected void prepareSections(int sectionsNumber) { }
        protected void onSectionAdded(QueueItem section, int sectionPosition) { }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(Color.DKGRAY);
            view.setTag("" + position);
            QueueItem item = getItem(position);
            if (item.typeItem == QueueItem.SECTION) {
                //view.setOnClickListener(PinnedSectionListActivity.this);
                view.setBackgroundColor(parent.getResources().getColor(COLORS[item.sectionPosition % COLORS.length]));
            }
            return view;
        }

        @Override public int getViewTypeCount() {
            return 2;
        }

        @Override public int getItemViewType(int position) {
            return getItem(position).typeItem;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == QueueItem.SECTION;
        }

    }

    static class FastScrollAdapter extends QueueAdapter implements SectionIndexer {

        private QueueItem[] sections;

        public FastScrollAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @Override protected void prepareSections(int sectionsNumber) {
            sections = new QueueItem[sectionsNumber];
        }

        @Override protected void onSectionAdded(QueueItem section, int sectionPosition) {
            sections[sectionPosition] = section;
        }

        @Override public QueueItem[] getSections() {
            return sections;
        }

        @Override public int getPositionForSection(int section) {
            if (section >= sections.length) {
                section = sections.length - 1;
            }
            return sections[section].listPosition;
        }

        @Override public int getSectionForPosition(int position) {
            if (position >= getCount()) {
                position = getCount() - 1;
            }
            return getItem(position).sectionPosition;
        }
    }
}
