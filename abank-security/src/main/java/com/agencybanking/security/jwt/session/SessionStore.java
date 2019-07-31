/**
 * 
 */
package com.agencybanking.security.jwt.session;

import com.agencybanking.security.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

/**
 * Persistence interface for JWT Tokens
 * 
 * @author dubic
 *
 */
public abstract class SessionStore {
	protected int timeout = 3;
	private Date lastCheck;

	/*
	 * Store the Session
	 */
	public abstract void startSession(User user, long tokenCreation);

	/*
	 * Retrieve current sessions
	 */
	public abstract Page<Session> getCurrentSessions(PageRequest p);

	/*
	 * check if session has expired
	 */
	public abstract boolean sessionExpired(Session session, boolean isWeb);

	public abstract void expireSession(Session session);

	@Scheduled(fixedRate = 60000*10)
	public void checkTimeout() {
		lastCheck = new Date();
		runTimeout();
	}

	public abstract void runTimeout();

	public Date getLastCheck() {
		return lastCheck;
	}

	public abstract Session retrieveSession(String username, long tokenCreation);
	
	public abstract void removeAllSessions();
	
	/*
	 * expunge a user's session in an application if no application is specified user session is terminated
	 * for all applications
	 */
	public abstract void expungeSession(String username);

	public abstract List<Session> getUserSessions(User user, Direction asc);

}
