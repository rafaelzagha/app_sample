package com.example.app_sample.data.remote;

import android.content.Context;

import com.example.app_sample.R;
import com.example.app_sample.data.remote.api.FoodService;

public class KeyManager {

    private int index;
    private final String[] keys;

    public KeyManager(Context context) {
        index = 0;
        keys = new String[]{context.getString(R.string.key1),
                context.getString(R.string.key2),
                context.getString(R.string.key3),
                context.getString(R.string.key4),
                context.getString(R.string.key5)};
        changeApiKey();
    }

    public boolean incrementIndex(){
        if(index != keys.length-1){
            index++;
            changeApiKey();
            return true;
        }
        else
            return false;
    }

    private void changeApiKey(){
        FoodService.changeApiKey(keys[index]);
    }
}
