package de.ludwig.smt.app.data;

/**
 * Representing an elasticsearch Hit, with basic information like document id
 * and the document itself. A document is a smarttrash object like a flow or a
 * note.
 * 
 * @author daniel
 *
 */
public class Hit<S> {
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
