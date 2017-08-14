package smt.model;

import java.io.Serializable;

/**
 * Representing an elasticsearch Hit, with basic information like document id
 * and the document itself. A document is a smarttrash object like a flow or a
 * note.
 * 
 * @author daniel
 *
 */
public class Hit<S> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3041429106084980828L;

	private String documentId;

	private S document;
	
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public S getDocument() {
		return document;
	}

	public void setDocument(S document) {
		this.document = document;
	}
}
