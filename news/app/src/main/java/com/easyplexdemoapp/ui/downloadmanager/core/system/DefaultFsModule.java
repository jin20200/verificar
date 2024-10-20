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
import android.net.Uri;
import android.os.StatFs;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

class DefaultFsModule implements FsModule
{
    private final Context appContext;

    public DefaultFsModule(@NonNull Context appContext)
    {
        this.appContext = appContext;
    }

    @Override
    public String getName(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).getName();
    }

    @Override
    public String getDirName(@NonNull Uri dir)
    {
        return dir.getPath();
    }

    @Override
    public Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create) throws IOException
    {
        File f = new File(dir.getPath(), fileName);
        if (create)
            f.createNewFile();

        return (f.exists() ? Uri.fromFile(f) : null);
    }

    @Override
    public Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir)
    {
        if (!relativePath.startsWith(File.separator))
            relativePath = File.separator + relativePath;
        File f = new File(dir.getPath() + relativePath);

        return (f.exists() ? Uri.fromFile(f) : null);
    }

    @Override
    public boolean delete(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).delete();
    }

    @Override
    public boolean exists(@NonNull Uri filePath) {
        return new File(filePath.getPath()).exists();
    }

    @Override
    public FileDescriptorWrapper openFD(@NonNull Uri path)
    {
        return new FileDescriptorWrapperImpl(appContext, path);
    }

    @Override
    public long getDirAvailableBytes(@NonNull Uri dir)
    {
        long availableBytes;

        try {
            File file = new File(dir.getPath());
            availableBytes = file.getUsableSpace();

        } catch (Exception e) {
            /* This provides invalid space on some devices */
            StatFs stat = new StatFs(dir.getPath());
            availableBytes = stat.getAvailableBytes();
        }

        return availableBytes;
    }

    @Override
    public long getFileSize(@NonNull Uri filePath) {
        return new File(filePath.getPath()).length();
    }

    @Override
    public void takePermissions(@NonNull Uri path) {
        // None
    }

    @Override
    public String getDirPath(@NonNull Uri dir) {
        return dir.getPath();
    }
}
