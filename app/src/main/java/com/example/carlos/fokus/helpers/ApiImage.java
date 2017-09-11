package com.example.carlos.fokus.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Carlos on 10/09/2017.
 */



public class ApiImage {
    private Uri fileUri;
    private ImageView img_forCompress;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Fokus";
    static File mediaFile;

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        Log.e("path", "media file:-" + mediaFile);
        return mediaFile;
    }

    public static File compressImage(File imgFileOrig) {
        // we'll start with the original picture already open to a file
        File f = null; //change "getPic()" for whatever you need to open the image file.
        Bitmap b = BitmapFactory.decodeFile(imgFileOrig.getAbsolutePath());
       // original measurements
        int origWidth = b.getWidth();
        int origHeight = b.getHeight();

        final int destWidth = 600;//or the width you need

        if(origWidth > destWidth){
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight/( origWidth / destWidth ) ;
            // we create an scaled bitmap so it reduces the image, not just trim it
            Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            b2.compress(Bitmap.CompressFormat.JPEG,70 , outStream);
            // we save the file, at least until we have made use of it
            f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "test.jpg");
            try {
                f.createNewFile();
                //write the bytes in file
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(outStream.toByteArray());
                // remember close de FileOutput
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return f;
    }
}
