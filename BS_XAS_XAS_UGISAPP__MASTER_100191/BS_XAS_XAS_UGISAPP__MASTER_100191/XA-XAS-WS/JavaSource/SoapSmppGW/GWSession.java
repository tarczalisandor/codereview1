/**
 * GWSession.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class GWSession  implements java.io.Serializable {
    private java.lang.String accountName;
    private java.lang.String accountPassword;

    public GWSession() {
    }

    public java.lang.String getAccountName() {
        return accountName;
    }

    public void setAccountName(java.lang.String accountName) {
        this.accountName = accountName;
    }

    public java.lang.String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(java.lang.String accountPassword) {
        this.accountPassword = accountPassword;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        GWSession other = (GWSession) obj;
        boolean _equals;
        _equals = true
            && ((this.accountName==null && other.getAccountName()==null) || 
             (this.accountName!=null &&
              this.accountName.equals(other.getAccountName())))
            && ((this.accountPassword==null && other.getAccountPassword()==null) || 
             (this.accountPassword!=null &&
              this.accountPassword.equals(other.getAccountPassword())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        GWSession history = (GWSession) __history.get();
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
        GWSession history = (GWSession) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getAccountName() != null) {
            _hashCode += getAccountName().hashCode();
        }
        if (getAccountPassword() != null) {
            _hashCode += getAccountPassword().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
