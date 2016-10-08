package com.blackbox.ids.core.repository.webcrawler;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.base.BaseRepository;

/**
 * A DAO for the entity User is simply created by extending the JpaRepository
 * interface provided by spring and custom repository. The following methods are some of the ones
 * available from former interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * Custom interface can be used to define custom query methods that can not be implemented by Spring.
 *
 * @author abhay2566
 */
public interface WebCrawlerJobAuditRepository extends BaseRepository<WebCrawlerJobAudit, Long> {
}