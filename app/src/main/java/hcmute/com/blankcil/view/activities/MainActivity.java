package hcmute.com.blankcil.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.view.adapter.PodcastAdapter;
import hcmute.com.blankcil.view.adapter.ViewPagerAdapter;
import hcmute.com.blankcil.view.fragments.CreatePodcastFragment;
import hcmute.com.blankcil.view.fragments.HomeFragment;
import hcmute.com.blankcil.view.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabCreatePodcast;
    private BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    // Hiển thị lại nút fabCreatePodcast khi fragment được đóng
                    fabCreatePodcast.setVisibility(View.VISIBLE);
                }
            }
        });

        mViewPager = findViewById(R.id.viewPager);
        mBottomNavigationView = findViewById(R.id.bottomNav);
        fabCreatePodcast = findViewById(R.id.fabCreatePodcast);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(viewPagerAdapter);

        fabCreatePodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setVisibility(View.GONE);
                mBottomNavigationView.setVisibility(View.GONE);
                bottomAppBar.setVisibility(View.GONE);

                Fragment mFragment = null;
                mFragment = new CreatePodcastFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mFragment).commit();
            }
        });

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.searchTab).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.chatTab).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.userTab).setChecked(true);
                        break;
                    default:
                        mBottomNavigationView.getMenu().findItem(R.id.homeTab).setChecked(true);
                        break;
                }
            }
        });

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.searchTab) {
                    mViewPager.setCurrentItem(1);
                    return true;
                } else if (item.getItemId() == R.id.chatTab) {
                    mViewPager.setCurrentItem(2);
                    return true;
                } else if (item.getItemId() == R.id.userTab) {
                    mViewPager.setCurrentItem(3);
                    return true;
                } else if (item.getItemId() == R.id.homeTab) {
                    mViewPager.setCurrentItem(0);
                    return true;
                }
                return false;
            }
        });
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openProfileFragment(int userId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(); // Xóa HomeFragment khỏi ngăn xếp fragment trước khi thêm ProfileFragment

        // Dừng video trước khi thêm ProfileFragment
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (homeFragment != null) {
            homeFragment.stopVideos();
        }

        Fragment profileFragment = ProfileFragment.newInstance(userId);
        replaceFragment(profileFragment);
    }
}