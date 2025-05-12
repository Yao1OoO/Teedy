package com.sismics.docs.core.dao.jpa;

import com.sismics.docs.BaseTransactionalTest;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.FileDao;
import com.sismics.docs.core.dao.VocabularyDao;
import com.sismics.docs.core.model.jpa.Vocabulary;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.model.jpa.File;
import com.sismics.docs.core.util.TransactionUtil;
import com.sismics.docs.core.util.authentication.InternalAuthenticationHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the persistance layer.
 * 
 * @author jtremeaux
 */
public class TestJpa extends BaseTransactionalTest {
    @Test
    public void testJpa() throws Exception {
        // Create a user
        UserDao userDao = new UserDao();
        User user = createUser("testJpa");

        TransactionUtil.commit();

        // Search a user by his ID
        user = userDao.getById(user.getId());
        Assert.assertNotNull(user);
        Assert.assertEquals("toto@docs.com", user.getEmail());

        // Authenticate using the database
        Assert.assertNotNull(new InternalAuthenticationHandler().authenticate("testJpa", "12345678"));

        // Delete the created user
        userDao.delete("testJpa", user.getId());
        TransactionUtil.commit();

        FileDao fileDao = new FileDao();
        File file = createFile(user, 1000L);

        TransactionUtil.commit();

        file = fileDao.findAll(0, 10).get(0);

        Assert.assertNotNull(file);

        VocabularyDao vocabularyDao = new VocabularyDao();

        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setName("test");

        vocabularyDao.create(vocabulary);

        vocabularyDao.getByName("test");
    }
}
