package com.karpunets.cinemaposter.element;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.karpunets.cinemaposter.R;
import com.karpunets.cinemaposter.dialogs.DialogEnter;
import com.karpunets.cinemaposter.dialogs.DialogSighUp;

public class LoginActivity extends Activity implements View.OnClickListener {

    public static final String MY_TAB = "karpo_tab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnEnter).setOnClickListener(this);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DialogFragment dialog;
        switch (view.getId()) {
            case R.id.btnEnter:
                dialog = DialogEnter.newInstance();
                dialog.show(getFragmentManager(), "Enter");
                break;
            case R.id.btnSignUp:
                dialog = DialogSighUp.newInstance();
                dialog.show(getFragmentManager(), "Sigh up");
                break;
        }
    }

    public void goToContent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
