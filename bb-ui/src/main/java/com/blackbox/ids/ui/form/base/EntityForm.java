package com.blackbox.ids.ui.form.base;

/**
 * Interface type {@code EntityForm} relates a particular UI form object to associated entity type.
 *
 * @author ajay2258
 *
 * @param <E> Type of entity class
 */
public interface EntityForm<E> {

	/** Conversion from the form object to associated entity instance. */
	E toEntity();

	/** Populates this form attributes with given entity object details. */
	void load(final E object);

}
