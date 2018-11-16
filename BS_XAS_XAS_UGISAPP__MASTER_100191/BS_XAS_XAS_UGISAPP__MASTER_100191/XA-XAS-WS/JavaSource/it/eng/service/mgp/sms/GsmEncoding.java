/**
 * GsmEncoding.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class GsmEncoding implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected GsmEncoding(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _GSM7 = "GSM7";
    public static final java.lang.String _OCTECT_UNSPECIFIED = "OCTECT_UNSPECIFIED";
    public static final java.lang.String _LATIN_1 = "LATIN_1";
    public static final java.lang.String _CYRILLIC = "CYRILLIC";
    public static final java.lang.String _UCS2 = "UCS2";
    public static final GsmEncoding GSM7 = new GsmEncoding(_GSM7);
    public static final GsmEncoding OCTECT_UNSPECIFIED = new GsmEncoding(_OCTECT_UNSPECIFIED);
    public static final GsmEncoding LATIN_1 = new GsmEncoding(_LATIN_1);
    public static final GsmEncoding CYRILLIC = new GsmEncoding(_CYRILLIC);
    public static final GsmEncoding UCS2 = new GsmEncoding(_UCS2);
    public java.lang.String getValue() { return _value_;}
    public static GsmEncoding fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        GsmEncoding enum = (GsmEncoding)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static GsmEncoding fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
}
