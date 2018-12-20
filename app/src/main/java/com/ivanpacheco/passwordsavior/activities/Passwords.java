package com.ivanpacheco.passwordsavior.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ivanpacheco.EncryotorLib.Encryptor;
import com.ivanpacheco.LoggerLib.Logger;
import com.ivanpacheco.UtilLib.UtilExtras;
import com.ivanpacheco.passwordsavior.App;
import com.ivanpacheco.passwordsavior.R;
import com.ivanpacheco.passwordsavior.activities.Adapters.ListAdapterPassword;
import com.ivanpacheco.passwordsavior.activities.Views.TouchableListView;
import com.ivanpacheco.passwordsavior.greenDAO.DAOs.CredentialDao;
import com.ivanpacheco.passwordsavior.greenDAO.Entities.Credential;

import java.io.IOException;
import java.util.List;

/**
 * Created by ivanpacheco on 24/01/18.
 *
 */

public class Passwords extends AppCompatActivity {

    private List<Credential> lstCredentials;
    private ConstraintLayout clNewCredential;
    private ListAdapterPassword listAdapterPassword;

    private boolean flagShowNewCredentialPanel;

    private static final Logger LOG = new Logger(Passwords.class.getName());

    public Passwords(){
        flagShowNewCredentialPanel = true;
    }

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_passwords);

        clNewCredential = findViewById(R.id.clNewPass);

        Toolbar myToolbar = findViewById(R.id.tbPasswords);
        setSupportActionBar(myToolbar);

        handleIntents(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntents(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_list, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayoutCompat ll = findViewById (R.id.llPasswords);

        switch (item.getItemId()) {
            case R.id.iAdd:
                if(flagShowNewCredentialPanel){
                    flagShowNewCredentialPanel = false;
                    clNewCredential.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_remove_white_24dp);
                }else{
                    flagShowNewCredentialPanel = true;
                    clNewCredential.setVisibility(View.GONE);
                    item.setIcon(R.drawable.ic_add_white_24dp);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void handleIntents(Intent intent){
        if(intent == null)
            return;

        String action = intent.getAction();
        if(action == null){
            handleNormalIntent(intent);
            return;
        }

        switch (action){
            case Intent.ACTION_SEARCH:
                handleSearchIntent(intent);
                break;
            default:
        }
    }

    private void handleSearchIntent(Intent intent){
        LOG.i(intent.getStringExtra(SearchManager.QUERY));
    }

    private void handleNormalIntent(Intent intent){
        byte[] objArr = intent.getByteArrayExtra(getResources().getString(R.string.encryptor));
        if(objArr == null)
            return;
        try {
            final Encryptor encryptor = (Encryptor) UtilExtras.deserializeObject(objArr);

            final EditText etxtUsername = findViewById(R.id.etxtUserName);
            final EditText etxtPassword = findViewById(R.id.etxtPass);
            final EditText etxtAppName = findViewById(R.id.etxtAppName);

            final TouchableListView lstvPasswords = findViewById(R.id.lstvPasswords);

            Button btnNewCredential = findViewById(R.id.btnOk);

            CredentialDao credentialDao = ((App)getApplication()).getDaoSession().getCredentialDao();
            lstCredentials = credentialDao.loadAll();

            listAdapterPassword = new ListAdapterPassword(this, lstCredentials, encryptor);

            lstvPasswords.setAdapter(listAdapterPassword);

            lstvPasswords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemClickListner(position);
                }
            });

            btnNewCredential.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickBtnNewCredential(encryptor, etxtAppName.getText().toString(),
                            encryptor.encrypt(etxtUsername.getText().toString()),
                            encryptor.encrypt(etxtPassword.getText().toString()));
                }
            });

//            lstvPasswords.setOnTouchListener(new OnSwipeTouchListener(getApplication(), lstvPasswords) {
//                @Override
//                public void onSwipeRight(int pos) {
//
//                }
//
//                @Override
//                public void onSwipeLeft(int pos) {
//
//                }
//
//                @Override
//                public void onSwipeDown(int pos) {
//                    clNewCredential.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onSwipeTop(int pos) {
//                    clNewCredential.setVisibility(View.GONE);
//                }
//            });

            credentialDao.detachAll();
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
            LOG.e("Error getting encryptor: "+e.getMessage(),e);
        }
    }

    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /*---------------------------------/
                Listeners
    /---------------------------------*/

    /********** List ***********/
    private void onItemClickLstvPasswords(int position) {
        Credential c = lstCredentials.get(position);

        switch (c.getType()){
            case Credential.TYPE_NORMAL:
                if(lstCredentials.size() > position+1 && lstCredentials.get(position+1).getType() == Credential.TYPE_DETAIL) {
                    lstCredentials.remove(position + 1);
                }else{
                    lstCredentials.add(position + 1, new Credential(c, Credential.TYPE_DETAIL));
                }
                break;
            case Credential.TYPE_DETAIL:
                lstCredentials.remove(position);
                break;
        }
    }

    public void onItemClickListner(int position) {
        onItemClickLstvPasswords(position);
        listAdapterPassword.notifyDataSetChanged();
    }

    public void onClickBtnNewCredential(Encryptor encryptor, String appName, String userName, String password) {
        onClickNewCredential(appName,
                encryptor.encrypt(userName),
                encryptor.encrypt(password));
        listAdapterPassword.notifyDataSetChanged();
    }

    private void onClickNewCredential(String appName, String userName, String password){
        final CredentialDao credentialDao = ((App)getApplicationContext()).getDaoSession().getCredentialDao();

        credentialDao.insert(new Credential(appName, userName, password));

        lstCredentials = credentialDao.loadAll();
    }

}
