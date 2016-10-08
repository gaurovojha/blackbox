
/**
 *
 */
package com.blackbox.ids.core.dao.impl.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.repository.base.BaseRepository;
import com.blackbox.ids.core.repository.base.BaseRepositoryImpl;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.path.StringPath;

/**
 * The <code>BaseDaoImpl</code> provides implementation of {@link BaseDAO} for common database CRUD operations for a
 * persistence entity. All DAO implementations for specific entities must extend this class.
 *
 * @author ajay2258
 *
 * @param <T>
 *            Persistence entity type
 * @param <ID>
 *            Data type of persistence entity's primary key
 */
@Repository
public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDAO<T, ID> {

	/** Constant for various numeric validations */
	private static final int ZERO = 0;

	/** The Constant ARCHIEVE_AB_AFTER_DAYS. */
	protected static final String ARCHIEVE_AB_AFTER_DAYS = "archive.agreed.business.after.days";

	/** Instance used to interact with persistence context. */
	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager entityManager;

	/** The base repository parameter. */
	private BaseRepository<T, ID> baseRepository;

	/** Class of Persistence entity */
	private Class<T> entityClass;

	/**
	 * Sets the entity manager.
	 *
	 * @param entityManager
	 *            the new entity manager
	 */
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/** @return the entity manager. */
	protected EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * Sets the generic JPA repository which is used to execute all CRUD operations.
	 *
	 * @param baseRepository
	 *            the baseRepository to set
	 */
	protected void setBaseRepository(final BaseRepository<T, ID> baseRepository) {
		this.baseRepository = baseRepository;
	}

	/**
	 * Associates a generic repository implementation if no repository is associated with this DAO.
	 */
	@PostConstruct
	private void setUp() {
		if (this.baseRepository == null) {
			this.baseRepository = new BaseRepositoryImpl<T, ID>(this.getEntityClass(), this.entityManager);
		}
	}

	@Override
	public <A extends Object> List<List<A>> split(final List<A> inQueryData) {
		return splitCollection(inQueryData, SQL_IN_QUERY_LIMIT);
	}

	@Override
	public <A extends Object> List<List<A>> split(final List<A> inQueryData, final int partitionBy) {
		return splitCollection(inQueryData, partitionBy);
	}

	private <A extends Object> List<List<A>> splitCollection(final List<A> inQueryData, final int partitionBy) {
		List<List<A>> partitions = new ArrayList<List<A>>();
		for (int i = 0; i < inQueryData.size(); i += partitionBy) {
			partitions.add(inQueryData.subList(i, i + Math.min(partitionBy, inQueryData.size() - i)));
		}
		return partitions;
	}

	@Override
	public T persist(final T entity) throws ApplicationException {
		try {
			return this.baseRepository.save(entity);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}
	
	@Override
	public long count() throws ApplicationException {
		try {
			return this.baseRepository.count();
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Iterable<T> persist(final Iterable<T> entities) throws ApplicationException {
		try {
			return this.baseRepository.save(entities);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void delete(final T entity) throws ApplicationException {
		try {
			this.baseRepository.delete(entity);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void delete(final Iterable<T> entities) throws ApplicationException {
		try {
			this.baseRepository.delete(entities);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void delete(final ID primaryKey) throws ApplicationException {
		try {
			this.baseRepository.delete(primaryKey);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void deleteAll() throws ApplicationException {
		try {
			this.baseRepository.deleteAll();
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	/**
	 * Gets the long list from sql id list.This method can be used where we are fetching id list using SQL query.
	 *
	 * @param idList
	 *            the id list
	 * @return the long list from sql id list
	 */
	protected List<Long> getLongListFromSQLIdList(final List<?> idList) {
		List<Long> result = Collections.emptyList();
		if (idList != null && !idList.isEmpty()) {
			result = new ArrayList<Long>(idList.size());
			for (final Object id : idList) {
				if (id instanceof BigDecimal) {
					result.add(((BigDecimal) id).longValue());
				} else if (id instanceof Long) {
					result.add((Long) id);
				}
			}
		}
		return result;
	}

	@Override
	public T find(final ID primaryKey) throws ApplicationException {
		try {
			final T entity = this.baseRepository.findOne(primaryKey);
			if (null == entity) {
				throw new NoResultException();
			}
			return entity;
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public List<T> findAll() throws ApplicationException {
		try {
			return this.baseRepository.findAll();
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public List<T> findAll(final Iterable<ID> ids) throws ApplicationException {
		try {
			return this.baseRepository.findAll(ids);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public List<T> findAll(final boolean ascending, final String... sortProperty) throws ApplicationException {
		try {
			return this.baseRepository.findAll(getSort(ascending, sortProperty));
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	/**
	 * Fetches an entity matching the given {@link Predicate} from database.
	 *
	 * @param predicate
	 *            the {@link Predicate} to be matched
	 * @return the found entity instance or null if the entity does not exist
	 * @throws ApplicationException
	 *             if any error occurs while fetching entity for given predicate
	 */
	protected T findOne(final Predicate predicate) throws ApplicationException {
		try {
			return this.baseRepository.findOne(predicate);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	/**
	 * Fetches all entity instances matching the given {@link Predicate} from database.
	 *
	 * @param predicate
	 *            the {@link Predicate} to be matched
	 * @return List of fetched entity instance
	 * @throws ApplicationException
	 *             if any error occurs while fetching entity collection for given predicate
	 */
	protected List<T> findAll(final Predicate predicate) throws ApplicationException {
		try {
			return (List<T>) this.baseRepository.findAll(predicate);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	/**
	 * Fetches all records of given entity matching the given {@link Predicate} sorted by provided properties and order.
	 *
	 * @param predicate
	 *            the {@link Predicate} to be matched
	 * @param offset
	 *            Position of the first result
	 * @param limit
	 *            Maximum number of records to be fetched
	 * @param ascending
	 *            Sorting order - true if ascending; <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @return {@link SearchResult} of fetched entities
	 * @throws ApplicationException
	 *             if any error occurs while fetching entity collection for given predicate
	 */
	protected SearchResult<T> findAll(final Predicate predicate, final int offset, final int limit,
			final boolean ascending, final String... sortProperty) throws ApplicationException {
		try {
			final Page<T> page = this.baseRepository.findAll(predicate,
					getPageable(offset, limit, ascending, sortProperty));
			return new SearchResult<T>(page.getTotalElements(), page.getContent());
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	/**
	 * Fetches all records of given entity by the given {@link JPAQuery} sorted by provided properties and order.
	 *
	 * @param predicateQuery
	 *            the {@link JPAQuery} to be executed
	 * @param selectEntity
	 *            the {@link Expression} for entity to be fetched
	 * @param offset
	 *            Position of the first result
	 * @param limit
	 *            Maximum number of records to be fetched
	 * @param ascending
	 *            Sorting order - true if ascending; <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @return {@link SearchResult} of fetched entities
	 * @throws ApplicationException
	 *             if any error occurs while fetching entity collection for given query
	 */
	protected SearchResult<T> findAll(final JPAQuery predicateQuery, final Expression<T> selectEntity, final int offset,
			final int limit, final boolean ascending, final ComparableExpressionBase... sortProperty)
					throws ApplicationException {
		List<T> items = new ArrayList<T>();
		Long totalCount = null;
		try {
			boolean fetchList = true;
			/*
			 * If provided limit is greater than 0 that means paginated result needs to be fetched. Fetch total limit of
			 * records that will be fetched by provided query in this case only.
			 */
			if (limit > ZERO) {
				totalCount = predicateQuery.count();
				if (totalCount <= ZERO) {
					fetchList = false;
				}
			}
			if (fetchList) {
				addPageableAndOrderBy(predicateQuery, offset, limit, ascending, sortProperty);
				items = predicateQuery.list(selectEntity);
				// Set totalCount as size of list in case total limit has not been fetched.
				if (totalCount == null) {
					totalCount = new Long(items.size());
				}
			}
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);//, LogMessages.ERR_FETCH_ALL_JPAQUERY, this.getEntityClass());
		}
		return new SearchResult<T>(totalCount, items);
	}

	@Override
	public Session getHibernateSession() {
		return (Session) this.entityManager.getDelegate();
	}

	/**
	 * @return {@link JPAQuery} instance.
	 */
	protected JPAQuery getJPAQuery() {
		return new JPAQuery(this.entityManager);
	}

	/**
	 * Clone the state of the given query to a new JPAQuery instance.
	 *
	 * @return Clone for given JPAQuery.
	 */
	protected JPAQuery cloneJPAQuery(JPAQuery query) {
		return query.clone(this.entityManager);
	}

	/**
	 * Gets the native query.
	 *
	 * @param query
	 *            the query
	 * @return the native query
	 */
	protected Query getNativeQuery(final String query) {
		return entityManager.createNativeQuery(query, getEntityClass());
	}

	/**
	 * Gets the JPA update clause.
	 *
	 * @param entityPath
	 *            the entity path
	 * @return the JPA update clause
	 */
	protected JPAUpdateClause getJPAUpdateClause(final EntityPath<T> entityPath) {
		return new JPAUpdateClause(entityManager, entityPath);
	}

	/**
	 * Gets the JPA implementation of {@code DeleteClause}.
	 *
	 * @param entityPath
	 *            the entity path
	 * @return the JPA delete clause
	 */
	protected JPADeleteClause getJPADeleteClause(final EntityPath<T> entityPath) {
		return new JPADeleteClause(entityManager, entityPath);
	}

	/**
	 * Returns {@link JPAQuery} instance with provided sorting properties. This API can be used by concrete DAO
	 * implementations for getting {@link JPAQuery} instance for executing complex QueryDsl queries.
	 *
	 * @param ascending
	 *            Sorting order - true if ascending <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @return {@link JPAQuery} instance
	 */
	protected JPAQuery getJPAQuery(final boolean ascending, final StringPath... sortProperty) {
		final JPAQuery query = getJPAQuery();
		addOrderBy(query, ascending, sortProperty);
		return query;
	}

	/**
	 * Returns {@link JPAQuery} instance with provided sorting and pagination properties. This API can be used by
	 * concrete DAO implementations for getting {@link JPAQuery} instance for executing complex QueryDsl queries.
	 *
	 * @param offset
	 *            Position of the first result
	 * @param limit
	 *            Maximum number of records to be fetched
	 * @param ascending
	 *            Sorting order - true if ascending; <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @return {@link JPAQuery} instance
	 */
	protected JPAQuery getJPAQuery(final int offset, final int limit, final boolean ascending,
			final StringPath... sortProperty) {
		final JPAQuery query = getJPAQuery(ascending, sortProperty);
		addPageable(query, offset, limit);
		return query;
	}

	/**
	 * This API adds provided sorting and pagination parameters to the given {@link JPAQuery} instance.
	 *
	 * @param query
	 *            the {@link JPAQuery} instance
	 * @param offset
	 *            Position of the first result
	 * @param limit
	 *            Maximum number of records to be fetched
	 * @param ascending
	 *            Sorting order - true if ascending <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 */
	private void addPageableAndOrderBy(final JPAQuery query, final int offset, final int limit, final boolean ascending,
			final ComparableExpressionBase... sortProperty) {
		addOrderBy(query, ascending, sortProperty);
		addPageable(query, offset, limit);
	}

	/**
	 * This API adds provided pagination parameters to the given {@link JPAQuery} instance.
	 *
	 * @param query
	 *            the {@link JPAQuery} instance
	 * @param offset
	 *            Position of the first result
	 * @param limit
	 *            Maximum number of records to be fetched
	 */
	private void addPageable(final JPAQuery query, final int offset, final int limit) {
		if (offset > -1) {
			query.offset(offset);
		}
		if (limit > -1) {
			query.limit(limit);
		}
	}

	/**
	 * This API adds provided sorting parameters to the given {@link JPAQuery} instance.
	 *
	 * @param query
	 *            the {@link JPAQuery} instance
	 * @param ascending
	 *            Sorting order - true if ascending <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @param sortProperty
	 */
	private void addOrderBy(final JPAQuery query, final boolean ascending,
			final ComparableExpressionBase... sortProperty) {
		if (sortProperty != null && sortProperty.length > 0) {
			for (final ComparableExpressionBase orderBy : sortProperty) {
				if (ascending) {
					query.orderBy(orderBy.asc().nullsLast());
				} else {
					query.orderBy(orderBy.desc().nullsLast());
				}
			}
		}
	}

	/**
	 * Returns the total number of entity instances matching the given {@link Predicate}.
	 *
	 * @param predicate
	 *            the {@link Predicate} to be matched
	 * @return the limit of instances
	 * @throws ApplicationException
	 *             if any error occurs while fetching limit of entities
	 */
	protected long count(final Predicate predicate) throws ApplicationException {
		try {
			return this.baseRepository.count(predicate);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public boolean exists(final ID id) throws ApplicationException {
		try {
			return this.baseRepository.exists(id);
		} catch (final PersistenceException e) {
			throw new ApplicationException(e);
		}
	}

	/**
	 * Returns an instance of {@link Sort} to be used with queries provided by
	 * {@link org.springframework.data.repository.PagingAndSortingRepository}.
	 *
	 * @param ascending
	 *            Sorting order - true if ascending <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @return Sort {@link Sort}
	 */
	private Sort getSort(final boolean ascending, final String... sortProperty) {
		Direction direction = Direction.ASC;
		if (!ascending) {
			direction = Direction.DESC;
		}
		return new Sort(direction, sortProperty);
	}

	/**
	 * Returns an instance of {@link Pageable} to be used with queries provided by
	 * {@link org.springframework.data.repository.PagingAndSortingRepository}.
	 *
	 * @param offset
	 *            Position of the first result
	 * @param limit
	 *            Maximum number of records to be fetched
	 * @param ascending
	 *            Sorting order - true if ascending <br/>
	 *            false, otherwise
	 * @param sortProperty
	 *            Property names to be sorted on
	 * @return Pageable {@link Pageable}
	 */
	private Pageable getPageable(final int offset, final int limit, final boolean ascending,
			final String... sortProperty) {
		Direction direction = Direction.ASC;
		if (!ascending) {
			direction = Direction.DESC;
		}
		return new PageRequest(offset, limit, direction, sortProperty);
	}

	/**
	 * Returns the persistence entity's class.
	 *
	 * @return Persistence entity's class
	 */
	private Class<T> getEntityClass() {
		if (this.entityClass == null) {
			final Class<?> targetClass = this.getClass();
			this.entityClass = this.getParameterizedClass(targetClass);
		}
		return this.entityClass;
	}

	/**
	 * Identifies and returns the parameterized class from provided class.
	 *
	 * @param targetClass
	 *            Target class from which entity class need to be identified
	 * @return Persistence entity's class
	 * @throws IllegalArgumentException
	 *             If entity class could not be identified
	 */
	@SuppressWarnings("unchecked")
	private Class<T> getParameterizedClass(final Class<?> targetClass) throws IllegalArgumentException {
		final Type type = targetClass.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			final ParameterizedType paramType = (ParameterizedType) type;
			return (Class<T>) paramType.getActualTypeArguments()[0];
		}
		final Class<?> superClass = targetClass.getSuperclass();
		if (superClass != targetClass) {
			return this.getParameterizedClass(superClass);
		}
		throw new IllegalArgumentException("Could not guess entity class by reflection");
	}

	@Override
	public Long getNextSequence(String sequence) throws ApplicationException {
		String sql = "SELECT NEXT VALUE FOR " + sequence;
		Query queryString = entityManager.createNativeQuery(sql);
		Long seqNo = ((BigInteger) queryString.getSingleResult()).longValue();
		return seqNo;
	}
	
	@Override
	public Long getCurrentSequence(String sequence) throws ApplicationException {
		String sql = "SELECT CURRENT_VALUE FROM SYS.SEQUENCES WHERE NAME = " + sequence;
		Query queryString = entityManager.createNativeQuery(sql);
		Long seqNo = ((BigInteger) queryString.getSingleResult()).longValue();
		return seqNo;
	}
	
}
