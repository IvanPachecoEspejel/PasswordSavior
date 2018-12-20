package com.ivanpacheco.DBLib.Entities;

import android.database.Cursor;

/**
 * Created by ivanpacheco on 20/02/18.
 */

public interface Entity {
    void retrieveData(Cursor c);
}
