package com.karpunets.cinemaposter.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.karpunets.cinemaposter.element.LoginActivity;
import com.karpunets.cinemaposter.R;
import com.karpunets.cinemaposter.manageUsers.Manager;

/**
 * Created by Arthur on 13.09.2016.
 */
public final class DialogSighUp extends DialogFragment {

    private static DialogSighUp instance = null;
    private static Manager manager;
    private LinearLayout view;
    private Context context;
    private EditText nameNewUser;

    public static DialogSighUp newInstance() {
        if (instance == null)
            instance = new DialogSighUp();
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        manager = new Manager(getActivity());
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        manager.close();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        manager = new Manager(context);

        AlertDialog.Builder bilder = new AlertDialog.Builder(context);
        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        view = (LinearLayout) ltInflater.inflate(R.layout.dialog_sigh_up, null, false);
        nameNewUser = (EditText) view.findViewById(R.id.nameNewUser);

        bilder.setView(view);
        bilder.setPositiveButton(R.string.btn_continuous, listener);
        bilder.setNegativeButton(R.string.btn_cancel, listener);
        return bilder.create();
    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (which == AlertDialog.BUTTON_POSITIVE) {
                String currentName = nameNewUser.getText().toString();
                if (currentName.equals("")) {
                    Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!manager.exist(currentName)) {
                    manager.addUser(currentName);
                    ((LoginActivity)getActivity()).goToContent();
                    Toast.makeText(context, "Welcome " + nameNewUser.getText(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "This name already exists", Toast.LENGTH_SHORT).show();
            }
        }


    };

}
