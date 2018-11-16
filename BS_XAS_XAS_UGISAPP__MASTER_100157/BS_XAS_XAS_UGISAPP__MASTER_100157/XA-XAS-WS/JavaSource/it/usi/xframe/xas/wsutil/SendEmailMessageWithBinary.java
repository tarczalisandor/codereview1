/**
 * SendEmailMessageWithBinary.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class SendEmailMessageWithBinary  implements java.io.Serializable {
    private it.usi.xframe.xas.bfutil.data.EmailMessage emailMessage;
    private it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment[] attachments;

    public SendEmailMessageWithBinary() {
    }

    public it.usi.xframe.xas.bfutil.data.EmailMessage getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(it.usi.xframe.xas.bfutil.data.EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    public it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment[] getAttachments() {
        return attachments;
    }

    public void setAttachments(it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment[] attachments) {
        this.attachments = attachments;
    }

    public it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment getAttachments(int i) {
        return attachments[i];
    }

    public void setAttachments(int i, it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment value) {
        this.attachments[i] = value;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        SendEmailMessageWithBinary history = (SendEmailMessageWithBinary) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        SendEmailMessageWithBinary other = (SendEmailMessageWithBinary) obj;
        boolean _equals;
        _equals = true
            && ((this.emailMessage==null && other.getEmailMessage()==null) || 
             (this.emailMessage!=null &&
              this.emailMessage.equals(other.getEmailMessage())))
            && ((this.attachments==null && other.getAttachments()==null) || 
             (this.attachments!=null &&
              java.util.Arrays.equals(this.attachments, other.getAttachments())));
        if (!_equals) {
            __history.set(null);
            return false;
        };
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
        SendEmailMessageWithBinary history = (SendEmailMessageWithBinary) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getEmailMessage() != null) {
            _hashCode += getEmailMessage().hashCode();
        }
        if (getAttachments() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttachments());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttachments(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
