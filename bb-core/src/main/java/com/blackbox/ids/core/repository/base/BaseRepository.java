package com.blackbox.ids.core.repository.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <code>BaseRepository</code> is a generic repository for all other JPA business repositories. This is a generic
 * interface which must be extended by all repository interfaces for specific entities.
 *
 * @author ajay2258
 *
 * @param <T>
 *            Persistence entity type
 * @param <ID>
 *            Data type of persistence entity's primary key
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T>,
    Serializable {

}