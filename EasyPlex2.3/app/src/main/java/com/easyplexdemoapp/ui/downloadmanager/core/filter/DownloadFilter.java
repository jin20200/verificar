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


package com.easyplexdemoapp.ui.downloadmanager.core.filter;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.InfoAndPieces;

import io.reactivex.functions.Predicate;

public interface DownloadFilter extends Predicate<InfoAndPieces> {}
