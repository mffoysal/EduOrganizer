package com.edu.eduorganizer.shortcut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.scanner.activity_qr_scanner;

public class ShortCut extends BaseMenu implements View.OnClickListener, View.OnLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_cut);

        if (getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            String shortcutId = getIntent().getStringExtra("android.intent.extra.shortcut.ID");
            if (shortcutId != null) {
                // Perform actions based on the shortcut ID
                if (shortcutId.equals("shortcut1")) {
                    // Handle shortcut1 action
                } else if (shortcutId.equals("shortcut2")) {
                    // Handle shortcut2 action
                }
            }
        }

    }


    private void createAppShortcut() {
        Intent shortcutIntent = new Intent(this, activity_qr_scanner.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "edu");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra("isLoggedIn", false);
        sendBroadcast(intent);
    }


    private void createShortcut() {
        Intent shortcutIntent = new Intent(this, activity_qr_scanner.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Edubox");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra("isLoggedIn", false);
        sendBroadcast(intent);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}