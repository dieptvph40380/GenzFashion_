package fpl.md37.genz_fashion.utils;

import android.content.Context;
import android.widget.Toast;

public class AndroidUtil {
    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
