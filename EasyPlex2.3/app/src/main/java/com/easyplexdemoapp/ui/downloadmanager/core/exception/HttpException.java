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

public class HttpException extends Exception
{
    private final int responseCode;

    public HttpException(String message)
    {
        this(message, 0);
    }

    public HttpException(String message, int responseCode)
    {
        super(message);
        this.responseCode = responseCode;
    }

    public HttpException(String message, int responseCode, Exception e)
    {
        this(message, responseCode);
        initCause(e);
    }

    public int getResponseCode()
    {
        return responseCode;
    }
}