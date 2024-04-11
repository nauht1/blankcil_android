package hcmute.com.blankcil.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.com.blankcil.view.AccountFragment;
import hcmute.com.blankcil.view.ChatFragment;
import hcmute.com.blankcil.view.FriendsFragment;
import hcmute.com.blankcil.view.HomeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new FriendsFragment();
            case 2:
                return new ChatFragment();
            case 3:
                return new AccountFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
