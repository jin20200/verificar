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

import androidx.annotation.NonNull;

public class SystemFacadeHelper
{
    private static SystemFacade systemFacade;
    private static FileSystemFacade fileSystemFacade;

    public synchronized static SystemFacade getSystemFacade(@NonNull Context appContext)
    {
        if (systemFacade == null)
            systemFacade = new SystemFacadeImpl(appContext);

        return systemFacade;
    }

    public synchronized static FileSystemFacade getFileSystemFacade(@NonNull Context appContext)
    {
        if (fileSystemFacade == null)
            fileSystemFacade = new FileSystemFacadeImpl(new SysCallImpl(),
                    new FsModuleResolverImpl(appContext));

        return fileSystemFacade;
    }
}
