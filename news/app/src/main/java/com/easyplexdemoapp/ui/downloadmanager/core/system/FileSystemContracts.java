/*
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.core.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.downloadmanager.ui.filemanager.FileManagerConfig;
import com.easyplexdemoapp.ui.downloadmanager.ui.filemanager.FileManagerDialog;

public final class FileSystemContracts {
    private FileSystemContracts() {}

    /**
     * An {@link ActivityResultContract} to prompt the user to select a directory, returning the
     * user selection as a {@link Uri}. Apps can fully manage documents within the returned
     * directory.
     * <p>
     * The input is an optional {@link Uri} of the initial starting location.
     */
    public static class OpenDirectory extends ActivityResultContracts.OpenDocumentTree {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, @Nullable Uri input) {
            Intent i;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                i = new Intent(context, FileManagerDialog.class);
                String dirPath = null;
                if (input != null && Utils.isFileSystemPath(input)) {
                    dirPath = input.getPath();
                }
                FileManagerConfig config = new FileManagerConfig(
                        dirPath,
                        context.getString(R.string.select_folder_to_save),
                        FileManagerConfig.DIR_CHOOSER_MODE
                );
                i.putExtra(FileManagerDialog.TAG_CONFIG, config);
            } else {
                i = super.createIntent(context, input);
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

            }
            return i;
        }
    }

    /**
     * An {@link ActivityResultContract} to prompt the user to open a document, receiving its
     * contents as a {@code file:/http:/content:} {@link Uri}.
     * <p>
     * The input is the mime types to filter by, e.g. {@code image/*}.
     */
    public static class OpenFile extends ActivityResultContracts.OpenDocument {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, @NonNull String[] input) {
            Intent i;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                i = new Intent(context, FileManagerDialog.class);
                FileManagerConfig config = new FileManagerConfig(
                        null,
                        null,
                        FileManagerConfig.FILE_CHOOSER_MODE
                );
                config.mimeType = input.length > 0 ? input[0] : null;
                i.putExtra(FileManagerDialog.TAG_CONFIG, config);
            } else {
                i = super.createIntent(context, input);
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

            }
            return i;
        }
    }
}
