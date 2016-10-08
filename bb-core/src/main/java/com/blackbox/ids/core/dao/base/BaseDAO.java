/**
 *
 */
package com.blackbox.ids.core.dao.base;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;

/**
 * The <code>BaseDAO</code> provides an abstraction for common database CRUD operations for a persistence entity. This
 * is a generic interface which must be extended by all DAO interfaces for specific entities. Concrete DAO
 * implementations should extend {@link BaseDaoImpl} rather than implementing this interface directly.
 *
 * @author ajay2258
 *
 * @param <T>
 *            Persistence entity type
 * @param <ID>
 *            Data type of persistence entity's primary key
 */
public interface BaseDAO<T, ID extends Serializable> {

	/** Maximum number of parameters allowed in {@code IN} query. */
	public static final int SQL_IN_QUERY_LIMIT = 1000;

	/**
	 * The method partitions the given list within smaller lists of size {@value #SQL_IN_QUERY_LIMIT}.
	 * <p>
	 * Since the Oracle doesn't accept more than {@value #SQL_IN_QUERY_LIMIT} parameters in an {@code IN} query,
	 * parameters list should be splitted within smaller lists and access predicate (i.e. {@code WHERE} clause) should
	 * be prepared as suggested below:
	 *
	 * <pre>
	 * SELECT ... FROM ... WHERE ...
	 * AND (ep_codes IN (...1000 ep_codes...) OR  ep_codes IN (...2000 ep_codes...)
	 * </pre>
	 *
	 * @param <A>
	 *            the generic type
	 * @param inQueryData
	 *            Original list, to be passed within Oracle {@code IN} query.
	 * @return Smaller partitioned lists.
	 */
	public <A extends Object> List<List<A>> split(final List<A> inQueryData);

	/**
	 * The method partitions the given list within smaller lists of given size.
	 *
	 * @param <A>
	 *            the generic type
	 * @param inQueryData
	 *            Original list, to be passed within Oracle {@code IN} query.
	 * @param partitionBy
	 *            Number, by which given input list is to be partitioned.
	 * @return Smaller partitioned lists.
	 */
	public <A extends Object> List<List<A>> split(final List<A> inQueryData, final int partitionBy);

	/**
	 * Return the underlying provider object for the EntityManager, if available. The result of this method is
	 * implementation specific. The unwrap method is to be preferred for new applications.
	 *
	 * @return underlying provider object for EntityManager
	 */
	public Session getHibernateSession();

	/**
	 * Persists the provided entity in database.
	 *
	 * @param entity
	 *            entity instance
	 * @return Saved entity instance
	 * @throws ApplicationException
	 *             if any error occurs while persisting provided entity
	 */
	public T persist(final T entity) throws ApplicationException;

	/**
	 * Persists the provided entity collection in database.
	 *
	 * @param entities
	 *            Collection of entities
	 * @return Saved entities
	 * @throws ApplicationException
	 *             if any error occurs while persisting provided entity collection
	 */
	Iterable<T> persist(final Iterable<T> entities) throws ApplicationException;

	/**
	 * Deletes the provided entity instance from database.
	 *
	 * @param entity
	 *            entity instance
	 * @throws ApplicationException
	 *             if any error occurs while deleting provided entity
	 */
	public void delete(final T entity) throws ApplicationException;

	/**
	 * Deletes the provided entity collection from database.
	 *
	 * @param entities
	 *            Collection of entities
	 * @throws ApplicationException
	 *             if any error occurs while deleting provided entity collection
	 */
	void delete(final Iterable<T> entities) throws ApplicationException;

	/**
	 * Deletes the entity having provided primary key from database.
	 *
	 * @param primaryKey
	 *            Primary key of entity
	 * @throws ApplicationException
	 *             if any error occurs while deleting entity for provided primary key
	 */
	void delete(final ID primaryKey) throws ApplicationException;

	/**
	 * Deletes all records of given entity from database.
	 *
	 * @throws ApplicationException
	 *             if any error occurs while deleting all records of given entity
	 */
	void deleteAll() throws ApplicationException;

	/**
	 * Fetches an entity by primary key from database.
	 *
	 * @param primaryKey
	 *            Primary key of entity
	 * @return the found entity instance
	 * @throws ApplicationException
	 *             if any error occurs while fetching entity for provided primary key OR in case if the entity does not
	 *             exist
	 */
	public T find(final ID primaryKey) throws ApplicationException;

	/**
	 * Fetches all records of given entity having provided primary keys from database.
	 *
	 * @param ids
	 *            Collection of primary keys
	 * @return List of fetched entity instance
	 * @throws ApplicationException
	 *             if any error occurs while fetching entities for provided primary keys
	 */
	public List<T> findAll(final Iterable<ID> ids) throws ApplicationException;

	/**
	 * Fetches all records of given entity from database.
	 *
	 * @return List of fetched entity instance
	 * @throws ApplicationException
	 *             if any error occurs while fetching all records of given entity
	 */
	public List<T> findAll() throws ApplicationException;

	/**
	 * Fetches all records of given entity from database sorted by provided properties and order.
	 *
	 * @param ascending
	 *            Sorting order - true if ascending; <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @return List of fetched entity instance
	 * @throws ApplicationException
	 *             if any error occurs while fetching records of given entity
	 */
	public List<T> findAll(final boolean ascending, final String... sortProperty) throws ApplicationException;

	/**
	 * Returns whether an entity with provided primary key exists in database.
	 *
	 * @param primaryKey
	 *            Primary key of entity
	 * @return true if an entity with given primary key exists, false otherwise
	 * @throws ApplicationException
	 *             if any error occurs while checking if provided entity exists
	 */
	public boolean exists(final ID primaryKey) throws ApplicationException;

	/**
	 * Returns the next value of the sequence from database.
	 *
	 * @param sequence
	 *            sequence name
	 * @return the next value of the sequence
	 * @throws ApplicationException
	 */
	public Long getNextSequence(String sequence) throws ApplicationException;

	/**
	 * Returns the number of entities available.
	 * @return number of entities
	 * @throws ApplicationException
	 */
	long count() throws ApplicationException;

	/**
	 * Returns current value of sequence
	 * 
	 * @param sequence
	 * @return
	 */
	public Long getCurrentSequence(String sequence);

}
