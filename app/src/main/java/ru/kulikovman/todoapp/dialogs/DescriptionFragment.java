package ru.kulikovman.todoapp.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import ru.kulikovman.todoapp.R;

public class DescriptionFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Это нужно для привязки к диалогу вью из макета
        LayoutInflater inflater = getActivity().getLayoutInflater();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.description_title)
                .setView(inflater.inflate(R.layout.dialog_description, null))
                .setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText textDescription = (EditText) getActivity().findViewById(R.id.text_description);
                        String temp = textDescription.getText().toString();

                        if (!temp.equals("")) {
                            TextView descriptionState = (TextView) getActivity().findViewById(R.id.description_state);
                            descriptionState.setText(temp);
                        }
                    }
                });

        return builder.create();
    }
}
