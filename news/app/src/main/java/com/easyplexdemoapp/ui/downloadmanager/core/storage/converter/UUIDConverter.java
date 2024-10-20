/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.storage.converter;

import androidx.room.TypeConverter;

import java.util.UUID;

public class UUIDConverter
{
    @TypeConverter
    public static UUID toUUID(String uuidStr)
    {
        if (uuidStr == null)
            return null;

        UUID uuid = null;
        try {
            uuid = UUID.fromString(uuidStr);

        } catch (IllegalArgumentException e) {
            return null;
        }

        return uuid;
    }

    @TypeConverter
    public static String fromUUID(UUID uuid)
    {
        return uuid == null ? null : uuid.toString();
    }
}
