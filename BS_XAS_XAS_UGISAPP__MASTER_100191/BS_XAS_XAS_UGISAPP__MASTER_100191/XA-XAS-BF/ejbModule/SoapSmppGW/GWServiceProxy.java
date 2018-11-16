package SoapSmppGW;

public class GWServiceProxy implements SoapSmppGW.GWService {
  private boolean _useJNDI = true;
  private boolean _useJNDIOnly = false;
  private String _endpoint = null;
  private SoapSmppGW.GWService __gWService = null;
  
  public GWServiceProxy() {
    _initGWServiceProxy();
  }
  
  private void _initGWServiceProxy() {
  
    if (_useJNDI || _useJNDIOnly) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __gWService = ((SoapSmppGW.GWServiceService)ctx.lookup("java:comp/env/service/GWServiceService")).getGWService();
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
    if (__gWService == null && !_useJNDIOnly) {
      try {
        __gWService = (new SoapSmppGW.GWServiceServiceLocator()).getGWService();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__gWService != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__gWService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__gWService)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __gWService = null;
    
  }
  
  public void useJNDIOnly(boolean useJNDIOnly) {
    _useJNDIOnly = useJNDIOnly;
    __gWService = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__gWService == null)
      _initGWServiceProxy();
    if (__gWService != null)
      ((javax.xml.rpc.Stub)__gWService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SoapSmppGW.Submit_resp submit(SoapSmppGW.Submit_sm sm, SoapSmppGW.GWSession gws) throws java.rmi.RemoteException{
    if (__gWService == null)
      _initGWServiceProxy();
    return __gWService.submit(sm, gws);
  }
  
  public SoapSmppGW.Deliver_resp deliver(SoapSmppGW.Deliver_sm sm, SoapSmppGW.GWSession gws) throws java.rmi.RemoteException{
    if (__gWService == null)
      _initGWServiceProxy();
    return __gWService.deliver(sm, gws);
  }
  
  
  public SoapSmppGW.GWService getGWService() {
    if (__gWService == null)
      _initGWServiceProxy();
    return __gWService;
  }
  
}