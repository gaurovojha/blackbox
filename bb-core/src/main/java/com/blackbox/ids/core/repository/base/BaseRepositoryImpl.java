/**
 *
 */
package com.blackbox.ids.core.repository.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <code>BaseRepositoryImpl</code> provides implementation of {@link BaseRepository}.
 *
 * @author ajay2258
 *
 * @param <T>
 *            Persistence entity type
 * @param <ID>
 *            Data type of persistence entity's primary key
 */
@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements
    BaseRepository<T, ID> {

	private static final long serialVersionUID = 393197475498597437L;

		/**
     * Creates default implementation of {@link BaseRepository} for provided persistence entity.
     *
     * @param entityClass
     *            Class of Persistence entity
     * @param entityManager
     *            {@link EntityManager} instance used to interact with persistence context
     */
    @SuppressWarnings("unchecked")
    public BaseRepositoryImpl(final Class<T> entityClass, final EntityManager entityManager) {
        super((JpaEntityInformation<T, ID>) JpaEntityInformationSupport.getEntityInformation(entityClass, entityManager),
            entityManager);
    }

}