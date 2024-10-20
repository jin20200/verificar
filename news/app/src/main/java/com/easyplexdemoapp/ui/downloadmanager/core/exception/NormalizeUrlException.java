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

package com.easyplexdemoapp.ui.downloadmanager.core.exception;

public class NormalizeUrlException extends Exception
{
    public NormalizeUrlException(String message, Exception e)
    {
        super(message);
        initCause(e);
    }

    public NormalizeUrlException(String message)
    {
        super(message);
    }
}
