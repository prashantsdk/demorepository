package com.blueplanet.smartcookieteacher.notification;

/**
 * Created by dhanashree.ghayal on 30-06-2015.
 * This is an interface need to be implemented by all Listeners who have registered for
 * Whenever any registered event is fired it will call eventNotify method.
 */
public interface IEventListener {

    /**
     * This method is called when any registered event is fired. Need to be implemented by all sub
     * classes.
     *
     * @param eventType   Constant indicating type of Event
     * @param eventObject Object containing extra information regarding event.
     * @return One of the Event state EVENT_CONSUMED/ EVENT_PROCESSED/ EVENT_IGNORED from
     */
    public int eventNotify(int eventType, Object eventObject);

}
