package com.easyplexdemoapp.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import timber.log.Timber;


public class DownloadFileAsync extends AsyncTask<String, String, String> {

    private static final String TAG ="DOWNLOADFILE";

    private final PostDownload callback;
    private File file;
    private final String downloadLocation;

    public DownloadFileAsync(String downloadLocation, PostDownload callback){
        this.callback = callback;
        this.downloadLocation = downloadLocation;
    }
    @Override
    protected String doInBackground(String... aurl) {
        int count;

        try {

            URL url = new URL(aurl[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int lenghtOfFile = con.getContentLength();
            Timber.d("Length of the file: %s", lenghtOfFile);


            try (InputStream input = new BufferedInputStream(url.openStream())) {
                file = new File(downloadLocation);
                try (FileOutputStream output = new FileOutputStream(file)) {
                    Timber.d("file saved at %s", file.getAbsolutePath());

                    byte[] data = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                        output.write(data, 0, count);
                    }

                    output.flush();

                }
            }
        } catch (Exception ignored) {


            //

        }
        return null;

    }


    @Override
    protected void onProgressUpdate(String... progress) {


        //


    }

    @Override
    protected void onPostExecute(String unused) {
        if(callback != null) {
            try {
                callback.downloadDone(file);
            } catch (IOException e) {


                Log.d(TAG, "" + Arrays.toString(e.getStackTrace()));

            }
        }
    }

    public interface PostDownload{
        void downloadDone(File fd) throws IOException;
    }
}