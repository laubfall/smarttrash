package de.ludwig.smt.req.frontend.tec;

import java.util.List;

import jodd.vtor.Violation;
import spark.ModelAndView;

/**
 * Base functionality for classes that want to provide editing and creating function for Elasticsearch document.
 * 
 * @author Daniel
 *
 */
public abstract class EditCreateDocument<D>
{
	public abstract void showEditCreateDocument();

	protected abstract D loadDocumentFillForm();

	public abstract void saveDocument();

	public abstract ModelAndView displayValidationMessage(List<Violation> violations);
	
	public boolean isNewDocument() {
		return false;
	}
}
