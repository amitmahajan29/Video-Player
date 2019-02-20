package acer.example.com.videoplayer;

import android.graphics.Bitmap;

/**
 * Created by Acer on 10/03/2018.
 */

public class VideoItem
{
    //This class was crated bcoz we have to store an image and text for listView. So in generics we could not put custom things, so
    //to put them in arrayAdapter we had to use arrayOfObject concept.
    public Bitmap icon;
    public String title;
    public String duration;

    public VideoItem(Bitmap icon, String title, String duration)
    {
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }
}
