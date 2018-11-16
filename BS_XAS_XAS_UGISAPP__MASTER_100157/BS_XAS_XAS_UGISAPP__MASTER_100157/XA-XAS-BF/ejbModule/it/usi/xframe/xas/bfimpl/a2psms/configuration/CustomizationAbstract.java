package it.usi.xframe.xas.bfimpl.a2psms.configuration;
/**
 * Define the custom configuration for all providers. 
 */
public abstract class CustomizationAbstract {
	/**
	 * Does the provider support Delivery Report service?
	 * @return true if the provider support the service
	 */
	abstract public boolean isSupportDeliveryReport();
	/**
	 * Does the provider support Mobile Originated service?
	 * @return true if the provider support the service
	 */
	abstract public boolean isSupportMobileOriginated();
	/**
	 * Does the provider support Replace Map service?
	 * @return true if the provider support the service
	 */
	abstract public boolean isSupportReplaceMap();
}