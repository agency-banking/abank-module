package com.agencybanking.security.jwt.session;

public class SessionEvent {

    public static final String SESSION_CREATED = "created";
    public static final String SESSION_DESTROYED = "created";

    private final Session session;
    private final String status;

    public SessionEvent(Session session, String status) {
        this.session = session;
        this.status = status;
    }

    public Session getSession() {
        return session;
    }
}
