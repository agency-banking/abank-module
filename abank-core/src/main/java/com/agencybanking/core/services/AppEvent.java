package com.agencybanking.core.services;

public interface AppEvent<T> {

    /**Fires event for App flow and audit logging
     * @param company
     * @param t
     * @param product
     * @param event
     */
    void fireAppEvent(String company, T t, String product, String event);
}
