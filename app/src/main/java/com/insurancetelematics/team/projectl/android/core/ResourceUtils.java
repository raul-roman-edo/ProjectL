package com.insurancetelematics.team.projectl.android.core;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class ResourceUtils {
    public static final String TAG = ResourceUtils.class.getName();
    private static final int ONEK = 1024;
    private static final String EMPTY = "";

    public static String extractStringFromRawResource(Context context, int resourceId) {
        InputStream is;
        try {
            is = context.getResources().openRawResource(resourceId);
        } catch (Resources.NotFoundException nfe) {
            Log.d(TAG, "extractStringFromRawResource", nfe);
            return EMPTY;
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[ONEK];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException ue) {
            Log.d(TAG, "extractStringFromRawResource", ue);
        } catch (IOException ioe) {
            Log.d(TAG, "extractStringFromRawResource", ioe);
        } finally {
            try {
                is.close();
            } catch (IOException anotherioe) {
                Log.d(TAG, "extractStringFromRawResource", anotherioe);
            }
        }
        String jsonString = writer.toString();
        return jsonString;
    }
}
