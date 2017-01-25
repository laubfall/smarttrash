package de.ludwig.smt.app.data;

import de.ludwig.smt.app.config.FlowId;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.vtor.constraint.NotNull;

/**
 * A Note.
 * 
 * @author daniel
 *
 */
public class Note
{
	/**
	 * Content of this note.
	 */
	private String content;
	
	/**
	 * The flow this note belongs to.
	 */
	@NotNull
	private FlowId flow;

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public FlowId getFlow()
	{
		return flow;
	}

	public void setFlow(FlowId flow)
	{
		this.flow = flow;
	}
	
	public static String toJson(final Note note)
	{
		JsonSerializer js = new JsonSerializer();
		return js.serialize(note);
	}

	public static Note fromJson(String ref)
	{
		JsonParser jsonParser = JsonParser.create();
		return jsonParser.parse(ref, Note.class);
	}
}
