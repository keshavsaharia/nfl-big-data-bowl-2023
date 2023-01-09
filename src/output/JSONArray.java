package output;

public class JSONArray {
	private StringBuilder builder;
	private boolean empty = true;
	
	public JSONArray() {
		builder = new StringBuilder();
	}
	
	private void addComma() {
		// First call changes boolean switch
		if (empty) {
			empty = false;
		}
		// Subsequent calls append comma
		else builder.append(',');
	}
	
	public JSONArray add(int value) {
		addComma();
		builder.append(value);
		return this;
	}
	
	public JSONArray add(float value) {
		addComma();
		builder.append(value);
		return this;
	}
	
	public JSONArray add(double value) {
		addComma();
		builder.append(value);
		return this;
	}
	
	public JSONArray add(JSONObject value) {
		addComma();
		builder.append(value.toString());
		return this;
	}
	
	public JSONArray add(String value) {
		addComma();
		builder.append('"').append(value).append('"');
		return this;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append('[').append(builder.toString()).append(']');
		return str.toString();
	}
}
