/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.system;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.IOException;

/*
 * A platform dependent interface for system calls.
 */

interface SysCall
{
    void lseek(@NonNull FileDescriptor fd, long offset) throws IOException, UnsupportedOperationException;

    void fallocate(@NonNull FileDescriptor fd, long length) throws IOException;

    long availableBytes(@NonNull FileDescriptor fd) throws IOException;
}
