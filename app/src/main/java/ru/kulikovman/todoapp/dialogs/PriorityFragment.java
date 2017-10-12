package ru.kulikovman.todoapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import ru.kulikovman.todoapp.R;

public class PriorityFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String emergency = getString(R.string.priority_0);
        final String high = getString(R.string.priority_1);
        final String common = getString(R.string.priority_2);
        final String low = getString(R.string.priority_3);
        final String lowest = getString(R.string.priority_4);

        final String priority[] = {emergency, high, common, low, lowest};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.priority_title)
                .setItems(priority, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView priorityField = (TextView) getActivity().findViewById(R.id.priority_field);

                        switch (which) {
                            case 0:
                                priorityField.setText(emergency);
                                break;
                            case 1:
                                priorityField.setText(high);
                                break;
                            case 2:
                                priorityField.setText(common);
                                break;
                            case 3:
                                priorityField.setText(low);
                                break;
                            case 4:
                                priorityField.setText(lowest);
                                break;
                        }
                    }
                });

        return builder.create();
    }
}
