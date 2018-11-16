/**
 * GsmPriority.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class GsmPriority implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected GsmPriority(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _LOW = "LOW";
    public static final java.lang.String _NORMAL = "NORMAL";
    public static final java.lang.String _URGENT = "URGENT";
    public static final java.lang.String _VERY_URGENT = "VERY_URGENT";
    public static final GsmPriority LOW = new GsmPriority(_LOW);
    public static final GsmPriority NORMAL = new GsmPriority(_NORMAL);
    public static final GsmPriority URGENT = new GsmPriority(_URGENT);
    public static final GsmPriority VERY_URGENT = new GsmPriority(_VERY_URGENT);
    public java.lang.String getValue() { return _value_;}
    public static GsmPriority fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        GsmPriority enum = (GsmPriority)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static GsmPriority fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
}
