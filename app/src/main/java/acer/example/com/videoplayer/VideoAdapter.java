package acer.example.com.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Acer on 10/03/2018.
 */

//Concept of custom list in ListView is used in this class. Instead of arrayAdapter we are using our own adapter.
//This class was crated bcoz we have to store an image and text for listView. So in generics we could not put custom things, so
//to put them in arrayAdapter we had to use arrayOfObject concept.
public class VideoAdapter extends ArrayAdapter<VideoItem>
{
    private Context context;
    private int layoutResourceId;
    private List<VideoItem> data = null;

    public VideoAdapter(@NonNull Context context, int resource, @NonNull List<VideoItem> objects)
    {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflator = ((Activity) context).getLayoutInflater();
            //convertView = inflator.inflate(position,parent,true);
            convertView = inflator.inflate(layoutResourceId,parent,false);
        }
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.ivThumbnail); //Standalone layouts need to be converted first.
        //we are accessing some random layout into this class so we inflated it and then used it in code
        TextView txtTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView txtDuration = (TextView) convertView.findViewById(R.id.tvDuration);

        VideoItem video = data.get(position);
        txtTitle.setText(video.title);
        imgIcon.setImageBitmap(video.icon);
        txtDuration.setText(video.duration);

        return convertView;
    }
}
