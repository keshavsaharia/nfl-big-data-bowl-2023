package output;

public class JSONObject {
	StringBuilder builder;
	
	public JSONObject() {
		builder = new StringBuilder();
	}
	
	private void addKey(String key) {
		if (builder.length() > 0)
			builder.append(',');
		builder.append('"').append(key).append("\":");
	}
	
	public JSONObject add(String key, int value) {
		addKey(key);
		builder.append(value);
		return this;
	}
	
	public JSONObject add(String key, float value) {
		addKey(key);
		builder.append(value);
		return this;
	}
	
	public JSONObject add(String key, double value) {
		addKey(key);
		builder.append(value);
		return this;
	}
	
	public JSONObject add(String key, String value) {
		addKey(key);
		builder.append('"').append(value).append('"');
		return this;
	}
	
	public JSONObject add(String key, JSONArray value) {
		addKey(key);
		builder.append(value.toString());
		return this;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append('{').append(builder.toString()).append('}');
		return str.toString();
	}
}
