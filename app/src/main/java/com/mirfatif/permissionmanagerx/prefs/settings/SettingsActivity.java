package com.mirfatif.permissionmanagerx.prefs.settings;

import static com.mirfatif.permissionmanagerx.BuildConfig.APPLICATION_ID;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceFragmentCompat.OnPreferenceStartFragmentCallback;
import com.mirfatif.permissionmanagerx.R;
import com.mirfatif.permissionmanagerx.ui.base.BaseActivity;
import com.mirfatif.permissionmanagerx.util.Utils;

public class SettingsActivity extends BaseActivity implements OnPreferenceStartFragmentCallback {

  public static final String EXTRA_NO_PARENT = APPLICATION_ID + ".extra.NO_PARENT";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Utils.setNightTheme(this)) {
      return;
    }
    setContentView(R.layout.activity_fragment_container);

    mCloseOnBackPressed = SettingsFragFlavor.shouldCloseOnBackPressed(getIntent());

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(R.string.settings_menu_item);
      Intent intent = getIntent();
      if (intent != null && intent.getBooleanExtra(EXTRA_NO_PARENT, false)) {
        actionBar.setDisplayHomeAsUpEnabled(false);
      }
    }

    /*
     Check null to avoid:
      "IllegalStateException: Target fragment must implement TargetFragment interface"
     on rotation when a DialogPreference is visible.
     https://issuetracker.google.com/issues/137173772
    */
    if (savedInstanceState == null) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment_container, new SettingsFragFlavor())
          .commit();
    }
  }

  private boolean mCloseOnBackPressed = false;

  @Override
  public void onBackPressed() {
    if (mCloseOnBackPressed) {
      finishAfterTransition();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
    final Fragment fragment =
        getSupportFragmentManager()
            .getFragmentFactory()
            .instantiate(getClassLoader(), pref.getFragment());
    fragment.setArguments(pref.getExtras());
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit();
    setActionBarTitle(Utils.capitalizeWords(pref.getTitle().toString()));
    return true;
  }

  void setActionBarTitle(String title) {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(title);
    }
  }
}
