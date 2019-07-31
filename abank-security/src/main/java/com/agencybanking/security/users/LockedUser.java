/**
 * 
 */
package com.agencybanking.security.users;

import com.agencybanking.core.data.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * holds meta info of locked users
 * 
 * @author dubic
 *
 */
@Getter
@Setter
@Entity
@Table(name = "sec_locked_user")
@SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "SEC_LOCKED_USER_SEQ", allocationSize = 1)
public class LockedUser extends BaseEntity {
	@NotNull(message = "sec.users.user.required")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Size(max = 255, message = "sec.users.lock.reason.size")
	@NotBlank(message = "sec.users.lock.reason.required")
	@Column(name = "reason", nullable = false)
	private String reason;

	@NotBlank(message = "core.app.required")
	@Column(name = "app", nullable = false)
	private String application;

	private LocalDateTime expires;

	public static LockedUser create(User user, String reason, String app, int timeout) {
		LockedUser locked = new LockedUser();
		locked.setUser(user);
		locked.setActive(true);
		locked.setReason(reason);
		locked.setApplication(app);
		if (timeout > 0) {
			locked.setExpires(LocalDateTime.now().plusMinutes(timeout));
		}
		locked.validate();
		return locked;
	}
	
	public boolean isExpired(){
		return LocalDateTime.now().isAfter(expires);
	}

}
