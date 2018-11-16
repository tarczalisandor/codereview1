/**
 * Submit_sm.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class Submit_sm  implements java.io.Serializable {
    private java.lang.String destination_addr;
    private java.lang.String service_type;
    private java.lang.String source_addr;
    private java.lang.Byte source_addr_ton;
    private java.lang.Byte source_addr_npi;
    private java.lang.Byte dest_addr_ton;
    private java.lang.Byte dest_addr_npi;
    private java.lang.Byte esm_class;
    private java.lang.Byte protocol_id;
    private java.lang.Byte priority_flag;
    private java.lang.String schedule_delivery_time;
    private java.lang.String validity_period;
    private java.lang.Byte registered_delivery;
    private java.lang.String short_message;
    private java.lang.Byte replace_if_present_flag;
    private java.lang.Byte data_coding;
    private java.lang.Byte sm_default_msg_id;
    private java.lang.Byte sm_length;
    private java.lang.String message_payload;

    public Submit_sm() {
    }

    public java.lang.String getDestination_addr() {
        return destination_addr;
    }

    public void setDestination_addr(java.lang.String destination_addr) {
        this.destination_addr = destination_addr;
    }

    public java.lang.String getService_type() {
        return service_type;
    }

    public void setService_type(java.lang.String service_type) {
        this.service_type = service_type;
    }

    public java.lang.String getSource_addr() {
        return source_addr;
    }

    public void setSource_addr(java.lang.String source_addr) {
        this.source_addr = source_addr;
    }

    public java.lang.Byte getSource_addr_ton() {
        return source_addr_ton;
    }

    public void setSource_addr_ton(java.lang.Byte source_addr_ton) {
        this.source_addr_ton = source_addr_ton;
    }

    public java.lang.Byte getSource_addr_npi() {
        return source_addr_npi;
    }

    public void setSource_addr_npi(java.lang.Byte source_addr_npi) {
        this.source_addr_npi = source_addr_npi;
    }

    public java.lang.Byte getDest_addr_ton() {
        return dest_addr_ton;
    }

    public void setDest_addr_ton(java.lang.Byte dest_addr_ton) {
        this.dest_addr_ton = dest_addr_ton;
    }

    public java.lang.Byte getDest_addr_npi() {
        return dest_addr_npi;
    }

    public void setDest_addr_npi(java.lang.Byte dest_addr_npi) {
        this.dest_addr_npi = dest_addr_npi;
    }

    public java.lang.Byte getEsm_class() {
        return esm_class;
    }

    public void setEsm_class(java.lang.Byte esm_class) {
        this.esm_class = esm_class;
    }

    public java.lang.Byte getProtocol_id() {
        return protocol_id;
    }

    public void setProtocol_id(java.lang.Byte protocol_id) {
        this.protocol_id = protocol_id;
    }

    public java.lang.Byte getPriority_flag() {
        return priority_flag;
    }

    public void setPriority_flag(java.lang.Byte priority_flag) {
        this.priority_flag = priority_flag;
    }

    public java.lang.String getSchedule_delivery_time() {
        return schedule_delivery_time;
    }

    public void setSchedule_delivery_time(java.lang.String schedule_delivery_time) {
        this.schedule_delivery_time = schedule_delivery_time;
    }

    public java.lang.String getValidity_period() {
        return validity_period;
    }

    public void setValidity_period(java.lang.String validity_period) {
        this.validity_period = validity_period;
    }

    public java.lang.Byte getRegistered_delivery() {
        return registered_delivery;
    }

    public void setRegistered_delivery(java.lang.Byte registered_delivery) {
        this.registered_delivery = registered_delivery;
    }

    public java.lang.String getShort_message() {
        return short_message;
    }

    public void setShort_message(java.lang.String short_message) {
        this.short_message = short_message;
    }

    public java.lang.Byte getReplace_if_present_flag() {
        return replace_if_present_flag;
    }

    public void setReplace_if_present_flag(java.lang.Byte replace_if_present_flag) {
        this.replace_if_present_flag = replace_if_present_flag;
    }

    public java.lang.Byte getData_coding() {
        return data_coding;
    }

    public void setData_coding(java.lang.Byte data_coding) {
        this.data_coding = data_coding;
    }

    public java.lang.Byte getSm_default_msg_id() {
        return sm_default_msg_id;
    }

    public void setSm_default_msg_id(java.lang.Byte sm_default_msg_id) {
        this.sm_default_msg_id = sm_default_msg_id;
    }

    public java.lang.Byte getSm_length() {
        return sm_length;
    }

    public void setSm_length(java.lang.Byte sm_length) {
        this.sm_length = sm_length;
    }

    public java.lang.String getMessage_payload() {
        return message_payload;
    }

    public void setMessage_payload(java.lang.String message_payload) {
        this.message_payload = message_payload;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        Submit_sm other = (Submit_sm) obj;
        boolean _equals;
        _equals = true
            && ((this.destination_addr==null && other.getDestination_addr()==null) || 
             (this.destination_addr!=null &&
              this.destination_addr.equals(other.getDestination_addr())))
            && ((this.service_type==null && other.getService_type()==null) || 
             (this.service_type!=null &&
              this.service_type.equals(other.getService_type())))
            && ((this.source_addr==null && other.getSource_addr()==null) || 
             (this.source_addr!=null &&
              this.source_addr.equals(other.getSource_addr())))
            && ((this.source_addr_ton==null && other.getSource_addr_ton()==null) || 
             (this.source_addr_ton!=null &&
              this.source_addr_ton.equals(other.getSource_addr_ton())))
            && ((this.source_addr_npi==null && other.getSource_addr_npi()==null) || 
             (this.source_addr_npi!=null &&
              this.source_addr_npi.equals(other.getSource_addr_npi())))
            && ((this.dest_addr_ton==null && other.getDest_addr_ton()==null) || 
             (this.dest_addr_ton!=null &&
              this.dest_addr_ton.equals(other.getDest_addr_ton())))
            && ((this.dest_addr_npi==null && other.getDest_addr_npi()==null) || 
             (this.dest_addr_npi!=null &&
              this.dest_addr_npi.equals(other.getDest_addr_npi())))
            && ((this.esm_class==null && other.getEsm_class()==null) || 
             (this.esm_class!=null &&
              this.esm_class.equals(other.getEsm_class())))
            && ((this.protocol_id==null && other.getProtocol_id()==null) || 
             (this.protocol_id!=null &&
              this.protocol_id.equals(other.getProtocol_id())))
            && ((this.priority_flag==null && other.getPriority_flag()==null) || 
             (this.priority_flag!=null &&
              this.priority_flag.equals(other.getPriority_flag())))
            && ((this.schedule_delivery_time==null && other.getSchedule_delivery_time()==null) || 
             (this.schedule_delivery_time!=null &&
              this.schedule_delivery_time.equals(other.getSchedule_delivery_time())))
            && ((this.validity_period==null && other.getValidity_period()==null) || 
             (this.validity_period!=null &&
              this.validity_period.equals(other.getValidity_period())))
            && ((this.registered_delivery==null && other.getRegistered_delivery()==null) || 
             (this.registered_delivery!=null &&
              this.registered_delivery.equals(other.getRegistered_delivery())))
            && ((this.short_message==null && other.getShort_message()==null) || 
             (this.short_message!=null &&
              this.short_message.equals(other.getShort_message())))
            && ((this.replace_if_present_flag==null && other.getReplace_if_present_flag()==null) || 
             (this.replace_if_present_flag!=null &&
              this.replace_if_present_flag.equals(other.getReplace_if_present_flag())))
            && ((this.data_coding==null && other.getData_coding()==null) || 
             (this.data_coding!=null &&
              this.data_coding.equals(other.getData_coding())))
            && ((this.sm_default_msg_id==null && other.getSm_default_msg_id()==null) || 
             (this.sm_default_msg_id!=null &&
              this.sm_default_msg_id.equals(other.getSm_default_msg_id())))
            && ((this.sm_length==null && other.getSm_length()==null) || 
             (this.sm_length!=null &&
              this.sm_length.equals(other.getSm_length())))
            && ((this.message_payload==null && other.getMessage_payload()==null) || 
             (this.message_payload!=null &&
              this.message_payload.equals(other.getMessage_payload())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        Submit_sm history = (Submit_sm) __history.get();
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
        Submit_sm history = (Submit_sm) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getDestination_addr() != null) {
            _hashCode += getDestination_addr().hashCode();
        }
        if (getService_type() != null) {
            _hashCode += getService_type().hashCode();
        }
        if (getSource_addr() != null) {
            _hashCode += getSource_addr().hashCode();
        }
        if (getSource_addr_ton() != null) {
            _hashCode += getSource_addr_ton().hashCode();
        }
        if (getSource_addr_npi() != null) {
            _hashCode += getSource_addr_npi().hashCode();
        }
        if (getDest_addr_ton() != null) {
            _hashCode += getDest_addr_ton().hashCode();
        }
        if (getDest_addr_npi() != null) {
            _hashCode += getDest_addr_npi().hashCode();
        }
        if (getEsm_class() != null) {
            _hashCode += getEsm_class().hashCode();
        }
        if (getProtocol_id() != null) {
            _hashCode += getProtocol_id().hashCode();
        }
        if (getPriority_flag() != null) {
            _hashCode += getPriority_flag().hashCode();
        }
        if (getSchedule_delivery_time() != null) {
            _hashCode += getSchedule_delivery_time().hashCode();
        }
        if (getValidity_period() != null) {
            _hashCode += getValidity_period().hashCode();
        }
        if (getRegistered_delivery() != null) {
            _hashCode += getRegistered_delivery().hashCode();
        }
        if (getShort_message() != null) {
            _hashCode += getShort_message().hashCode();
        }
        if (getReplace_if_present_flag() != null) {
            _hashCode += getReplace_if_present_flag().hashCode();
        }
        if (getData_coding() != null) {
            _hashCode += getData_coding().hashCode();
        }
        if (getSm_default_msg_id() != null) {
            _hashCode += getSm_default_msg_id().hashCode();
        }
        if (getSm_length() != null) {
            _hashCode += getSm_length().hashCode();
        }
        if (getMessage_payload() != null) {
            _hashCode += getMessage_payload().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
