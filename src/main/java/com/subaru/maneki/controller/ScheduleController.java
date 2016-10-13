package com.subaru.maneki.controller;

import com.subaru.maneki.dao.LoginRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * @author zhangchaojie
 * @since 2016-08-16
 */
@Controller
public class ScheduleController {

    @Resource
    private LoginRecordDao loginRecordDao;

    private Logger         logger = LoggerFactory.getLogger(ScheduleController.class);

    @Scheduled(cron = "0 0 1 * * ?")
    @RequestMapping(value = "/schedule/refresh_login_record", method = RequestMethod.GET)
    public void refreshLoginRecord() {

        int expiredCount = loginRecordDao.countExpired();
        if (expiredCount > 0) {
            loginRecordDao.deleteExpired();
        }
        logger.info("Schedule refreshLoginRecord over, within " + expiredCount
                    + "records deleted !!");
        return;

    }
}
