package com.example.app_sample.utils;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.RecipeImage;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Response;

public class DownloadService extends Service {

    RecipeRepository repo;

    @Override
    public void onCreate() {
        super.onCreate();

        repo = new RecipeRepository(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = intent.getIntExtra("id", 0);
        new DownloadAsyncTask().execute(id);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class DownloadAsyncTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... ids) {
            long id = ids[0];
            try{
                String string = repo.getRecipeCard(id).execute().body().getUrl();

                URL url = new URL(string);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.connect();
                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();
                    Bitmap bmp = BitmapFactory.decodeStream(in);
                    in.close();

                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                    File file = new File(path, "Image_" + id + ".jpg");
                    path.mkdirs();

                    OutputStream fOutputStream = new FileOutputStream(file);

                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

                    fOutputStream.flush();
                    fOutputStream.close();

                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                }


            } catch (IOException e) {
                return "Something went wrong";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null)
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }
}
