/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core;

import android.text.InputFilter;
import android.text.Spanned;

/*
 * Filtering numbers, which are outside the specified range.
 */

public class InputFilterMinMax implements InputFilter
{
    private final int min;
    private final int max;

    public InputFilterMinMax(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max)
    {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3)
    {
        if (charSequence.length() != 0 && charSequence.charAt(0) == '-')
            return null;

        try {
            int input = Integer.parseInt(spanned.toString() + charSequence);
            if (inRange(min, max, input))
                return null;

        } catch (NumberFormatException e) {
            /* Ignore */
        }

        return "";
    }

    private boolean inRange(int a, int b, int c)
    {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
