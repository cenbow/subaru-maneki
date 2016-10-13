package test.com.subaru.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.LoginRecordDao;
import com.subaru.maneki.model.LoginRecord;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author lee
 * @since 2016-8-15
 */
public class LoginRecordDaoTest extends BaseTestCaseJunit {

    private static final int TOKEN_LENGTH = 10;

    @Resource
    private LoginRecordDao   loginRecordDao;

    private LoginRecord      loginRecord;

    public static String generateRandomToken(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            sb.append(str.charAt(num));
        }

        return sb.toString();
    }

    @Before
    public void Setup() {
        loginRecord = new LoginRecord();

        loginRecord.setIp("127.0.0.1");
        loginRecord.setToken(generateRandomToken(TOKEN_LENGTH));
        loginRecord.setUserId(2);

        loginRecordDao.insert(loginRecord);

    }

    @Test
    public void testSelect() {
        LoginRecord newLoginRecord = loginRecordDao.select(loginRecord.getId());

        Assert.assertTrue(newLoginRecord.getIp().equals(loginRecord.getIp()));
    }

    @Test
    public void testSelectByUserId() {
        List<LoginRecord> loginRecordList = loginRecordDao.selectByUserId(loginRecord.getUserId());
        for (LoginRecord lr : loginRecordList) {
            Assert.assertTrue(lr.getUserId() == loginRecord.getUserId());
        }
    }

    @Test
    public void testSelectByIp() {
        List<LoginRecord> loginRecordList = loginRecordDao.selectByIp(loginRecord.getIp());
        for (LoginRecord lr : loginRecordList) {
            Assert.assertTrue(lr.getIp().equals(loginRecord.getIp()));
        }
    }

    @Test
    public void testSelectByToken() {
        LoginRecord newLoginRecord = loginRecordDao.selectByToken(loginRecord.getToken());

        Assert.assertTrue(newLoginRecord.getIp().equals(loginRecord.getIp()));
        Assert.assertTrue(newLoginRecord.getToken().equals(loginRecord.getToken()));
    }

    /**
     * 测试三种情况
     * 1、当gmtExpired字段为空时，返回值为1
     * 2、当gmtExpired字段不为空，时间比当前时间要小，返回1
     * 3、当gmtExpired字段不为空，时间比当前时间要大或者相等，返回0
     */
    @Test
    public void testCountExpired() {
        int originCountExpired = loginRecordDao.countExpired();

        //插入一个比当前时间短的记录
        LoginRecord newLoginRecord = new LoginRecord();

        newLoginRecord.setIp("127.0.0.2");
        newLoginRecord.setToken(generateRandomToken(TOKEN_LENGTH));
        newLoginRecord.setUserId(3);
        long time = System.currentTimeMillis() - 3600 * 10 * 1000;
        Timestamp timestamp = new Timestamp(time);
        newLoginRecord.setGmtExpire(timestamp);

        loginRecordDao.insert(newLoginRecord);

        Assert.assertTrue(loginRecordDao.countExpired() == originCountExpired + 1);

        //更新gmtExpired，使得expire比当前时间大100秒
        time = System.currentTimeMillis() + 3600 * 10 * 1000;
        timestamp = new Timestamp(time);
        newLoginRecord.setGmtExpire(timestamp);
        loginRecordDao.update(newLoginRecord);

        Assert.assertTrue(loginRecordDao.countExpired() == originCountExpired);

        //清理数据
        loginRecordDao.delete(newLoginRecord.getId());
    }

    /**
     * 测试两种情况
     * 1、当gmtExpired字段不为空，时间比当前时间要小，删除成功
     * 2、当gmtExpired字段不为空，时间比当前时间要大或者相等，删除失败
     */
    @Test
    public void testDeleteExpired() {
        LoginRecord newLoginRecord = new LoginRecord();

        //删除失败
        newLoginRecord.setIp("127.0.0.2");
        newLoginRecord.setToken(generateRandomToken(TOKEN_LENGTH));
        newLoginRecord.setUserId(3);
        long time = System.currentTimeMillis() + 3600 * 10 * 1000;
        Timestamp timestamp = new Timestamp(time);
        newLoginRecord.setGmtExpire(timestamp);
        loginRecordDao.insert(newLoginRecord);

        loginRecordDao.deleteExpired();
        Assert.assertTrue(loginRecordDao.countExpired() == 0);

        //删除成功
        time = System.currentTimeMillis() - 3600 * 10 * 1000;
        timestamp = new Timestamp(time);
        newLoginRecord.setGmtExpire(timestamp);
        loginRecordDao.update(newLoginRecord);
        Assert.assertTrue(loginRecordDao.countExpired() == 1);
        loginRecordDao.deleteExpired();
        Assert.assertTrue(loginRecordDao.countExpired() == 0);
    }

    /**
     * 在更新的时候要sleep 1s
     * 这样避免最后要比较的两个时间是相等的情况
     */
    @Test
    public void testUpdate() {
        LoginRecord newloginRecord = loginRecordDao.select(loginRecord.getId());
        newloginRecord.setToken(generateRandomToken(TOKEN_LENGTH));
        newloginRecord.setIp("127.10.1.1");
        
        try{
        	Thread.sleep(1000 * 2);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }

        Timestamp oldGmtUpdate = newloginRecord.getGmtUpdate();
        loginRecordDao.update(newloginRecord);

        Timestamp updateGmtUpdate = loginRecordDao.select(loginRecord.getId()).getGmtUpdate();
        System.out.println(oldGmtUpdate.toString() + "：" +updateGmtUpdate.toString());
        Assert.assertFalse(updateGmtUpdate.toString().equals(oldGmtUpdate.toString()));
    }
}
