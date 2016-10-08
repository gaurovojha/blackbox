/**
 *
 */
package com.blackbox.ids.ui.tags;

import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.tagext.TagSupport;

import com.blackbox.ids.core.util.BlackboxCollectionUtil;

/**
 * The {@code BlackboxUtilTag} serves as a taglib, exposing general utility functions.
 *
 * @author ajay2258
 */
public class BlackboxUtilTag extends TagSupport {

	/** The serial version UID. */
	private static final long serialVersionUID = -4270473261520410230L;

	/**
	 * Returns true if this collection contains the specified element. More formally, returns true if and only if this
	 * collection contains at least one element e such that (o==null ? e==null : o.equals(e)).
	 * <p/>
	 * The method returns {@code false} if either of collection or element is null.
	 *
	 * @param collection
	 *            collection to be inspected
	 * @param element
	 *            whose presence in this collection is to be tested
	 *
	 * @return {@code true} if this collection contains the specified element.
	 */
	public static boolean contains(final Collection<Object> collection, final Object element) {
		return (collection != null) && (collection.contains(element) || collection.contains(String.valueOf(element)));
	}

	/**
	 * The method sees whether the given collection contains the given string value. The search is case insensitive.
	 * <p/>
	 * Method is {@code null} safe and returns {@code false} if either of the parameters is {@code null}.
	 *
	 * @param collection
	 *            Collection containing the input strings.
	 * @param element
	 *            Value to check for existence in given collection.
	 * @return {@code true} if given {@code element} exists in given collection; {@code false} otherwise.
	 */
	public static boolean containsIgnoreCase(final Collection<String> collection, final String element) {
		return BlackboxCollectionUtil.containsIgnoreCase(collection, element);
	}

	/**
	 * The method sees whether the given map contains the given key. The search is case insensitive.
	 * <p/>
	 * Method is {@code null} safe and returns {@code false} if either of the parameters is {@code null}.
	 *
	 * @param map
	 *            map containing the input strings.
	 * @param key
	 *            key to check for existence in given map.
	 * @return {@code true} if given {@code key} exists in given map; {@code false} otherwise.
	 */
	public static boolean containsKey(final Map<String, String> map, final String key) {
		return BlackboxCollectionUtil.containsKey(map, key);
	}

}
