/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.ui.main.drawer;

import java.util.ArrayList;
import java.util.List;

/*
 * Expandable group of clickable items (radio button-like behavior).
 */

public class DrawerGroup
{
    public static final long DEFAULT_SELECTED_ID = 0;

    public long id;
    public String name;
    public List<DrawerGroupItem> items = new ArrayList<>();
    private long selectedItemId = DEFAULT_SELECTED_ID;
    private final boolean defaultExpandState;

    public DrawerGroup(long id, String name, boolean defaultExpandState)
    {
        this.id = id;
        this.name = name;
        this.defaultExpandState = defaultExpandState;
    }

    public void selectItem(long itemId)
    {
        selectedItemId = itemId;
    }

    public boolean isItemSelected(long itemId)
    {
        return itemId == selectedItemId;
    }

    public long getSelectedItemId()
    {
        return selectedItemId;
    }

    /*
     * true - expanded; false - collapsed
     */

    public boolean getDefaultExpandState()
    {
        return defaultExpandState;
    }
}
