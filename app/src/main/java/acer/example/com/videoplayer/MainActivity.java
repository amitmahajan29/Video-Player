package acer.example.com.videoplayer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    List<VideoItem> videoItem;
    List<String> lstPath;
    VideoAdapter adapter;
    ProgressBar pb;

    private void playVideo(String strPath)
    {
        Intent i = new Intent(this, VideoActivity.class);
        i.putExtra("path",strPath);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(lstPath.size() > 0)
                {
                    playVideo(lstPath.get(0));
                }
            }
        });
        pb = (ProgressBar) findViewById(R.id.progressBar);
        ListView lst = (ListView) findViewById(R.id.lvVideos);
        videoItem = new ArrayList<>();
        adapter = new VideoAdapter(MainActivity.this, R.layout.itemrow,videoItem);
        ArrayAdapter<String> videos = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lst.setAdapter(adapter);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                playVideo(lstPath.get(position));
            }
        });


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        new FetchVideosTask().execute();
    }

    public class FetchVideosTask extends AsyncTask<Void, Integer, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String[] projection = {MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA};
            ContentResolver resolver = getContentResolver();

            final Cursor vc = resolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null, null,
                    MediaStore.Video.Media.DATE_ADDED+" "+"desc");

            lstPath = new ArrayList<>();
            if(vc.moveToFirst() && vc!=null)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        pb.setMax(vc.getCount());
                    }
                });

                do
                {
                    int colIndex = vc.getColumnIndex(MediaStore.Video.Media.DATA);
                    String path = vc.getString(colIndex);

                    colIndex = vc.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                    String name = vc.getString(colIndex);

                    colIndex = vc.getColumnIndex(MediaStore.Video.Media.DURATION);
                    String duration = vc.getString(colIndex);

                    Bitmap bmpThumb = ThumbnailUtils.createVideoThumbnail(path,MediaStore.Video.Thumbnails.MICRO_KIND);

                    VideoItem item = new VideoItem(bmpThumb,name,duration);
                    videoItem.add(item);
                    lstPath.add(path);
                    publishProgress(1);
                }while(vc.moveToNext());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            pb.incrementProgressBy(1);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            pb.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
