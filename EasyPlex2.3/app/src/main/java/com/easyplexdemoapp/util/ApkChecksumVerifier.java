package com.easyplexdemoapp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

public class ApkChecksumVerifier {


    public static String getApkSignatureSHA256(Context context) {
        try {
            // Get the package name of the app
            String packageName = context.getPackageName();

            // Get the package manager
            PackageManager packageManager = context.getPackageManager();

            // Get package information including signatures
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            // Get the first signature (assuming there is only one signature)
            Signature signature = packageInfo.signatures[0];

            // Get the signature bytes
            byte[] signatureBytes = signature.toByteArray();

            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Update the MessageDigest with the signature bytes
            md.update(signatureBytes);

            // Get the digest bytes
            byte[] digestBytes = md.digest();

            // Convert the digest bytes to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte digestByte : digestBytes) {
                sb.append(String.format("%02x", digestByte));
            }

            // Return the SHA-256 hash as a string
            return sb.toString();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Timber.tag("TAG").e(e, "Error retrieving APK signature SHA-256 hash");
        }

        // Return null in case of an error
        return null;
    }


}
