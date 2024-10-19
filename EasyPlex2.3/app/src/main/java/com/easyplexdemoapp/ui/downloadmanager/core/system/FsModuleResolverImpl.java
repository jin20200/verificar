/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.system;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;

class FsModuleResolverImpl implements FsModuleResolver
{
    private final Context appContext;
    private final SafFsModule safModule;
    private final DefaultFsModule defaultModule;

    public FsModuleResolverImpl(@NonNull Context appContext)
    {
        this.appContext = appContext;
        this.safModule = new SafFsModule(appContext);
        this.defaultModule = new DefaultFsModule(appContext);
    }

    @Override
    public FsModule resolveFsByUri(@NonNull Uri uri)
    {
        if (Utils.isSafPath(appContext, uri))
            return safModule;
        else if (Utils.isFileSystemPath(uri))
            return defaultModule;
        else
            throw new IllegalArgumentException("Cannot resolve file system for the given uri: " + uri);
    }
}
