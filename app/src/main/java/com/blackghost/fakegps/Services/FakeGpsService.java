package com.blackghost.fakegps.Services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class FakeGpsService extends Service {
    public static final String CHANNEL_ID = "fakegps_channel";
    private static final int NOTIFICATION_ID = 1001;
//    private final IBinder binder = new LocalBinder();
    private boolean running = false;

    public class LocalBinder extends Binder {
        public FakeGpsService getService() {
            return FakeGpsService.this;
        }
    }

    private void startForeground(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
