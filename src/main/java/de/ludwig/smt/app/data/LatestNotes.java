package de.ludwig.smt.app.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.ludwig.jodd.JsonUtils;

public class LatestNotes implements Serializable
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -5930130128590005046L;

	private LinkedList<String> latest = new LinkedList<>();

	public final void addLatestNote(final String esDocumentId)
	{
		latest.addFirst(esDocumentId);
	}

	public List<String> getLatest()
	{
		return latest;
	}

	public void setLatest(LinkedList<String> latest)
	{
		this.latest = latest;
	}

	public static String toJson(LatestNotes latestNotes)
	{
		return JsonUtils.toJson(latestNotes);
	}

	public static LatestNotes fromJson(String json)
	{
		return JsonUtils.fromJson(json, LatestNotes.class);
	}
}
