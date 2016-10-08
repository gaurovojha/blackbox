package com.blackbox.ids.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.util.StringUtils;

import com.blackbox.ids.core.ApplicationException;

/**
 * Blackbox common utility class for collections.
 *
 * @author ajay2258
 */
public class BlackboxCollectionUtil {

	/**
	 * <code>BlackboxCollectionUtil</code> is a utility class which must not be instantiated. Use static methods of
	 * class to perform operations.
	 *
	 * @throws InstantiationException
	 *             if constructor is called for instantiation
	 */
	private BlackboxCollectionUtil() throws InstantiationException {
		throw new InstantiationException("Static class: must not be instantiated.");
	}

	/**
	 * This methods creates a new instance of specified class against each value in given
	 * <code>List&ltLong&gt ids</code>, using default implicit constructor. Then it sets the <code>Long id</code>
	 * attribute of the newly created instance.
	 *
	 * <p>
	 * <b>*Prerequisites</b>:
	 *
	 * <pre>
	 * - This method should have access to the definition of specified class.
	 * - Specified class should be instanceable using default implicit constructor.
	 * - The class must have property named <code>id</code> of type <code>Long</code>.
	 * </pre>
	 *
	 * </p>
	 *
	 * @param clazz
	 *            The class whose set of objects is to be created.
	 * @param ids
	 *            List of IDs. A new object is instantiated against each ID in list.
	 * @param <T>
	 *            type variable for generic type
	 *
	 * @return If everything goes fine, it return the set of objects of specified class; <code>null</code> otherwise.
	 *
	 * @throws ApplicationException
	 *             A wrapper exception if some error occurs during operation.
	 */
	@Deprecated
	public static <T extends Object> Set<T> createObjectSet(final Class<T> clazz, final List<Long> ids)
			throws ApplicationException {
		if (CollectionUtils.isNotEmpty(ids)) {
			Set<T> objectSet = new HashSet<T>(ids.size());
			try {
				for (final Long id : ids) {
					T object = clazz.newInstance();
					BeanUtils.setProperty(object, "id", id);
					objectSet.add(object);
				}
				return objectSet;
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new ApplicationException(e);
			}
		}
		return null;
	}

	/**
	 * The method extracts the list of IDs i.e. unique identifiers for the entity classes from the provided set of
	 * entity instances.
	 * <p>
	 * <b>*Prerequisites</b>:
	 *
	 * <pre>
	 * - This method should have access to the definition of specified class.
	 * - Id of object should be of type <code>Long</code>.
	 * - The class must have a method named <code>getId()</code> of return type <code>Long</code>.
	 * </pre>
	 *
	 * </p>
	 *
	 * @param objectSet
	 *            Set of entity instance. It is supposed that method <code><b>public Long getId();</b></code> is defined
	 *            for the object.
	 * @param <T>
	 *            type variable for generic type
	 * @return A list containing IDs for all provided objects.
	 *
	 * @throws ApplicationException
	 *             A wrapper exception if some error occurs during operation.
	 */
	@Deprecated
	public static <T extends Object> List<Long> getIds(final Set<T> objectSet) throws ApplicationException {
		if (CollectionUtils.isNotEmpty(objectSet)) {
			try {
				List<Long> ids = new ArrayList<Long>(objectSet.size());
				for (T object : objectSet) {
					ids.add(Long.parseLong(BeanUtils.getProperty(object, "id")));
				}
				return ids;
			} catch (NumberFormatException | IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				throw new ApplicationException(e);
			}
		}

		return null;
	}

	/**
	 * Checks whether the passed {@link Collection collection} contains duplicate elements.
	 *
	 * @param <T>
	 *            type variable for generic type
	 * @param collection
	 *            A list of objects to be checked for duplicate elements.
	 * @return <code>true</code>, if any duplicate element is found in the list.
	 */
	public static <T extends Object> boolean containsDuplicates(final Collection<T> collection) {
		final Set<T> set = new HashSet<T>();
		for (T element : collection) {
			if (!set.add(element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the passed {@code stringList} contains duplicate strings. The check is case-insensitive.
	 *
	 * @param stringList
	 *            List of strings to be checked for duplicate case-insensitive strings.
	 * @return {@code true} if any duplicate string is found in the list; {@code false} otherwise.
	 */
	public static boolean containsDuplicatesIgnoreCase(final List<String> stringList) {
		boolean status = false;
		if (CollectionUtils.isNotEmpty(stringList) && (stringList.size() > 1)) {
			Set<String> stringSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			stringSet.addAll(stringList);
			status = stringSet.size() != stringList.size();
		}
		return status;
	}

	/**
	 * The method sees whether the given collection contains the given string value. The search is case insensitive.
	 * <p/>
	 * Method is {@code null} safe and returns {@code false} if either of the parameters is {@code null}.
	 *
	 * @param stringCollection
	 *            Collection containing the input strings.
	 * @param str
	 *            Value to check for existence in given collection.
	 * @return {@code true} if given {@code str} exists in given collection; {@code false} otherwise.
	 */
	public static boolean containsIgnoreCase(final Collection<String> stringCollection, final String str) {
		boolean status = false;
		if (CollectionUtils.isNotEmpty(stringCollection) && !StringUtils.isEmpty(str)) {
			Set<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			set.addAll(stringCollection);
			status = set.contains(str);
		}
		return status;

	}

	/**
	 * The method sees whether the given Map contains the given key. The search is case insensitive.
	 * <p/>
	 * Method is {@code null} safe and returns {@code false} if either of the parameters is {@code null}.
	 *
	 * @param stringCollection
	 *            Collection containing the input strings.
	 * @param key
	 *            Value to check for existence in given collection.
	 * @return {@code true} if given {@code key} exists in given collection; {@code false} otherwise.
	 */
	public static boolean containsKey(final Map<String, String> stringMap, final String key) {
		boolean status = false;
		if (MapUtils.isNotEmpty(stringMap) && !StringUtils.isEmpty(key)) {
			Set<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			set.addAll(stringMap.keySet());
			status = set.contains(key);
		}
		return status;

	}

	/**
	 * The method {@link Transformer#transform(Object) transforms} every string in given collection to lower case.
	 *
	 * @param strCollection
	 *            String collection to be converted into lower case
	 */
	public static void toLowerCase(Collection<String> strCollection) {

		CollectionUtils.transform(strCollection, new Transformer<String, String>() {
			@Override
			public String transform(String input) {
				return (input == null) ? null : input.toLowerCase();
			}
		});
	}

	/**
	 * Splits collection into smaller lists and returns list of that collection. These lists are backed objects and so
	 * change in any of source or result lists causes changes in other too.
	 *
	 * @param <T>
	 *            the generic type
	 * @param bigCollection
	 *            the collection to be split
	 * @param maxBatchSize
	 *            the max batch size
	 * @return List of smaller lists.
	 */
	public static <T> List<List<T>> split(List<T> bigCollection, int maxBatchSize) {
		List<List<T>> result = new ArrayList<List<T>>();

		if (CollectionUtils.isEmpty(bigCollection)) {
			// return empty list
		} else if (bigCollection.size() < maxBatchSize) {
			result.add(bigCollection);
		} else {
			for (int i = 0; (i + maxBatchSize) <= bigCollection.size(); i = i + maxBatchSize) {
				result.add(bigCollection.subList(i, i + maxBatchSize));
			}

			if (bigCollection.size() % maxBatchSize > 0) {
				result.add(bigCollection.subList(bigCollection.size() / maxBatchSize * maxBatchSize,
						bigCollection.size()));
			}
		}

		return result;
	}

	/**
	 * The method returns an array containing all of the elements in this list in proper sequence (from first to last
	 * element);
	 *
	 * @param strList
	 *            Source list to be returned as array (must not be {@code null}).
	 * @return Equivalent string array.
	 * @throws NullPointerException
	 *             if source list given is {@code null}.
	 */
	public static String[] toArray(final List<String> strList) {
		/*- Un-comment below with JRE-1.8 */
		return strList.stream().toArray(String[]::new);
		// return strList.toArray(new String[strList.size()]);
	}

	/**
	 * Returns List of trimmed string sperated from the passed delimeter.
	 *
	 * @param stringVal
	 *            the string val
	 * @param delimeter
	 *            the delimeter
	 * @return List of string
	 */
	public static List<String> getListFromStringSeperatedByDelimeter(@Nonnull final String stringVal,
			@Nonnull final String delimeter) {
		String[] strArr = stringVal.split(delimeter);
		List<String> list = new ArrayList<String>();
		for (String str : strArr) {
			list.add(str.trim());
		}
		return list;
	}

}
