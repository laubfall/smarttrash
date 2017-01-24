package de.ludwig.jodd.proxetta.samplebeans;

import jodd.petite.meta.PetiteBean;

@PetiteBean
public abstract class AppLogBean1_1<D>
{
	public abstract void test01();
	
	/**
	 * This is like NoteService.
	 * @param something does not matter.
	 * @return does not matter.
	 */
	public abstract D test02(D something);
	
	public abstract void test03(D something);
}
