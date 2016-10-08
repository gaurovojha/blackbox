/**
 *
 */
package com.blackbox.ids.core.dto.user;

import com.mysema.query.annotations.QueryProjection;

/**
 * Final class {@code UserAccessDTO} contains the flags, a particular user has been granted access to.
 *
 * @author ajay2258
 */
public final class UserAccessDTO {

	public final Long id;

	public final boolean enabled;

	public final boolean locked;

	public final boolean active;

	@QueryProjection
	public UserAccessDTO(final Long id, final boolean enabled, final Boolean locked, final boolean active) {
		super();
		this.id = id;
		this.enabled = enabled;
		this.locked = locked == null ? false : locked;
		this.active = active;
	}

}
