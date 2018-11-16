package it.usi.xframe.xas.wsutil;

public class XasNotificationProxy implements it.usi.xframe.xas.wsutil.XasNotification {
  private boolean _useJNDI = true;
  private boolean _useJNDIOnly = false;
  private String _endpoint = null;
  private it.usi.xframe.xas.wsutil.XasNotification __xasNotification = null;
  
  public XasNotificationProxy() {
    _initXasNotificationProxy();
  }
  
  private void _initXasNotificationProxy() {
  
    if (_useJNDI || _useJNDIOnly) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __xasNotification = ((it.usi.xframe.xas.wsutil.XasNotificationService)ctx.lookup("java:comp/env/service/XasNotificationService")).getXasNotification();
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
    if (__xasNotification == null && !_useJNDIOnly) {
      try {
        __xasNotification = (new it.usi.xframe.xas.wsutil.XasNotificationServiceLocator()).getXasNotification();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__xasNotification != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__xasNotification)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__xasNotification)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __xasNotification = null;
    
  }
  
  public void useJNDIOnly(boolean useJNDIOnly) {
    _useJNDIOnly = useJNDIOnly;
    __xasNotification = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__xasNotification == null)
      _initXasNotificationProxy();
    if (__xasNotification != null)
      ((javax.xml.rpc.Stub)__xasNotification)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.usi.xframe.xas.wsutil.NotificationResponse notifyMobileOriginated(it.usi.xframe.xas.wsutil.MobileOriginated mobileOriginatedRequest) throws java.rmi.RemoteException{
    if (__xasNotification == null)
      _initXasNotificationProxy();
    return __xasNotification.notifyMobileOriginated(mobileOriginatedRequest);
  }
  
  public it.usi.xframe.xas.wsutil.NotificationResponse notifyDeliveryReport(it.usi.xframe.xas.wsutil.DeliveryReport deliveryReportRequest) throws java.rmi.RemoteException{
    if (__xasNotification == null)
      _initXasNotificationProxy();
    return __xasNotification.notifyDeliveryReport(deliveryReportRequest);
  }
  
  
  public it.usi.xframe.xas.wsutil.XasNotification getXasNotification() {
    if (__xasNotification == null)
      _initXasNotificationProxy();
    return __xasNotification;
  }
  
}