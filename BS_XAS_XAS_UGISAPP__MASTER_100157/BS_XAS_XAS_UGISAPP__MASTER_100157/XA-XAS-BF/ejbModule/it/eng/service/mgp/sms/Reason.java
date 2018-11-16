/**
 * Reason.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class Reason implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Reason(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _INVALID_SRC_ADDR = "INVALID_SRC_ADDR";
    public static final java.lang.String _INVALID_DEST_ADDR = "INVALID_DEST_ADDR";
    public static final java.lang.String _INVALID_PAYLOAD = "INVALID_PAYLOAD";
    public static final java.lang.String _PAYLOAD_TOO_LONG = "PAYLOAD_TOO_LONG";
    public static final java.lang.String _INVALID_PAYLOAD_ENCODING = "INVALID_PAYLOAD_ENCODING";
    public static final java.lang.String _TX_DUPLICATED = "TX_DUPLICATED";
    public static final java.lang.String _UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
    public static final Reason INVALID_SRC_ADDR = new Reason(_INVALID_SRC_ADDR);
    public static final Reason INVALID_DEST_ADDR = new Reason(_INVALID_DEST_ADDR);
    public static final Reason INVALID_PAYLOAD = new Reason(_INVALID_PAYLOAD);
    public static final Reason PAYLOAD_TOO_LONG = new Reason(_PAYLOAD_TOO_LONG);
    public static final Reason INVALID_PAYLOAD_ENCODING = new Reason(_INVALID_PAYLOAD_ENCODING);
    public static final Reason TX_DUPLICATED = new Reason(_TX_DUPLICATED);
    public static final Reason UNEXPECTED_ERROR = new Reason(_UNEXPECTED_ERROR);
    public java.lang.String getValue() { return _value_;}
    public static Reason fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        Reason enum = (Reason)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static Reason fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
}
