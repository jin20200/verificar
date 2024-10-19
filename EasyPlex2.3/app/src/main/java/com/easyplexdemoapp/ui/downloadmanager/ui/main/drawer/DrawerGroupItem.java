/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.ui.main.drawer;

/*
 * Clickable item in group.
 */

public class DrawerGroupItem
{
    public long id;
    public int iconResId;
    public String name;

    public DrawerGroupItem(long id, int iconResId, String name)
    {
        this.id = id;
        this.iconResId = iconResId;
        this.name = name;
    }
}
