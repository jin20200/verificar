/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.easyplexdemoapp.R;

/*
 * Adds "Copy" item in share dialog.
 */

public class SendTextToClipboard extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Activity.CLIPBOARD_SERVICE);
            ClipData clip;

            CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);

            clip = ClipData.newPlainText(intent.getType(), text);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(getApplicationContext(),
                    R.string.text_copied_to_clipboard,
                    Toast.LENGTH_SHORT)
                    .show();
        }

        finish();
        overridePendingTransition(0, 0);
    }
}
