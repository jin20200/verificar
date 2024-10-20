package com.easyplexdemoapp.util;

import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class RootCheckUtil {

    private static final List<String> COMMON_ROOT_PATHS = Arrays.asList(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
    );

    private static final List<String> DANGEROUS_APPS = Arrays.asList(
            "com.noshufou.android.su",
            "com.thirdparty.superuser",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.zachspong.temprootremovejb",
            "com.ramdroid.appquarantine"
    );

    private static final List<String> DANGEROUS_BINARY_PATHS = Arrays.asList(
            "/system/bin/",
            "/system/xbin/",
            "/system/sbin/",
            "/sbin/",
            "/vendor/bin/"
    );

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4() || checkRootMethod5();
    }

    // Check for common system file presence
    private static boolean checkRootMethod1() {
        for (String path : COMMON_ROOT_PATHS) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    // Check for the presence of the "su" command
    private static boolean checkRootMethod2() {
        return executeCommand("/system/xbin/which su") != null;
    }

    // Check for device properties that indicate root access
    private static boolean checkRootMethod3() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    // Check for dangerous apps
    private static boolean checkRootMethod4() {
        for (String packageName : DANGEROUS_APPS) {
            if (isPackageInstalled(packageName)) {
                return true;
            }
        }
        return false;
    }

    // Check for dangerous binaries
    private static boolean checkRootMethod5() {
        for (String path : DANGEROUS_BINARY_PATHS) {
            if (new File(path + "su").exists() || new File(path + "busybox").exists()) {
                return true;
            }
        }
        return false;
    }

    // Helper method to execute a command and return the output
    private static String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return in.readLine();
            }
        } catch (Throwable t) {
            return null;
        }
    }

    // Helper method to check if a package is installed
    private static boolean isPackageInstalled(String packageName) {
        try {
            return new File("/data/app/" + packageName + "-1/base.apk").exists() ||
                    new File("/data/app/" + packageName + "-2/base.apk").exists();
        } catch (Throwable t) {
            return false;
        }
    }
}
