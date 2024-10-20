/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.ui.errorreport;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.databinding.DialogErrorBinding;
import com.easyplexdemoapp.ui.downloadmanager.ui.BaseAlertDialog;

public class ErrorReportDialog extends BaseAlertDialog
{
    @SuppressWarnings("unused")
    private static final String TAG = ErrorReportDialog.class.getSimpleName();

    protected static final String TAG_DETAIL_ERROR = "detail_error";

    /* In the absence of any parameter need set 0 or null */

    public static ErrorReportDialog newInstance(String title, String message,
                                                String detailError)
    {
        ErrorReportDialog frag = new ErrorReportDialog();

        Bundle args = new Bundle();
        args.putString(TAG_TITLE, title);
        args.putString(TAG_MESSAGE, message);
        args.putString(TAG_DETAIL_ERROR, detailError);

        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        Bundle args = getArguments();
        String title = args.getString(TAG_TITLE);
        String message = args.getString(TAG_MESSAGE);
        String positiveText = getString(R.string.report);
        String negativeText = getString(R.string.cancel);
        String detailError = args.getString(TAG_DETAIL_ERROR);

        LayoutInflater i = LayoutInflater.from(getActivity());
        DialogErrorBinding binding = DataBindingUtil.inflate(i, R.layout.dialog_error, null, false);
        binding.setDetailError(detailError);

        initLayoutView(binding);

        return buildDialog(title, message, binding.getRoot(),
                positiveText, negativeText, null, false);
    }

    private void initLayoutView(DialogErrorBinding binding)
    {
        binding.expansionHeader.setOnClickListener((View view) -> {
            binding.expandableLayout.toggle();
            binding.expansionHeader.toggleExpand();
        });
    }
}
