package smt.req.frontend.tec;

import java.util.ArrayList;
import java.util.List;

import jodd.petite.meta.PetiteBean;
import smt.rdd.Requirement;

/**
 * Abstraction of services that provides gui related data.
 * @author Daniel
 *
 */
@Requirement
@PetiteBean
public class OverviewDataService
{
	public final List<MenuEntry> createFlowOverviewMenu()
	{
		final List<MenuEntry> entries = new ArrayList<>();
		entries.add(new MenuEntry("/createFlow", "menu.createFlow"));
		return entries;
	}
}
