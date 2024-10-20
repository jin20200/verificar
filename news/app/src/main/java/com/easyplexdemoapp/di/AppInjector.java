package com.easyplexdemoapp.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.easyplexdemoapp.EasyPlexApp;
import com.easyplexdemoapp.di.component.DaggerAppComponent;
import org.jetbrains.annotations.NotNull;
import dagger.android.AndroidInjection;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;

/**
 * Helper class to automatically inject fragments if they implement {link Injectable}.
 *
 * @author Yobex.
 */
public class AppInjector {

    private AppInjector() {
    }

    public static void init(EasyPlexApp easyPlexApp) {

        DaggerAppComponent
                .builder()
                .application(easyPlexApp)
                .build()
                .inject(easyPlexApp);

        easyPlexApp.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(@NotNull Activity activity, Bundle savedInstanceState) {

                        activity.getFragmentManager();

                        if(activity instanceof FragmentActivity) {
                            ((FragmentActivity)activity).getSupportFragmentManager();
                        }

                        handleActivity(activity);
                    }

                    @Override
                    public void onActivityStarted(@NotNull Activity activity) {

                        //

                    }

                    @Override
                    public void onActivityResumed(@NotNull Activity activity) {

                        //

                    }

                    @Override
                    public void onActivityPaused(@NotNull Activity activity) {

                        //

                    }

                    @Override
                    public void onActivityStopped(@NotNull Activity activity) {

                        //

                    }

                    @Override
                    public void onActivitySaveInstanceState(@NotNull Activity activity, Bundle outState) {

                        //

                    }

                    @Override
                    public void onActivityDestroyed(@NotNull Activity activity) {


                        //

                    }
                });
    }

    private static void handleActivity(Activity activity) {
        if (activity instanceof HasAndroidInjector) {
            AndroidInjection.inject(activity);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            new FragmentManager.FragmentLifecycleCallbacks() {
                                @Override
                                public void onFragmentCreated(@NotNull FragmentManager fm, @NotNull Fragment f,
                                                              Bundle savedInstanceState) {
                                    if (f instanceof Injectable) {
                                        AndroidSupportInjection.inject(f);
                                    }
                                }
                            }, true);
        }
    }

}
