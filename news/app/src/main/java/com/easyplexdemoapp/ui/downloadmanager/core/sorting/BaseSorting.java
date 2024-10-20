/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.sorting;

public class BaseSorting
{
    public enum Direction
    {
        ASC, DESC;

        public static Direction fromValue(String value)
        {
            for (Direction direction : Direction.class.getEnumConstants()) {
                if (direction.toString().equalsIgnoreCase(value))
                    return direction;
            }

            return Direction.ASC;
        }
    }

    public interface SortingColumnsInterface<F>
    {
        int compare(F item1, F item2, Direction direction);

        String name();
    }

    private final Direction direction;
    private final String columnName;

    public BaseSorting(String columnName, Direction direction)
    {
        this.direction = direction;
        this.columnName = columnName;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public String getColumnName()
    {
        return columnName;
    }

    @Override
    public String toString()
    {
        return "BaseSorting{" +
                "direction=" + direction +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
