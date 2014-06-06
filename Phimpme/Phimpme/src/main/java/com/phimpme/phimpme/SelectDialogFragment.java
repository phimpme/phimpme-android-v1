package com.phimpme.phimpme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class SelectDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] array = {"Capture a new photo", "Choose from library", "Select from map"};
        builder.setTitle("Upload a Photo")
                .setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                // Capture a new photo
                                startActivity(new Intent(getActivity(), CaptureActivity.class));
                                break;
                            case 1:
                                // Choose from library
                                startActivity(new Intent(getActivity(), ChooseFromLibraryActivity.class));
                                break;
                            case 2:
                                // Select from map
                                Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_LONG);
                                startActivity(new Intent(getActivity(), MapActivity.class));
                                break;
                        }
                    }
                });
        return builder.create();
    }

}