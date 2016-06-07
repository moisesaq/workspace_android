package com.moises.playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.moises.broadcastreceiver.R;
import com.moises.generic.Audio;
import com.moises.generic.Global;
import com.moises.playlist.PlayListFragment.PlayListAdapter.ViewHolder;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayListFragment extends Fragment{
	public static final String TAG_FRAGMENT = "tagPlayListFrag";
	private View view;
	private ListView playList;
	
	private Handler myHandler = new Handler();
    private Runnable seekBarRunnable;
    private ViewHolder ultimoHolder;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.play_list, container, false);
		setup();
		return view;
	}
	
	private void setup(){
		playList = (ListView)view.findViewById(R.id.listViewPlayList);
		List<Song> listSongs = new ArrayList<Song>();
		List<File> listFileSongs = getFindListSong(Environment.getExternalStorageDirectory());
		for (File fileSong: listFileSongs) {
			listSongs.add(new Song()
								.setTitulo(fileSong.getName())
								.setPathFile(fileSong.getAbsolutePath())
			);
		}
		
		PlayListAdapter adapter = new PlayListAdapter(listSongs);
		playList.setAdapter(adapter);		
	}
	
	public List<File> getFindListSong(File root){
		List<File> listSong = new ArrayList<File>();
		
		File[] files = root.listFiles();
		for(File singleFile: files){
			//TODO No entendido repasar con internet
			if(singleFile.isDirectory() && !singleFile.isHidden()){
				listSong.addAll(getFindListSong(singleFile));
			}else{
				if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
					listSong.add(singleFile);
				}
			}
		}
		return listSong;
	}

	public class PlayListAdapter extends ArrayAdapter<Song>{
		
		public MediaPlayer mediaPlayer;
		public String afterPathSoung = "";
		public ViewHolder afterViewHolder;
		public Uri uri;
		
		public PlayListAdapter(List<Song> listSong){
			super(getActivity(), R.layout.song_item, listSong);
			mediaPlayer = new MediaPlayer();
		}
		
		public class ViewHolder{
			ImageButton play;
			ImageButton pause;
			TextView duration;
			SeekBar seekBar;
			TextView title;
		}
		
		@Override
		public Song getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if(view==null){
				view = getActivity().getLayoutInflater().inflate(R.layout.song_item, null);
				holder = new ViewHolder();
				holder.play = (ImageButton)view.findViewById(R.id.play);
				holder.pause = (ImageButton)view.findViewById(R.id.pause);
				holder.duration = (TextView)view.findViewById(R.id.duration);
				holder.seekBar = (SeekBar)view.findViewById(R.id.seekBar);
				holder.title = (TextView)view.findViewById(R.id.titulo);
				view.setTag(holder);
			}else{
				holder = (ViewHolder)view.getTag();
			}
			
			final Song song = this.getItem(position);
			
			holder.title.setText(song.getTitulo());
			int durationMax = (int)Audio.getAudioLength(song.getPathFile());
			holder.duration.setText(Audio.formatDuration((durationMax)));
			holder.seekBar.setMax(durationMax);
			
			holder.play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//playSong(song, holder);
					showPauseButton(holder);
					//Global.getAudio().stopAudioPlaying();
                    Global.getAudio().startAudioPlaying(song.getPathFile(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            showPlayButton(holder);
                            holder.seekBar.setProgress(0);
                        }
                    });

                    if (seekBarRunnable != null) myHandler.removeCallbacks(seekBarRunnable);
                    myHandler.postDelayed(seekBarRunnable = new Runnable() {
                        @Override
                        public void run() {
                        	holder.seekBar.setProgress(Global.getAudio().getAudioCurrentPosition());
//                            holder.durationSound.setText(
//                                    Global.getAudio().getFormattedDuration()
//                                            + " - "
//                                            + Global.getAudio().getFormattedCurrentPosition()
//                            );
                            myHandler.postDelayed(this, 500);
                        }
                    }, 500);

                    
				}
			});
			holder.pause.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//pauseSong(song, holder);
					Global.getAudio().stopAudioPlaying();
					holder.seekBar.setProgress(Global.getAudio().getAudioCurrentPosition());
                    showPlayButton(holder);
				}
			});
			
			return view;
		}
		
		public void progressSeek(ViewHolder holder){
			ultimoHolder = holder;
			ultimoHolder.seekBar.setProgress(Global.getAudio().getAudioCurrentPosition());
		}
		
		private void showPlayButton(ViewHolder holder) {
	        holder.play.setVisibility(View.VISIBLE);
	        holder.pause.setVisibility(View.GONE);
	    }

	    private void showPauseButton(ViewHolder holder) {
	        holder.play.setVisibility(View.GONE);
	        holder.pause.setVisibility(View.VISIBLE);
	    }
		
		public void playSong(Song song, ViewHolder holder){
			if(song.getPathFile().equals(afterPathSoung)){
				Global.toast("Sigue tocando "+afterPathSoung);
				mediaPlayer.start();
				afterViewHolder.play.setVisibility(View.INVISIBLE);
				afterViewHolder.pause.setVisibility(View.VISIBLE);
			}else{
				if(afterViewHolder == null){
					afterViewHolder = holder;
				}else{
					if(mediaPlayer.isPlaying()){
						mediaPlayer.stop();
						mediaPlayer.reset();
					}
					Global.toast("Detenido "+afterPathSoung);
					afterViewHolder.play.setVisibility(View.VISIBLE);
					afterViewHolder.pause.setVisibility(View.INVISIBLE);
				}
				
				Global.toast("Ahora toca "+song.getPathFile());
				uri = Uri.parse(song.getPathFile());
				mediaPlayer = MediaPlayer.create(getActivity(), uri);
				mediaPlayer.start();
				holder.play.setVisibility(View.INVISIBLE);
				holder.pause.setVisibility(View.VISIBLE);
				afterPathSoung = song.getPathFile();
				afterViewHolder = holder;
			}
		}
		
		public void pauseSong(Song song, ViewHolder holder){
			if(song.getPathFile().equals(afterPathSoung)){
				mediaPlayer.pause();
				Global.toast("Pausa "+afterPathSoung);
				afterViewHolder.play.setVisibility(View.VISIBLE);
				afterViewHolder.pause.setVisibility(View.INVISIBLE);
			}
		}
		
	}
	
	
}
