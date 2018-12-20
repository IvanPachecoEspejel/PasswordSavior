package com.ivanpacheco.passwordsavior.greenDAO.DAOs;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.ivanpacheco.passwordsavior.greenDAO.Entities.Credential;
import com.ivanpacheco.passwordsavior.greenDAO.DAOs.CredentialDao;

import java.util.Date;

public class CredentialTest extends AbstractDaoTestLongPk<CredentialDao, Credential> {

    public CredentialTest() {
        super(CredentialDao.class);
    }

    @Override
    protected Credential createEntity(Long key) {
        Credential entity = new Credential();
        entity.setId(key);
        entity.setAppName("");
        entity.setUserName("");
        entity.setPassWord("");
        entity.setCreationDate(new Date().getTime());
        entity.setLastUpdate(new Date().getTime());
        return entity;
    }

}
