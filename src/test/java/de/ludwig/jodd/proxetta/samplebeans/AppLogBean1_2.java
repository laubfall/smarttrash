package de.ludwig.jodd.proxetta.samplebeans;

import de.ludwig.rdd.Requirement;
import jodd.petite.meta.PetiteBean;

@PetiteBean
@Requirement
public class AppLogBean1_2 extends AppLogBean1_1<String>
{

	@Override
	public void test01()
	{
		
	}

	@Override
	public String test02(String something)
	{
		return something;
	}

	@Override
	public void test03(String something)
	{
		
	}
}
