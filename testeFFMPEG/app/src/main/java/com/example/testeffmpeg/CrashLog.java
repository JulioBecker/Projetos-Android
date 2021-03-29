package com.example.testeffmpeg;

import android.util.Log;


/**
 * Created by arthur on 21/09/17.
 */

public final class CrashLog {

    /**
     * Priority constant for the println method; use CrashLog.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use CrashLog.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use CrashLog.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use CrashLog.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use CrashLog.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    /**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        Log.v(tag, msg);

    }


    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        Log.d(tag, msg);

    }


    /**
     * Send an {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        Log.i(tag, msg);

    }


    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        Log.w(tag, msg);

    }

    /**
     * Send an {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        Log.e(tag, msg);

    }

}
