package hcmute.com.blankcil.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.com.blankcil.view.fragments.AccountFragment;
import hcmute.com.blankcil.view.fragments.NotificationFragment;
import hcmute.com.blankcil.view.fragments.HomeFragment;
import hcmute.com.blankcil.view.fragments.SearchFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new SearchFragment();
            case 2:
                return new NotificationFragment();
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
