package com.ivanpacheco.passwordsavior.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.ivanpacheco.EncryotorLib.Encryptor;
import com.ivanpacheco.LoggerLib.Logger;
import com.ivanpacheco.UtilLib.UtilExtras;
import com.ivanpacheco.passwordsavior.App;
import com.ivanpacheco.passwordsavior.R;
import com.ivanpacheco.passwordsavior.activities.FragmentPasswords.PasswordFragment;
import com.ivanpacheco.passwordsavior.activities.FragmentPasswords.PasswordPatternFragment;
import com.ivanpacheco.passwordsavior.activities.FragmentPasswords.PasswordPinFragment;
import com.ivanpacheco.passwordsavior.activities.FragmentPasswords.PasswordTextFragment;
import com.ivanpacheco.passwordsavior.greenDAO.DAOs.CredentialDao;
import com.ivanpacheco.passwordsavior.greenDAO.Entities.Credential;

import java.io.IOException;
import java.util.Date;

/**
 * Created by ivanpacheco on 24/01/18.
 *
 */

public class MasterPassword extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener {

    private Handler mHandler = new Handler();
    private int passwordType;
    private final static Logger LOG = new Logger(MasterPassword.class.getName());

    public MasterPassword(){
        passwordType = 0;
        mHandler = new Handler();
    }

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_master_password);

        Toolbar myToolbar = findViewById(R.id.tbMasPass);
        setSupportActionBar(myToolbar);

        Fragment fragment;

        Resources res = this.getResources();

        if(!UtilExtras.getExtraBoolean(this, res.getString(R.string.isFirstTime))){
            String typeFrag = UtilExtras.getExtraString(this, res.getString(R.string.isFirstTimeTypePass));
            switch (typeFrag){
                case "0":
                    fragment = new PasswordTextFragment();
                    break;
                case "1":
                    fragment = new PasswordPatternFragment();
                    break;
                default:
                    fragment = new PasswordPinFragment();
            }
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.mpFlContainer, fragment, typeFrag)
                    .commit();
        }else if(saveInstanceState == null){
            fragment = new PasswordTextFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.mpFlContainer, fragment, "0")
                    .commit();
        }else{
            passwordType = getFragmentManager().getBackStackEntryCount();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        Resources res = this.getResources();

        // Add either a "photo" or "finish" button to the action bar, depending on which page
        // is currently selected.
        if(UtilExtras.getExtraBoolean(this, res.getString(R.string.isFirstTime))){
            int typePassInfo;
            switch (passwordType){
                case 0:
                    typePassInfo = R.string.lblTxtTitle;
                    break;
                case 1:
                    typePassInfo = R.string.lblPatterTitle;
                    break;
                default:
                    typePassInfo = R.string.lblPinTitle;
                    break;
            }
            MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,typePassInfo);
            item.setIcon(R.drawable.ic_compare_arrows_24dp);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_flip:
                flipCard();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void flipCard() {
        if (passwordType == 2) {
            while(passwordType != 0){
                getFragmentManager().popBackStack();
                passwordType--;
            }
            return;
        }
        Fragment fragment;

        passwordType++;

        switch (passwordType){
            case 0:
                fragment = new PasswordTextFragment();
                break;
            case 1:
                fragment = new PasswordPatternFragment();
                break;
            default:
                fragment = new PasswordPinFragment();
                break;
        }

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.mpFlContainer, fragment, String.valueOf(passwordType))
                .addToBackStack(null)
                .commit();

        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        passwordType = getFragmentManager().getBackStackEntryCount();
        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_UP){

            Fragment frg = getFragmentManager().findFragmentByTag(String.valueOf(passwordType));
            PasswordTextFragment ptf;

            if(frg instanceof PasswordTextFragment){

                ptf = (PasswordTextFragment)frg;
                ptf.dispatchKeyEvent(event);
            }
        }

        return super.dispatchKeyEvent(event);
    }

    public void submitPassword(PasswordFragment fragment, String password){
        Resources res = this.getResources();
        Boolean isLogin;
        Encryptor encryptor = new Encryptor(password);

        CredentialDao credentialDao = ((App)getApplication()).getDaoSession().getCredentialDao();

        if(UtilExtras.getExtraBoolean(this, res.getString(R.string.isFirstTime))){
            try {

                Credential credential = new Credential(null,
                        res.getString(R.string.unique_app_name),
                        String.valueOf(getFragmentManager().getBackStackEntryCount()),
                        encryptor.encrypt(res.getString(R.string.unique_app_name)),
                        new Date().getTime(),
                        new Date().getTime());

                credentialDao.insert(credential);

                isLogin = true;
            }
            finally {
                credentialDao.detachAll();
            }
        }else{
            String encryptApp = UtilExtras.getExtraString(this, res.getString(R.string.isFirstTimePass));
            isLogin = encryptor.decrypt(encryptApp).equals(res.getString(R.string.unique_app_name));
        }

        if(isLogin){
            try {
                Intent i = new Intent(MasterPassword.this, Passwords.class);
                i.putExtra(res.getString(R.string.encryptor), UtilExtras.serializeObject(encryptor));
                startActivity(i);
                finish();
            } catch (IOException e) {
                LOG.e("Error putting extra encryptor", e);
            }
        }else{
            fragment.onWrongPassword();
        }
    }

}
