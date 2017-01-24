package com.kagubuzz.spring.social;

import com.kagubuzz.datamodels.hibernate.TBLUser;

/**
 * Simple SecurityContext that stores the currently signed-in connection in a thread local.
 * @author Keith Donald
 */
public final class SecurityContext {

	private static final ThreadLocal<TBLUser> currentUser = new ThreadLocal<TBLUser>();

	public static TBLUser getCurrentUser() {
		TBLUser user = currentUser.get();
		if (user == null) {
			throw new IllegalStateException("No user is currently signed in");
		}
		return user;
	}

	public static void setCurrentUser(TBLUser user) {
		currentUser.set(user);
	}

	public static boolean userSignedIn() {
		return currentUser.get() != null;
	}

	public static void remove() {
		currentUser.remove();
	}

}