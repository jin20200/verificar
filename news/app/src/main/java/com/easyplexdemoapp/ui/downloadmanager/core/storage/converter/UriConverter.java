/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.storage.converter;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class UriConverter
{
    @TypeConverter
    public static Uri toUri(@NonNull String uriStr)
    {
        return Uri.parse(uriStr);
    }

    @TypeConverter
    public static String fromUri(@NonNull Uri uri)
    {
        return uri.toString();
    }
}
