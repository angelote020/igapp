package com.angelote.testimagenes.Utilerias;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.angelote.testimagenes.R;

public abstract class UTBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack) {
        mostrarFragment(fragmentClass, containerViewId, bundle, addToBackStack, false);
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack, boolean clearStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (clearStack) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                fragmentManager.popBackStack();
            }
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
            } catch (Exception e) {
                throw new RuntimeException("error", e);
            }
        }
        fragmentTransaction.setCustomAnimations(R.anim.entrada, R.anim.salida, R.anim.entrada, R.anim.salida);
        fragmentTransaction.show(fragment);
        fragmentTransaction.replace(containerViewId, fragment, fragmentClass.getSimpleName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentClass.getName());
        }
        fragmentTransaction.commit();
    }

}