package net.nel.il.handler;

import org.springframework.stereotype.Component;


@Component
public class Handler {

    public static final int CREATING_ACCOUNT_FROM = 1;

    public static final int GETTING_DATA_FROM = 2;

    public static final int UPDATE_DATA_FROM = 3;

    public static final int REQUEST_FROM = 4;

    public static final int ACCEPT_FROM = 5;

    public static final int REJECT_FROM = 6;

    public static final int BREAK_FROM = 10;

    public static final int CREATE_ACCOUNT_TO = 1;

    public static final int GET_DATA_TO = 2;

    public static final int UPDATE_DATA_TO = 3;

    public static final int REQUEST_TO = 4;

    public static final int ACCEPT_TO = 5;

    public static final int REJECT_TO = 6;

    public static final int BREAK_TO = 10;

    public static final int TIME_OUTSIDE_TO = 11;

    public static final int FIRST_REQUEST_TO = 12;

    public static final int SENDING_MESSAGE = 13;

    public static final int BUSY = 15;

    public static final int NO_CONNECTION = 19;

    public static final int NO_REQUEST = -1;

    public static final int WAS_DONE_BREAK = -10;

    public static final int WAS_DONE_REJECTION = -2;

    public static final int WAS_DONE_NO_CONNECTION = -3;

    public static final int STATUS_AVAILABLE = 1;

    public static final int STATUS_UNAVAILABLE = 0;

    public static final int CLEAR_MESSAGES = 20;

    public static final int EVENT_REQUEST = 50;

    public static final int EVENT_CREATION_REQUEST = 51;

    public static final int TRYING_TO_RETURN_OLD_ACCOUNT = 100;

    public static final int NEGATIVE_TRYING_TO_RETURN_OLD_ACCOUNT = 101;

    public static final int POSITIVE_TRYING_TO_RETURN_OLD_ACCOUNT = 102;



    public Handler(){

    }
}
