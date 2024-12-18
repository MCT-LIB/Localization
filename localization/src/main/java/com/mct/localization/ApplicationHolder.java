package com.mct.localization;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

class ApplicationHolder {

    private static Application sApplication;

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param context context
     */
    public static void init(final Context context) {
        init(context == null ? null : (Application) context.getApplicationContext());
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(final Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
        }
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApplication() {
        if (sApplication == null) {
            init(getApplicationByReflect());
        }
        return sApplication;
    }

    @Nullable
    @SuppressLint("PrivateApi")
    private static Application getApplicationByReflect() {
        try {
            // Obtain the application object of the current process through reflection
            Application app = (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null);
            if (app != null) {
                return app;
            }
        } catch (Exception ignored) {
            try {
                Application app = (Application) Class.forName("android.app.AppGlobals")
                        .getMethod("getInitialApplication")
                        .invoke(null);
                if (app != null) {
                    return app;
                }
            } catch (Exception ignored1) {
            }
        }
        return null;
    }

    private ApplicationHolder() {
        //no instance
    }
}
