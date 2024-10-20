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

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyplexdemoapp.ui.downloadmanager.core.exception.FileAlreadyExistsException;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public interface FileSystemFacade
{
    void seek(@NonNull FileOutputStream fout, long offset) throws IOException;

    void allocate(@NonNull FileDescriptor fd, long length) throws IOException;

    void closeQuietly(Closeable closeable);

    String makeFilename(@NonNull Uri dir,
                        @NonNull String desiredFileName);

    void moveFile(@NonNull Uri srcDir,
                  @NonNull String srcFileName,
                  @NonNull Uri destDir,
                  @NonNull String destFileName,
                  boolean replace) throws IOException, FileAlreadyExistsException;

    void copyFile(@NonNull Uri srcFile,
                  @NonNull Uri destFile,
                  boolean truncateDestFile) throws IOException;

    FileDescriptorWrapper getFD(@NonNull Uri path);

    String getExtensionSeparator();

    String appendExtension(@NonNull String fileName, @NonNull String mimeType);

    @Nullable
    String getDefaultDownloadPath();

    @Nullable
    String getUserDirPath();

    boolean deleteFile(@NonNull Uri path) throws FileNotFoundException;

    Uri getFileUri(@NonNull Uri dir,
                   @NonNull String fileName);

    Uri getFileUri(@NonNull String relativePath,
                   @NonNull Uri dir);

    Uri createFile(@NonNull Uri dir,
                   @NonNull String fileName,
                   boolean replace) throws IOException;

    long getDirAvailableBytes(@NonNull Uri dir);

    String getExtension(String fileName);

    boolean isValidFatFilename(String name);

    String buildValidFatFilename(String name);

    String getDirName(@NonNull Uri dir);

    long getFileSize(@NonNull Uri filePath);

    void truncate(@NonNull Uri filePath, long newSize) throws IOException;

    void takePermissions(@NonNull Uri path);

    String getDirPath(@NonNull Uri dir);

    boolean exists(@NonNull Uri filePath);
}
