package com.edu.eduorganizer.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edu.eduorganizer.login.ui.login.FragmentAdminTab;

public class LoginPagerAdapter extends FragmentStateAdapter {
    public LoginPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new Fragment_signup_tab();
        } else if (position==2) {
            return new FragmentAdminTab();
        }
        return new FragmentLoginTab();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}