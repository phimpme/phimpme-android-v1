package com.phimpme.phimpme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

public class SelectDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // The title list contains text labels of each function
        // The action list contains associated behaviors of each menu item
        final ArrayList<String> title = new ArrayList<String>();
        final ArrayList<Behavior> action = new ArrayList<Behavior>();
        if (Configuration.ENABLE_PHOTO_CAPTURING) {
            title.add("Capture a new photo");
            action.add(Behavior.CAPTURE);
        }
        if (Configuration.ENABLE_CHOOSE_FROM_LIBRARY) {
            title.add("Choose from library");
            action.add(Behavior.CHOOSE);
        }
        if (Configuration.ENABLE_MAP) {
            title.add("Select from map");
            action.add(Behavior.MAP);
        }
        // If only one function is enabled, just perform it.
        if (action.size() == 1) {
            perform(action.get(0));
        }

	    //noinspection ToArrayCallWithZeroLengthArrayArgument
	    builder.setTitle("Upload a Photo")
                .setItems(title.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        perform(action.get(which));
                    }
                });

        return builder.create();
    }

    private void perform(Behavior behavior) {
        Intent intent = new Intent();
        switch (behavior) {
            case CAPTURE:
                intent.setClass(getActivity(), CaptureActivity.class);
                break;
            case CHOOSE:
                intent.setClass(getActivity(), ChooseFromLibraryActivity.class);
                break;
            case MAP:
                if (Configuration.ENABLE_GOOGLEMAP) {
                    intent.setClass(getActivity(), MapActivity.class);
                } else {
                    intent.setClass(getActivity(), OpenStreetMapActivity.class);
                }
                break;
        }
        startActivity(intent);
        dismiss();
    }

    private enum Behavior {CAPTURE, CHOOSE, MAP}
}