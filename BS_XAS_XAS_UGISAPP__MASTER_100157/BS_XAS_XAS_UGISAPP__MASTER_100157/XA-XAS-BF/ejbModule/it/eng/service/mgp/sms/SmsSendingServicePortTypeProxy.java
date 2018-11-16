package it.eng.service.mgp.sms;

public class SmsSendingServicePortTypeProxy implements it.eng.service.mgp.sms.SmsSendingServicePortType {
  private boolean _useJNDI = true;
  private boolean _useJNDIOnly = false;
  private String _endpoint = null;
  private it.eng.service.mgp.sms.SmsSendingServicePortType __smsSendingServicePortType = null;
  
  public SmsSendingServicePortTypeProxy() {
    _initSmsSendingServicePortTypeProxy();
  }
  
  private void _initSmsSendingServicePortTypeProxy() {
  
    if (_useJNDI || _useJNDIOnly) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __smsSendingServicePortType = ((it.eng.service.mgp.sms.SmsSendingService)ctx.lookup("java:comp/env/service/SmsSendingService")).getSmsSendingServicePort();
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
    if (__smsSendingServicePortType == null && !_useJNDIOnly) {
      try {
        __smsSendingServicePortType = (new it.eng.service.mgp.sms.SmsSendingServiceLocator()).getSmsSendingServicePort();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__smsSendingServicePortType != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__smsSendingServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__smsSendingServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __smsSendingServicePortType = null;
    
  }
  
  public void useJNDIOnly(boolean useJNDIOnly) {
    _useJNDIOnly = useJNDIOnly;
    __smsSendingServicePortType = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__smsSendingServicePortType == null)
      _initSmsSendingServicePortTypeProxy();
    if (__smsSendingServicePortType != null)
      ((javax.xml.rpc.Stub)__smsSendingServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.eng.service.mgp.sms.MtSendingResult sendMessage(it.eng.service.mgp.sms.MtMessage arg1) throws java.rmi.RemoteException{
    if (__smsSendingServicePortType == null)
      _initSmsSendingServicePortTypeProxy();
    return __smsSendingServicePortType.sendMessage(arg1);
  }
  
  
  public it.eng.service.mgp.sms.SmsSendingServicePortType getSmsSendingServicePortType() {
    if (__smsSendingServicePortType == null)
      _initSmsSendingServicePortTypeProxy();
    return __smsSendingServicePortType;
  }
  
}