package team.e.com.moneykeeper.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import team.e.com.moneykeeper.fragments.CategoryEditFragment;

public class CategoryEditActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        insertFragment(new CategoryEditFragment());
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
