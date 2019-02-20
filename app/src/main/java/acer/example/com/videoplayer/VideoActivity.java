package acer.example.com.videoplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends /*AppCompat*/Activity
//To hide the blue colour bar on top we did this
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        String path = getIntent().getExtras().getString("path");
        if(path!=null && !path.isEmpty())
        {
            VideoView videoView = (VideoView) findViewById(R.id.videoView);
            MediaController m = new MediaController(this);
            m.setAnchorView(videoView); //To associate the mediaContoller with videoView
            videoView.setMediaController(m);
            videoView.setVideoURI(Uri.parse(path)); //Converting the path to URI
            videoView.requestFocus(); //To prepare the videoPlayer
            videoView.start();
            //Below line is for finishing the current activity and going back to main activity after the end of video.
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer)
                {
                    finish();
                }
            });

        }
    }
}
