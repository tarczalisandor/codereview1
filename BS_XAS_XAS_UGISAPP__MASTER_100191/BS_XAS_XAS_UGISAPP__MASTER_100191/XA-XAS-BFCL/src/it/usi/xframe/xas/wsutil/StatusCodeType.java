/**
 * StatusCodeType.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class StatusCodeType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StatusCodeType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _XAS00080I = "XAS00080I";
    public static final java.lang.String _XAS00081E = "XAS00081E";
    public static final java.lang.String _XAS00082E = "XAS00082E";
    public static final StatusCodeType XAS00080I = new StatusCodeType(_XAS00080I);
    public static final StatusCodeType XAS00081E = new StatusCodeType(_XAS00081E);
    public static final StatusCodeType XAS00082E = new StatusCodeType(_XAS00082E);
    public java.lang.String getValue() { return _value_;}
    public static StatusCodeType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        StatusCodeType enum = (StatusCodeType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static StatusCodeType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
}
