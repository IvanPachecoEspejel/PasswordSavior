package com.ivanpacheco.passwordsavior.activities.FragmentPasswords;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ivanpacheco.passwordsavior.R;
import com.ivanpacheco.passwordsavior.activities.MasterPassword;

import java.util.List;

/**
 * Created by ivanpacheco on 14/02/18.
 *
 */
public class PasswordPatternFragment extends Fragment implements PasswordFragment{

    private PatternLockView pattLockPass;

    public PasswordPatternFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View container = inflater.inflate(R.layout.fragment_password_pattern, viewGroup, false);

        pattLockPass = container.findViewById(R.id.pattLockPass);
        pattLockPass.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                onCompletePattern(pattern);
            }
            @Override
            public void onCleared() {}
        });

        return container;
    }

    @Override
    public void onWrongPassword() {
        pattLockPass.clearPattern();
        Toast.makeText(getActivity(), R.string.pattPasssInc, Toast.LENGTH_SHORT).show();
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        pattLockPass.startAnimation(shake);
    }

    private void onCompletePattern(List<PatternLockView.Dot> pattern) {
        MasterPassword mp = (MasterPassword)getActivity();
        mp.submitPassword(this, PatternLockUtils.patternToString(pattLockPass, pattern));
    }

}