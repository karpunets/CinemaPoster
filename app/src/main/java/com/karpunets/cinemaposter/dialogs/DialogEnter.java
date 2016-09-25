package com.karpunets.cinemaposter.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.karpunets.cinemaposter.element.LoginActivity;
import com.karpunets.cinemaposter.R;
import com.karpunets.cinemaposter.manageUsers.DBUsers;
import com.karpunets.cinemaposter.manageUsers.Manager;

/**
 * Created by Arthur on 13.09.2016.
 */
public final class DialogEnter extends DialogFragment implements View.OnClickListener {

    private static DialogEnter instance = null;
    private static Manager manager;
    private Context context;

    public static DialogEnter newInstance() {
        if (instance == null)
            instance = new DialogEnter();
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        manager = new Manager(context);

        LinearLayout listUser = createListUser(manager.getUsers());

        AlertDialog.Builder bilder = new AlertDialog.Builder(context);
        bilder.setView(listUser);
        bilder.setPositiveButton(R.string.btn_cancel, null);
        return bilder.create();
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

    private LinearLayout createListUser(Cursor users) {

        LayoutInflater ltInflater = getActivity().getLayoutInflater();

        LinearLayout mainLayout = (LinearLayout) ltInflater.inflate(R.layout.dialog_enter, null, false);
        LinearLayout list = (LinearLayout) mainLayout.findViewById(R.id.linLayout);
        String name;
        int id;
        if (users != null && users.getCount() != 0) {
            users.moveToFirst();
            do {
                name = users.getString(users.getColumnIndex(DBUsers.COLUMN_USER_NAME));
                id = users.getInt(users.getColumnIndex(DBUsers.COLUMN_ID));

                View item = ltInflater.inflate(R.layout.item_user, list, false);
                item.setId(id);

                Button userNameBtn = (Button) item.findViewById(R.id.userName);
                userNameBtn.setText(name);
                userNameBtn.setTag(id);
                userNameBtn.setOnClickListener(this);

                ImageButton deleteUserBtn = (ImageButton) item.findViewById(R.id.deleteUser);
                deleteUserBtn.setTag(id);
                deleteUserBtn.setOnClickListener(this);

                item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                list.addView(item);
            } while (users.moveToNext());
        }

        return mainLayout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteUser:
                manager.deleteUser((Integer) view.getTag());
                dismiss();
                show(getFragmentManager(), "Enter");
                Toast.makeText(context, "User has been deleted", Toast.LENGTH_SHORT).show();
                break;
            case R.id.userName:
                manager.setCurrentUser((Integer) view.getTag(), getActivity());
                ((LoginActivity)getActivity()).goToContent();
                Toast.makeText(context, "Welcome " + ((Button) view).getText(), Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }
}
