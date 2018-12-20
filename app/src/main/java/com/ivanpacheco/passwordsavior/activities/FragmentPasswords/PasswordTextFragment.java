package com.ivanpacheco.passwordsavior.activities.FragmentPasswords;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ivanpacheco.LoginValidatiorLib.LoginValidator;
import com.ivanpacheco.UtilLib.UtilExtras;
import com.ivanpacheco.passwordsavior.R;
import com.ivanpacheco.passwordsavior.activities.MasterPassword;

/**
 * Created by ivanpacheco on 14/02/18.
 *
 */

public class PasswordTextFragment extends Fragment implements PasswordFragment {

    public PasswordTextFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        boolean isFirstTime;


        View container= inflater.inflate(R.layout.fragment_password_text, viewGroup, false);

        TextView txtvPassInsecure;
        Button btnOk;
        AppCompatImageButton btnEye;
        EditText etxtPass;

        btnOk = container.findViewById(R.id.btnOk);
        etxtPass = container.findViewById(R.id.etxtPass);
        txtvPassInsecure = container.findViewById(R.id.txtvPassInsecure);
        btnEye = container.findViewById(R.id.btnEye);

        txtvPassInsecure.setVisibility(View.GONE);

        isFirstTime = UtilExtras.getExtraBoolean(getActivity(), getActivity().getResources().getString(R.string.isFirstTime));

        if(isFirstTime){
            txtvPassInsecure.setVisibility(View.VISIBLE);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnOk();
            }
        });

        etxtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setInsecure(LoginValidator.getInsecurityPassword(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnEye();
            }
        });

        return container;
    }

    private void onClickBtnEye(){
        View view = getView();
        if(view == null)
            return;
        EditText etxtPass = view.findViewById(R.id.etxtPass);
        if(etxtPass == null)
            return;
        if(etxtPass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD))
            etxtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            etxtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etxtPass.setSelection(etxtPass.length());
    }

    private void onClickBtnOk(){
        View view = getView();
        if(view == null)
            return;
        EditText etxtPass = view.findViewById(R.id.etxtPass);
        if(etxtPass == null)
            return;

        MasterPassword mp = (MasterPassword)getActivity();
        mp.submitPassword(this, etxtPass.getText().toString());
    }

    private void setInsecure(int insecure){
        View view = getView();
        if(view == null)
            return;
        TextView txtvPassInsecure = view.findViewById(R.id.txtvPassInsecure);
        if(txtvPassInsecure == null)
            return;
        Resources res = getView().getResources();
        switch (insecure){
            case 0:
                txtvPassInsecure.setBackgroundColor(res.getColor(R.color.green));
                break;
            case 1:
                txtvPassInsecure.setBackgroundColor(res.getColor(R.color.yellow));
                break;
            case 2:
                txtvPassInsecure.setBackgroundColor(res.getColor(R.color.orange));
                break;
            default:
                txtvPassInsecure.setBackgroundColor(res.getColor(R.color.red));
                break;
        }

    }

    public void dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        View view = getView();
        if(view == null)
            return;
        Button btnOk = view.findViewById(R.id.btnOk);
        if(btnOk == null)
            return;
        if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            btnOk.callOnClick();
        }
    }

    @Override
    public void onWrongPassword() {
        View view = getView();
        if(view == null)
            return;
        EditText etxtPass = view.findViewById(R.id.etxtPass);
        if(etxtPass == null)
            return;
        Toast.makeText(getActivity(), R.string.txtPasssInc, Toast.LENGTH_SHORT).show();
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        etxtPass.startAnimation(shake);
        etxtPass.setText("");

    }
}
