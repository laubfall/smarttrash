package de.ludwig.smt.app.data;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;

import de.ludwig.smt.SmartTrashException;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

/**
 * A Flow.
 * 
 * @author daniel
 *
 */
public class Flow {

	private final String name;
	
	public Flow(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public BytesReference toElasticSearch(){

		try {
			XContentBuilder builder = jsonBuilder()
			    .startObject()
			        .field("name", name)
			    .endObject();
			return builder.bytes();
		} catch (IOException e) {
			throw new SmartTrashException(e);
		}
	}
}
