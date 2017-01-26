package de.ludwig.jodd.vtor;

import jodd.vtor.Vtor;

/**
 * Vtor with some special smt features.
 * @author Daniel
 *
 */
public class SmtVtor extends Vtor
{
	/**
	 * Use VtorProfiles. Shortcut to {@link Vtor#useProfile(String)}.
	 * @param profiles use these profiles.
	 */
	public final void useProfiles(VtorProfile ...profiles)
	{
		for(VtorProfile vt : profiles) {
			useProfile(vt.name());
		}
	}
}
