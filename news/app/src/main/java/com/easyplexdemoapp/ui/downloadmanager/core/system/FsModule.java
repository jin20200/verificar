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

import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * A platform dependent filesystem interface, that uses in FileSystemFacade.
 */

interface FsModule
{
    String getName(@NonNull Uri filePath);

    /*
     * Returns path (if present) or directory name
     */

    String getDirName(@NonNull Uri dir);

    /*
     * Returns Uri of the file by the given file name or
     * null if the file doesn't exists
     */

    Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create) throws IOException;

    /*
     * Returns a file (if exists) Uri by relative path (e.g foo/bar.txt)
     * from the pointed directory
     */

    Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir);

    boolean delete(@NonNull Uri filePath) throws FileNotFoundException;

    boolean exists(@NonNull Uri filePath);

    FileDescriptorWrapper openFD(@NonNull Uri path);

    /*
     * Return the number of bytes that are free on the file system
     * backing the given Uri
     */

    long getDirAvailableBytes(@NonNull Uri dir) throws IOException;

    long getFileSize(@NonNull Uri filePath);

    void takePermissions(@NonNull Uri path);

    /*
     * Returns path (if present) or directory name
     */

    String getDirPath(@NonNull Uri dir);
}
