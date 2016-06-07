package com.moises.generic;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Base64;

import com.moises.generic.Global;

import java.io.File;
import java.io.FileOutputStream;

public class Audio {
    private boolean recording = false;
    private boolean playing = false;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MediaRecorder mediaRecorder = new MediaRecorder();
    private OnCompletionListener listener;

    public void startAudioPlaying(int resourceId) {
        startAudioPlaying(resourceId, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
    }

    public void startAudioPlaying(int resourceId, final MediaPlayer.OnCompletionListener onCompletionListener) {
        if (!isRecordingOrPlaying()) {
            playing = true;

            try {
                mediaPlayer = MediaPlayer.create(Global.getContext(), resourceId);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onCompletionListener.onCompletion(mp);
                    }
                });
            } catch (Exception e) {
                stopAudioPlaying();
            }
        }
    }

    public void startAudioPlaying(String filename) {
        startAudioPlaying(filename, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
    }

    @SuppressWarnings("static-access")
	public void startAudioPlaying(String filePath, final MediaPlayer.OnCompletionListener onCompletionListener) {
        if (isPlaying()) {
            stopAudioPlaying();
        }
        try {
            listener = onCompletionListener;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudioPlaying();
                }
            });
            //mediaPlayer = MediaPlayer.create(Global.getContext(), new Uri().parse(filePath));
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Global.toast("Toca "+filePath);
            playing = true;
        } catch (Exception e) {
        	e.printStackTrace();
            stopAudioPlaying();
        }
    }

    public void stopAudioPlaying() {
        try {
        	//mediaPlayer.seekTo(mediaPlayer.getDuration() - 1);
            listener.onCompletion(mediaPlayer);
            mediaPlayer.stop();
            mediaPlayer.reset();
            Global.toast("Anterior stop");
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        playing = false;
    }

    public void pauseAudioPlaying() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        playing = false;
    }

    public void startAudioRecording(String outputFilename) {
        stopAudioPlaying();

        if(!isRecording()) {
            recording = true;

            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setOutputFile(outputFilename);

                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (Exception e) {
                recording = false;
                e.printStackTrace();
            }
        }
    }

    public void stopAudioRecording() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.reset();
            }
        } catch (Exception e) {
        }

        recording = false;
    }

    public void writeAudioToDisk(String folder, String filename, String bytes) {
        try {
            File file = new File(folder, filename);
            if (!file.exists()) {
                FileOutputStream fileOutputStream = new FileOutputStream(folder + filename);
                fileOutputStream.write(Base64.decode(bytes, 0));
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean eraseAudioFromDisk(String folder, String filename) {
        File file = new File(folder, filename);
        return file.exists() && file.delete();
    }

    public static long getAudioLength(String filename) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(filename);
        String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.valueOf(duration);
    }

    public int getAudioCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public static String formatDuration(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60 ;
        int minutes = ((milliseconds / (1000 * 60)) % 60);
        int hours   = ((milliseconds / (1000 * 60 * 60)) % 24);
        return ((hours < 10) ? "0"  +hours : hours) + ":" +
                ((minutes < 10) ? "0" + minutes : minutes) + ":" +
                ((seconds < 10) ? "0" +seconds : seconds);
    }

    public String getFormattedDuration() {
        return formatDuration(mediaPlayer.getDuration());
    }

    public String getFormattedCurrentPosition() {
        return formatDuration(mediaPlayer.getCurrentPosition());
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isRecordingOrPlaying() {
        return isRecording() || isPlaying();
    }
}
