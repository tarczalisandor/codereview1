/*
 * Copyright (C) 2008, 2014 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 04. January 2008 by Joerg Schaible
 */
package it.usi.xframe.xas.util.json;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import eu.unicredit.xframe.slf.UUID;

/**
 * Converts a {@link UUID} to a string.
 * 
 * Register with
 * 
 *  xstream.registerConverter(new UUIDConverter());
 * 
 *  Copied from eu.unicredit.xframe.util.UUIDConverter.
 *  
 */
public class UUIDConverter extends AbstractSingleValueConverter {

	/**
	 * Is the class convertible.
	 * 
	 * @param type the class of the object to convert.
	 * @return true if the class is a slf.UUID.
	 */
	public final boolean canConvert(final Class type) {
		return type.equals(UUID.class);
	}

	/**
	 * Convert a string to a UUID.
	 * 
	 * @param str the UUID string representation.
	 * @return the UUID object.
	 */
	public final Object fromString(final String str) {
		try {
			return UUID.fromString(str);
		} catch (final IllegalArgumentException e) {
			throw new ConversionException("Cannot create UUID instance", e);
		}
	}
}
