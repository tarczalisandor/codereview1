package eu.unicredit.sms.serverweb;

public class SendSMS_PortProxy implements eu.unicredit.sms.serverweb.SendSMS_Port {
  private boolean _useJNDI = true;
  private boolean _useJNDIOnly = false;
  private String _endpoint = null;
  private eu.unicredit.sms.serverweb.SendSMS_Port __sendSMS_Port = null;
  
  public SendSMS_PortProxy() {
    _initSendSMS_PortProxy();
  }
  
  private void _initSendSMS_PortProxy() {
  
    if (_useJNDI || _useJNDIOnly) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __sendSMS_Port = ((eu.unicredit.sms.serverweb.SendSMS_Service)ctx.lookup("java:comp/env/service/SendSMS")).getSendSMSImplPort();
      }
      catch (javax.naming.NamingException namingException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("JNDI lookup failure: javax.naming.NamingException: " + namingException.getMessage());
          namingException.printStackTrace(System.out);
        }
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__sendSMS_Port == null && !_useJNDIOnly) {
      try {
        __sendSMS_Port = (new eu.unicredit.sms.serverweb.SendSMS_ServiceLocator()).getSendSMSImplPort();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__sendSMS_Port != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__sendSMS_Port)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__sendSMS_Port)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __sendSMS_Port = null;
    
  }
  
  public void useJNDIOnly(boolean useJNDIOnly) {
    _useJNDIOnly = useJNDIOnly;
    __sendSMS_Port = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__sendSMS_Port == null)
      _initSendSMS_PortProxy();
    if (__sendSMS_Port != null)
      ((javax.xml.rpc.Stub)__sendSMS_Port)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public int send(java.lang.String phonenumber, java.lang.String msg, java.lang.String alias, java.lang.String abicode) throws java.rmi.RemoteException{
    if (__sendSMS_Port == null)
      _initSendSMS_PortProxy();
    return __sendSMS_Port.send(phonenumber, msg, alias, abicode);
  }
  
  
  public eu.unicredit.sms.serverweb.SendSMS_Port getSendSMS_Port() {
    if (__sendSMS_Port == null)
      _initSendSMS_PortProxy();
    return __sendSMS_Port;
  }
  
}