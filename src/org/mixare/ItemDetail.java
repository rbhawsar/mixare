package org.mixare;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ItemDetail extends Activity
{

    private static String SERVER_URL = "";

    // private textView
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ImageView image = (ImageView)findViewById(R.id.imageView1);
        bitmap = MixView.thumbnail;
        if (bitmap != null)
            image.setImageBitmap(bitmap);

        Button uploadButton = (Button)findViewById(R.id.button1);

        uploadButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            //executeMultipartPost();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();

                //Return to previous activity.
                finish();

            }

        });

    }

    public void executeMultipartPost() throws Exception
    {
        try
        {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            bitmap.compress(CompressFormat.JPEG, 50, bos);

            MixView.thumbnail = null;

            byte[] data = bos.toByteArray();

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost(

            SERVER_URL);

            String fileName = String.format("File_%d.png", new Date().getTime());
            ByteArrayBody bab = new ByteArrayBody(data, fileName);

            // File file= new File("/mnt/sdcard/forest.png");

            // FileBody bin = new FileBody(file);

            MultipartEntity reqEntity = new MultipartEntity(

            HttpMultipartMode.BROWSER_COMPATIBLE);

            reqEntity.addPart("file", bab);

            postRequest.setEntity(reqEntity);
            int timeoutConnection = 60000;
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 60000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);

            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader reader = new BufferedReader(new InputStreamReader(

            response.getEntity().getContent(), "UTF-8"));

            String sResponse;

            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null)
            {

                s = s.append(sResponse);

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_detail, menu);
        return true;
    }

}
