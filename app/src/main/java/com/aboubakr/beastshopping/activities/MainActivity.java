package com.aboubakr.beastshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.aboubakr.beastshopping.Dialog.AddListDialogFragment;
import com.aboubakr.beastshopping.R;
import com.aboubakr.beastshopping.infrastructure.Utils;
import com.aboubakr.beastshopping.services.ShoppingListService;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.activity_main_FAB)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String toolBarName;
        if(userName.contains(" ")){
            toolBarName = userName.substring(0,userName.indexOf(" ")) + "'s shopping list";
        }else {
            toolBarName = userName +"'s shopping list";
        }
        getSupportActionBar().setTitle(toolBarName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_main, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Utils.EMAIL, null).apply();
                editor.putString(Utils.USERNAME, null).apply();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.activity_main_FAB)
    public void setFloatingctionButton(){
        DialogFragment dialogFragment = AddListDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(),AddListDialogFragment.class.getSimpleName());
    }

}
