package com.ivanpacheco.passwordsavior.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;

import com.ivanpacheco.passwordsavior.App;
import com.ivanpacheco.passwordsavior.R;
import com.ivanpacheco.passwordsavior.greenDAO.DAOs.CredentialDao;
import com.ivanpacheco.passwordsavior.greenDAO.Entities.Credential;

import java.util.List;

/**
 * Created by ivanpacheco on 24/01/18.
 * Activity validation
 */

public class Logo extends Activity{

    private static final String LOG_TAG = Logo.class.getName();
    private Resources res;
    private CredentialDao credentialDao;

    public Logo(){

    }

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_logo);
        final ProgressBar pbLoad = findViewById(R.id.pbLoad);

        res = this.getResources();
        credentialDao = ((App)getApplication()).getDaoSession().getCredentialDao();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Intent i = new Intent(Logo.this, MasterPassword.class);
                    boolean isFirstTime = true;
                    Credential appPasswordCursor = readAppPassword();

                    if(appPasswordCursor != null){
                        isFirstTime = false;
                        i.putExtra(res.getString(R.string.isFirstTimePass),appPasswordCursor.getPassWord());
                        i.putExtra(res.getString(R.string.isFirstTimeTypePass), appPasswordCursor.getUserName());
                    }

                    i.putExtra(res.getString(R.string.isFirstTime), isFirstTime);
                    startActivity(i);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }finally {
                    finish();
                    if(credentialDao != null)
                        credentialDao.detachAll();
                    pbLoad.onCancelPendingInputEvents();
                }
            }
        });

        thread.start();
    }

    private Credential readAppPassword() throws Exception{

        List<Credential> appPasscord =
                credentialDao.queryBuilder()
                        .where(CredentialDao.Properties.AppName.eq(res.getString(R.string.unique_app_name)))
                        .list();



        if(appPasscord == null || appPasscord.isEmpty())
            return null;
        return appPasscord.get(0);
    }

}
