/**
 * ENUM_STATUS.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class ENUM_STATUS implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ENUM_STATUS(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _NO_ROUTE = "NO_ROUTE";
    public static final java.lang.String _REJECTED_BY_FILTER = "REJECTED_BY_FILTER";
    public static final java.lang.String _EXPIRED_IN_QUEUE = "EXPIRED_IN_QUEUE";
    public static final java.lang.String _COMMAND_FAILED = "COMMAND_FAILED";
    public static final java.lang.String _UNKNOWN_SMSC_RESPONSE = "UNKNOWN_SMSC_RESPONSE";
    public static final java.lang.String _REJECTED_BY_SMSC = "REJECTED_BY_SMSC";
    public static final java.lang.String _DROPPED_BY_SMSC = "DROPPED_BY_SMSC";
    public static final java.lang.String _EXPIRED_ON_SMSC = "EXPIRED_ON_SMSC";
    public static final java.lang.String _REJECTED_BY_DEV = "REJECTED_BY_DEV";
    public static final java.lang.String _DELIVERED_TO_DEV = "DELIVERED_TO_DEV";
    public static final ENUM_STATUS NO_ROUTE = new ENUM_STATUS(_NO_ROUTE);
    public static final ENUM_STATUS REJECTED_BY_FILTER = new ENUM_STATUS(_REJECTED_BY_FILTER);
    public static final ENUM_STATUS EXPIRED_IN_QUEUE = new ENUM_STATUS(_EXPIRED_IN_QUEUE);
    public static final ENUM_STATUS COMMAND_FAILED = new ENUM_STATUS(_COMMAND_FAILED);
    public static final ENUM_STATUS UNKNOWN_SMSC_RESPONSE = new ENUM_STATUS(_UNKNOWN_SMSC_RESPONSE);
    public static final ENUM_STATUS REJECTED_BY_SMSC = new ENUM_STATUS(_REJECTED_BY_SMSC);
    public static final ENUM_STATUS DROPPED_BY_SMSC = new ENUM_STATUS(_DROPPED_BY_SMSC);
    public static final ENUM_STATUS EXPIRED_ON_SMSC = new ENUM_STATUS(_EXPIRED_ON_SMSC);
    public static final ENUM_STATUS REJECTED_BY_DEV = new ENUM_STATUS(_REJECTED_BY_DEV);
    public static final ENUM_STATUS DELIVERED_TO_DEV = new ENUM_STATUS(_DELIVERED_TO_DEV);
    public java.lang.String getValue() { return _value_;}
    public static ENUM_STATUS fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        ENUM_STATUS enum = (ENUM_STATUS)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static ENUM_STATUS fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
}
