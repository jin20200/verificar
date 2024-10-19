/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.ui.filemanager;

/*
 * The interface with basic functions for a file object.
 */

import androidx.annotation.NonNull;

import java.io.Serializable;

public interface FileNode<F> extends Comparable<F>
{
    class Type implements Serializable
    {
        public static int DIR = 0;
        public static int FILE = 1;
    }

    String getName();

    void setName(String name);

    int getType();

    void setType(int type);

    @Override
    int compareTo(@NonNull F another);
}
