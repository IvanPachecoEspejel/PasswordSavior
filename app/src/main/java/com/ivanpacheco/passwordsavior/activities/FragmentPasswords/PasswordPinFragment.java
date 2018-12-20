package com.ivanpacheco.passwordsavior.activities.FragmentPasswords;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ivanpacheco.passwordsavior.R;
import com.ivanpacheco.passwordsavior.activities.MasterPassword;

import java.util.Arrays;

/**
 * Created by ivanpacheco on 14/02/18.
 *
 */

public class PasswordPinFragment extends Fragment implements View.OnClickListener, PasswordFragment {

    private Button[] btnPins;
    private RadioButton[] rdbtnPass;

    private int countPassword;
    private String password[];

    public PasswordPinFragment() {
        rdbtnPass = new RadioButton[4];
        btnPins = new Button[10];
        countPassword = 0;
        password = new String[4];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        AppCompatImageButton btnBack;

        View container = inflater.inflate(R.layout.fragment_password_pin, viewGroup, false);

        int[] idBtns = {R.id.btnPin0, R.id.btnPin1, R.id.btnPin2, R.id.btnPin3, R.id.btnPin4,
                R.id.btnPin5, R.id.btnPin6, R.id.btnPin7, R.id.btnPin8, R.id.btnPin9};

        for(int i = 0; i< btnPins.length; i++){
            btnPins[i] = container.findViewById(idBtns[i]);
            btnPins[i].setOnClickListener(this);
        }

        btnBack = container.findViewById(R.id.btnPinB);
        btnBack.setOnClickListener(this);

        int[] idrBtsPass = {R.id.rbtnPin1, R.id.rbtnPin2, R.id.rbtnPin3, R.id.rbtnPin4};

        for(int i = 0; i<rdbtnPass.length;i++){
            rdbtnPass[i] = container.findViewById(idrBtsPass[i]);
        }

        return  container;
    }

    @Override
    public void onClick(View v) {

        if(v instanceof AppCompatImageButton && v.getId() == R.id.btnPinB){
            if(countPassword > 0){
                password[countPassword] = null;
                countPassword--;
                rdbtnPass[countPassword].setChecked(false);
            }
        }else if(v instanceof  Button){
            password[countPassword] = ((Button)v).getText().toString();
            rdbtnPass[countPassword].setChecked(true);
            countPassword++;
        }

        if(countPassword>=4){
            MasterPassword mp = (MasterPassword)getActivity();
            mp.submitPassword(this,Arrays.toString(password));
        }
    }

    private void clearPassword(){
        for (RadioButton rdbtnPas : rdbtnPass) {
            rdbtnPas.setChecked(false);
        }
        countPassword = 0;
        password = new String [4];
    }

    @Override
    public void onWrongPassword() {
        clearPassword();
        Toast.makeText(getActivity(), R.string.pinPasssInc, Toast.LENGTH_SHORT).show();
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        for(RadioButton rdb : rdbtnPass){
            rdb.startAnimation(shake);
        }
    }
}
