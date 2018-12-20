package com.ivanpacheco.passwordsavior.activities.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ivanpacheco.EncryotorLib.Encryptor;
import com.ivanpacheco.LoggerLib.Logger;
import com.ivanpacheco.passwordsavior.R;
import com.ivanpacheco.passwordsavior.greenDAO.Entities.Credential;

import java.util.List;

/**
 * Created by ivanpacheco on 21/02/18.
 *
 */

public class ListAdapterPassword extends ArrayAdapter<Credential> {

    private List<Credential> credentials;
    private final Encryptor encryptor;
    private final Context context;
    private static final Logger LOG = new Logger(ListAdapterPassword.class.getName());


    public ListAdapterPassword(@NonNull Context context, List<Credential> credentials, Encryptor encryptor) {
            super(context, R.layout.activity_passwords, credentials);
            this.credentials = credentials;
            this.encryptor = encryptor;
            this.context = context;
    }


    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Credential c = credentials.get(position);
        View rowView;

        if(inflater == null){
            LOG.e("Error in inflater when put the view in the list view");
            return new View(context);
        }

        if(c.getType() == Credential.TYPE_DETAIL){

            rowView = inflater.inflate(R.layout.list_item_password_dtl, parent, false);

            TextView txtvUsername = rowView.findViewById(R.id.txtvUsername);
            TextView txtvPassword = rowView.findViewById(R.id.txtvPassword);

            txtvUsername.setText(encryptor.decrypt(c.getUserName()));
            txtvPassword.setText(encryptor.decrypt(c.getPassWord()));

        }else{

            rowView = inflater.inflate(R.layout.list_item_password, parent, false);

            TextView txtvAppName = rowView.findViewById(R.id.txtvAppName);
            TextView txtvFirstLatter = rowView.findViewById(R.id.txtvFirstLetter);

            String appName = c.getAppName();

            txtvAppName.setText(appName);
            txtvFirstLatter.setText(appName != null && appName.length() > 0 ?(c.getAppName().substring(0,1).toUpperCase()):" - ");
        }
        return rowView;
    }
}
