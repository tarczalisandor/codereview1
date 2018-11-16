/**
 * ReplaceClass.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.bfutil.data;

public class ReplaceClass  implements java.io.Serializable, com.ibm.ws.webservices.engine.encoding.SimpleType {
    private java.lang.String value;

    public ReplaceClass() {
    }

    // Simple Types must have a String constructor
    public ReplaceClass(java.lang.String value) {
        this.value = new java.lang.String(value);
    }

    // Simple Types must have a toString for serializing the value
    public java.lang.String toString() {
        return value == null ? null : value.toString();
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        ReplaceClass other = (ReplaceClass) obj;
        boolean _equals;
        _equals = true
            && ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        ReplaceClass history = (ReplaceClass) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        __history.set(null);
        return true;
    }

    private transient java.lang.ThreadLocal __hashHistory;
    public int hashCode() {
        if (__hashHistory == null) {
            synchronized (this) {
                if (__hashHistory == null) {
                    __hashHistory = new java.lang.ThreadLocal();
                }
            }
        }
        ReplaceClass history = (ReplaceClass) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
