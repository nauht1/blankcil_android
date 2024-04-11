package hcmute.com.blankcil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import hcmute.com.blankcil.adapter.ViewPagerAdapter;
import hcmute.com.blankcil.databinding.ActivityMainBinding;
import hcmute.com.blankcil.view.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mViewPager;
    private BottomNavigationView mBottomNavigationView;
    ActivityMainBinding activityMainBinding;
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewPager);
        mBottomNavigationView = findViewById(R.id.bottomNav);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(viewPagerAdapter);

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
                        mBottomNavigationView.getMenu().findItem(R.id.friendTab).setChecked(true);
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
                if (item.getItemId() == R.id.friendTab) {
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
}