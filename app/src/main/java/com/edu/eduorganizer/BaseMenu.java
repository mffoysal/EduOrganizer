package com.edu.eduorganizer;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.edu.eduorganizer.bubble.AppWidget;
import com.edu.eduorganizer.bubble.Widget;
import com.edu.eduorganizer.mess.SecondHome;
import com.edu.eduorganizer.update.AppUpdate;
import com.edu.eduorganizer.user.UserProfile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseMenu extends AppCompatActivity {

    private static final int REQUEST_PICK_APPWIDGET = 101;
    private static final int YOUR_REQUEST_CODE = 123;
    private static final int APPWIDGET_HOST_ID = 1;
    private Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem menuItem = menu.findItem(R.id.fsettingId);
        MenuItem menuItem2 = menu.findItem(R.id.flogoutId);
        MenuItem menuItem3 = menu.findItem(R.id.profileId);
        MenuItem menuItem4 = menu.findItem(R.id.allUserId);
        MenuItem menuItem5 = menu.findItem(R.id.menuId);
        MenuItem menuItem6 = menu.findItem(R.id.settingId);
        MenuItem menuItem7 = menu.findItem(R.id.shareId);
        MenuItem menuItem8 = menu.findItem(R.id.aboutId);
        MenuItem menuItem9 = menu.findItem(R.id.setSettings);
        MenuItem menuItem10 = menu.findItem(R.id.wifiWebMenu);
        MenuItem menuItem11 = menu.findItem(R.id.updateAppMenu);
        MenuItem menuItem12 = menu.findItem(R.id.widgetAppMenu);
        MenuItem menuItem13 = menu.findItem(R.id.messId);
//        SpannableString spannableString = new SpannableString(menuItem.getTitle());
//        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.menu_item_color)),
//                0, spannableString.length(), 0);
//        menuItem.setTitle(spannableString);
//
//        SpannableString spannableString2 = new SpannableString(menuItem2.getTitle());
//        spannableString2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.menu_item_color)),
//                0, spannableString2.length(), 0);
//        menuItem2.setTitle(spannableString2);

        setColor(menuItem);
        setColor(menuItem2);
        setColor(menuItem3);
        setColor(menuItem4);
        setColor(menuItem5);
        setColor(menuItem6);
        setColor(menuItem7);
        setColor(menuItem8);
        setColor(menuItem9);
        setColor(menuItem10);
        setColor(menuItem11);
        setColor(menuItem12);
        setColor(menuItem13);
        return true;
    }

    public void setColor(MenuItem menuItem){
        SpannableString spannableString = new SpannableString(menuItem.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.menu_item_color)),
                0, spannableString.length(), 0);
        menuItem.setTitle(spannableString);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_layout,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.settingId){

//            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
//            intent.putExtra("profile","Farhad Foysal\n+8801585855075");
//            startActivity(intent);

            return true;
        } else if (item.getItemId()==android.R.id.home) {

            onBackPressed();

            return true;
        }else if (item.getItemId()==R.id.messId) {

            Intent intent = new Intent(getApplicationContext(), SecondHome.class);
//            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("profile","Farhad Foysal\n+8801585855075");
            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.profileId) {

            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("profile","Farhad Foysal\n+8801585855075");
            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.fsettingId) {

//            Intent intent = new Intent(getApplicationContext(), MainPanelActivity.class);
//            intent.putExtra("profile","Farhad Foysal\n+8801585855075");
//            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.flogoutId) {

            Logout logout = new Logout(getApplicationContext());
            logout.getOut();
            finish();

            return true;
        }else if (item.getItemId()==R.id.allUserId) {

//            intent = new Intent(BaseMenu.this,AllUsersActivity.class);
////            intent.putExtra("users","01585855075");
//            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.aboutId) {

//            Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
//            intent.putExtra("users","01585855075");
//            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.setSettings) {

//            Intent intent = new Intent(getApplicationContext(), Permission.class);
//            intent.putExtra("users","01585855075");
//            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.wifiWebMenu) {

//            Intent intent = new Intent(getApplicationContext(), WifiWeb.class);
//            intent.putExtra("users","01585855075");
//            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.updateAppMenu) {

            Intent intent = new Intent(getApplicationContext(), AppUpdate.class);
            intent.putExtra("users","01585855075");
            startActivity(intent);

            return true;
        }else if (item.getItemId()==R.id.menuId) {
//            NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext(),"FarhadFoysal");
//            notificationHelper.showBigTextNotification("Edubox","Hello farhad foysal","Farid Ahmed\nRojina Akter\nSanjida Farid Najifa","FarhadFoysal");
//
            return true;
        }else if (item.getItemId()==R.id.widgetAppMenu) {
            Intent widgetPickerIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
            widgetPickerIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            startActivityForResult(widgetPickerIntent, REQUEST_PICK_APPWIDGET);
            return true;
        }else if (item.getItemId()==R.id.shareId) {
//            Map<String, String> msg = new HashMap<>();
//            msg.put("01585855075","Hello Farhad Foysal");
//
//            Intent intent = new Intent(getApplicationContext(), sendSmss.class);
//            intent.putExtra("user","");
//            intent.putExtra("userMessages", (Serializable) msg);
//            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                AppWidgetHost appWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
                appWidgetHost.startListening();
                AppWidgetHostView hostView = appWidgetHost.createView(this, widgetId, new AppWidgetProviderInfo());

                // Add the widget to the home screen (not to your custom layout).
//                appWidgetHost.addWidget(hostView);

                // Start listening for updates.
                appWidgetHost.startListening();

//                appWidgetHost.stopListening();

            }
        }
    }


}
