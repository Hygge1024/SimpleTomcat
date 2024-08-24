package com.lt.jerrymous.engine;

import com.lt.jerrymous.engine.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManagerImpl implements Runnable{
    final Logger logger = LoggerFactory.getLogger(getClass());
//    保存与该会话管理器关联的 ServletContextImpl 对象，它提供了与 Servlet 环境的交互。
    final ServletContextImpl servletContext;
//    使用线程安全的 ConcurrentHashMap 存储会话。键是会话 ID，值是 HttpSessionImpl 对象。
    final Map<String, HttpSessionImpl> sessions = new ConcurrentHashMap<>();
//    代表会话的最大不活动时间（以秒为单位），即会话在此时间内没有访问则被视为过期。
    final int inactiveInterval;

    public SessionManagerImpl(ServletContextImpl servletContext, int interval) {
        this.servletContext = servletContext;
        this.inactiveInterval = interval;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
    //获取给定SessionID对应的对话
    public HttpSession getSession(String sessionId){
        HttpSessionImpl session = sessions.get(sessionId);
        if(session == null){
            session = new HttpSessionImpl(this.servletContext, sessionId,inactiveInterval);
//            对象并将其存入 sessions
            sessions.put(sessionId,session);
        }
        return session;
    }
    public void remove(HttpSession session) {
        this.sessions.remove(session.getId());
    }

    @Override
    public void run() {
        for (;;) {
            try {
                Thread.sleep(60_000L);
            } catch (InterruptedException e) {
                break;
            }
            long now = System.currentTimeMillis() - 60_000L;
            for (String sessionId : sessions.keySet()) {
                HttpSession session = sessions.get(sessionId);
                if (now + session.getMaxInactiveInterval() * 1000L > session.getLastAccessedTime()) {
                    logger.warn("remove expired session: {}, last access time: {}", sessionId, DateUtils.formatDateTimeGMT(session.getLastAccessedTime()));
                    session.invalidate();
                }
            }
        }
    }
}
