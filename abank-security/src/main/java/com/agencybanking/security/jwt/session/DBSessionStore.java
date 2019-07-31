/**
 *
 */
package com.agencybanking.security.jwt.session;

import com.agencybanking.core.services.AppContextHolder;
import com.agencybanking.security.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author dubic
 */
@Service
public class DBSessionStore extends SessionStore {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionRepository sessionRepository;
    //	@Autowired
//	private SecurityService securityService;
    @Autowired
    private ApplicationEventPublisher eventCaster;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unionsystems.security.jwt.session.SessionStore#startSession(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    @Override
    public void startSession(User user, long tokenCreation) {
        Session session = new Session(user, tokenCreation);
        session.setIp(AppContextHolder.getInstance().getRequest().getRemoteAddr());
        session.setDomain(null);
        session.setExpires(getExpiryDate(new Date()));
        sessionRepository.save(session);
        logger.debug("Session saved in database..." + session.getId());
        eventCaster.publishEvent(new SessionEvent(session, SessionEvent.SESSION_CREATED));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unionsystems.security.jwt.session.SessionStore#getCurrentSessions()
     */
    @Override
    public Page<Session> getCurrentSessions(PageRequest p) {
//        return sessionRepository.findAll(new Session().predicates(), p);
		return sessionRepository.findAll(p);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unionsystems.security.jwt.session.SessionStore#sessionExpired(com.
     * unionsystems.security.jwt.session.Session, boolean)
     */
    @Override
    public boolean sessionExpired(Session session, boolean isWeb) {

        Date expires = session.getExpires();// getExpiryDate(session.getLastAccess());
        logger.debug("Token for {} Expires : {}", session.getUsername(), expires);
        if (new Date().after(expires)) {
            expireSession(session);
            return true;
        }
        if (isWeb) {
            // refresh session timeout date
            session.resetLastAccess();
            session.setExpires(getExpiryDate(new Date()));
            sessionRepository.save(session);
        }
        return false;
    }

    private Date getExpiryDate(Date accessDate) {
//		SecurityPolicy policy = securityService.getSecurityPolicy(1L);
//		Assert.notNull(policy, "No security policy found");
//		Date shouldExpire = new Date(accessDate.getTime() + policy.getSessionTimeout() * 60000);
        return new Date(accessDate.getTime() + 30 * 60000);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unionsystems.security.jwt.session.SessionStore#expireSession(java.
     * lang.String, java.lang.String)
     */
    @Override
    public void expireSession(Session session) {
        if (session == null) {
            logger.warn("Session expiration return null on remove");
            return;
        }
        int removed = sessionRepository.remove(session.getId());
        if (removed > 0) {
			eventCaster.publishEvent(new SessionEvent(session, SessionEvent.SESSION_DESTROYED));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unionsystems.security.jwt.session.SessionStore#runTimeout()
     */

    @Override
    public void runTimeout() {
        PageRequest paged = PageRequest.of(0, 20);
        sessionRepository.getExpiredSessions(paged).forEach(this::expireSession);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unionsystems.security.jwt.session.SessionStore#retrieveSession(java.
     * lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Session retrieveSession(String username, long tokenCreation) {
        logger.debug("Retreive session [{}]", Session.buildId(username, tokenCreation));
        return sessionRepository.findById(Session.buildId(username, tokenCreation)).orElse(null);
    }

    @Override
    public void removeAllSessions() {
        sessionRepository.deleteAll();
        logger.debug("Cleared all sessions from DB");
    }

    @Override
    public void expungeSession(String username) {
        sessionRepository.findByUsername(username).forEach(this::expireSession);
    }

    @Override
    public List<Session> getUserSessions(User user, Direction dir) {
        return sessionRepository.findByUsername(user.getUsername(), new Sort(dir, "id"));
    }

}
