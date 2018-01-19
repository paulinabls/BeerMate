package com.psc.beermate.presentation.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class Navigator {
    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }
}
