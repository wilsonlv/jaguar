package org.jaguar.commons.shiro.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;

/**
 * 会话监听器
 *
 * @author lvws
 */
@Component
public class SessionListener implements org.apache.shiro.session.SessionListener {

    private Logger logger = LogManager.getLogger(SessionListener.class);

    @Override
    public void onStart(Session session) {
        logger.info("创建了一个Session连接:[" + session.getId() + "]");
    }

    @Override
    public void onStop(Session session) {
        logger.info("销毁了一个Session连接:[" + session.getId() + "]");
    }

    @Override
    public void onExpiration(Session session) {
        logger.info("过期了一个Session连接:[" + session.getId() + "]");
    }
}
