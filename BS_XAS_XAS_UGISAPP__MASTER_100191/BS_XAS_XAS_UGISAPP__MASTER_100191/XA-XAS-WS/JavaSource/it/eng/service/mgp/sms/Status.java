/**
 * Status.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class Status implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Status(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _NO_ROUTE = "NO_ROUTE";
    public static final java.lang.String _REJECTED_BY_FILTER = "REJECTED_BY_FILTER";
    public static final java.lang.String _EXPIRED_IN_QUEUE = "EXPIRED_IN_QUEUE";
    public static final java.lang.String _COMMAND_FAILED = "COMMAND_FAILED";
    public static final java.lang.String _UNKNOWN_SMSC_RESPONSE = "UNKNOWN_SMSC_RESPONSE";
    public static final java.lang.String _ACCEPTED_BY_SMSC = "ACCEPTED_BY_SMSC";
    public static final java.lang.String _REJECTED_BY_SMSC = "REJECTED_BY_SMSC";
    public static final java.lang.String _DROPPED_BY_SMSC = "DROPPED_BY_SMSC";
    public static final java.lang.String _EXPIRED_ON_SMSC = "EXPIRED_ON_SMSC";
    public static final java.lang.String _DELIVERED_TO_DEV = "DELIVERED_TO_DEV";
    public static final java.lang.String _REJECTED_BY_DEV = "REJECTED_BY_DEV";
    public static final Status NO_ROUTE = new Status(_NO_ROUTE);
    public static final Status REJECTED_BY_FILTER = new Status(_REJECTED_BY_FILTER);
    public static final Status EXPIRED_IN_QUEUE = new Status(_EXPIRED_IN_QUEUE);
    public static final Status COMMAND_FAILED = new Status(_COMMAND_FAILED);
    public static final Status UNKNOWN_SMSC_RESPONSE = new Status(_UNKNOWN_SMSC_RESPONSE);
    public static final Status ACCEPTED_BY_SMSC = new Status(_ACCEPTED_BY_SMSC);
    public static final Status REJECTED_BY_SMSC = new Status(_REJECTED_BY_SMSC);
    public static final Status DROPPED_BY_SMSC = new Status(_DROPPED_BY_SMSC);
    public static final Status EXPIRED_ON_SMSC = new Status(_EXPIRED_ON_SMSC);
    public static final Status DELIVERED_TO_DEV = new Status(_DELIVERED_TO_DEV);
    public static final Status REJECTED_BY_DEV = new Status(_REJECTED_BY_DEV);
    public java.lang.String getValue() { return _value_;}
    public static Status fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        Status enum = (Status)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static Status fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
}
