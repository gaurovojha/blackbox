/**
 *
 */
package com.blackbox.ids.core.dto.datatable;

import java.util.List;

/**
 * The <code>SearchResult</code> represents a search result of persistence entities. It contains following data as a
 * result of a search query execution <br/>
 * 1. A list of records fetched by search query whose results might be restricted based on a paging criteria.<br/>
 * 2. The total number of records present in the database for the search query without any paging criteria restriction.
 *
 * @param <T>
 *            Persistence entity type
 */
public class SearchResult<T> {

	/** The total count of persistence entities in the database. Not accounting for filtering. */
    private final Long recordsTotal;

    /** Number of records in the data set, accounting for filtering. */
    private final Long recordsFiltered;

    /** The List of persistence entities returned by the search query. */
    private final List<T> items;

	/**
     * Creates a <code>Tuple</code> instance with provided parameters.
     *
     * @param count
     *            The total count of persistence entities
     * @param items
     *            The List of persistence entities
     */
    public SearchResult(final Long recordsTotal, final List<T> items) {
    	this.recordsTotal = recordsTotal;
    	this.recordsFiltered = recordsTotal;
        this.items = items;
    }

    public SearchResult(final Long recordsTotal, final Long recordsFiltered, final List<T> items) {
    	this.recordsTotal = recordsTotal;
    	this.recordsFiltered = recordsFiltered;
        this.items = items;
    }

    /*- --------------------------------- getters -- */
    public Long getRecordsTotal() {
		return recordsTotal;
	}

	public Long getRecordsFiltered() {
		return recordsFiltered;
	}

	public List<T> getItems() {
		return items;
	}

}
