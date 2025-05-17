package com.sismics.docs.core.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.common.base.Strings;
import com.sismics.docs.core.constant.Constants;
import com.sismics.docs.core.model.jpa.Register;
import com.sismics.docs.core.util.EncryptionUtil;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class RegisterDao {
    private static final Logger log = LoggerFactory.getLogger(RegisterDao.class);

    public String create(Register register) throws Exception{
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from User u where u.username = :username and u.deleteDate is null");
        q.setParameter("username", register.getUsername());
        List<?> l = q.getResultList();
        if (l.size() > 0) {
            throw new Exception("AlreadyExistingUsername");
        }
        q = em.createQuery("select r from Register r where r.username = :username");
        q.setParameter("username", register.getUsername());
        l = q.getResultList();
        if (l.size() > 0) {
            throw new Exception("AlreadyExistingUsername");
        }

        register.setCreateDate(new Date());
        register.setPassword(hashPassword(register.getPassword()));
        register.setPrivateKey(EncryptionUtil.generatePrivateKey());
        register.setStorageCurrent(0L);
        em.persist(register);

        // Create audit log
        //AuditLogUtil.create(, AuditLogType.CREATE, register.getId());

        return register.getId();
    }

    public List<Register> getAll(){
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Register> q = em.createQuery("select r from Register r", Register.class);
        return q.getResultList();
    }

    public Register getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        try {
            return em.find(Register.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    public Register updateState(String id, String state) throws Exception {
        Register r = getById(id);
        if (r == null) {
            throw new Exception("NotFound");
        }
        r.setState(state);
        return r;
    }

    private String hashPassword(String password) {
        int bcryptWork = Constants.DEFAULT_BCRYPT_WORK;
        String envBcryptWork = System.getenv(Constants.BCRYPT_WORK_ENV);
        if (!Strings.isNullOrEmpty(envBcryptWork)) {
            try {
                int envBcryptWorkInt = Integer.parseInt(envBcryptWork);
                if (envBcryptWorkInt >= 4 && envBcryptWorkInt <= 31) {
                    bcryptWork = envBcryptWorkInt;
                } else {
                    log.warn(Constants.BCRYPT_WORK_ENV + " needs to be in range 4...31. Falling back to " + Constants.DEFAULT_BCRYPT_WORK + ".");
                }
            } catch (NumberFormatException e) {
                log.warn(Constants.BCRYPT_WORK_ENV + " needs to be a number in range 4...31. Falling back to " + Constants.DEFAULT_BCRYPT_WORK + ".");
            }
        }
        return BCrypt.withDefaults().hashToString(bcryptWork, password.toCharArray());
    }
}
