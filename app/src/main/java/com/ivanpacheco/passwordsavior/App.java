package com.ivanpacheco.passwordsavior;

import android.app.Application;
import android.content.res.Resources;

import com.ivanpacheco.passwordsavior.greenDAO.DAOs.DaoMaster;
import com.ivanpacheco.passwordsavior.greenDAO.DAOs.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ivanpacheco on 21/02/18.
 *
 */

public class App extends Application {

    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        Resources res = this.getResources();

        //Setup green dao
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(
                this,
                ENCRYPTED ? res.getString(R.string.db_name)+"-encrypted" : res.getString(R.string.db_name));
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
