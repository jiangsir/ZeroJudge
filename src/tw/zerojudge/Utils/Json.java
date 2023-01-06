package tw.zerojudge.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.zerojudge.Tables.User;

/**
 * @author jiangsir
 * 
 */

public class Json {

	/**
	 * getList provides a List representation of the JSON Object
	 * 
	 * @param jsonResponse
	 *            The JSON array string
	 * @return List of JSONObject.
	 **/
	protected List<Object> getList(String jsonResponse) throws Exception {
		List<Object> listResponse = new ArrayList<Object>();
		if (jsonResponse.startsWith("[")) {
			JSONArray jsonArray = new JSONArray(jsonResponse);
			listResponse = toJavaList(jsonArray);
		} else {
			throw new Exception("MalFormed JSON Array Response.");
		}

		return listResponse;
	}

	/**
	 * getMap provides a Map representation of the JSON Object
	 * 
	 * @param jsonResponse
	 *            The JSON object string
	 * @return Map of JSONObject.
	 **/
	protected Map<String, Object> getMap(String jsonResponse) throws Exception {
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		if (jsonResponse.startsWith("{")) {
			JSONObject jsonObj = new JSONObject(jsonResponse);
			mapResponse = toJavaMap(jsonObj);
		} else {
			throw new Exception("MalFormed JSON Array Response.");
		}
		return mapResponse;
	}

	/**
	 * toJavaMap converts the JSONObject into a Java Map
	 * 
	 * @param o
	 *            JSONObject to be converted to Java Map
	 * @param b
	 *            Java Map to hold converted JSONObject response.
	 * @throws JSONException
	 **/
	public static HashMap<String, Object> toJavaMap(JSONObject o)
			throws JSONException {
		HashMap<String, Object> b = new HashMap<String, Object>();
		Iterator<?> ji = o.keys();
		while (ji.hasNext()) {
			String key = (String) ji.next();
			Object val = o.get(key);
			if (val.getClass() == JSONObject.class) {
				Map<String, Object> sub = new HashMap<String, Object>();
				sub = toJavaMap((JSONObject) val);
				b.put(key, sub);
			} else if (val.getClass() == JSONArray.class) {
				List<Object> l = new ArrayList<Object>();
				JSONArray arr = (JSONArray) val;
				for (int a = 0; a < arr.length(); a++) {
					Map<String, Object> sub = new HashMap<String, Object>();
					Object element = arr.get(a);
					if (element instanceof JSONObject) {
						sub = toJavaMap((JSONObject) element);
						l.add(sub);
					} else {
						l.add(element);
					}
				}
				b.put(key, l);
			} else {
				b.put(key, val);
			}
		}
		return b;
	}

	/**
	 * toJavaList converts JSON's array response into Java's List
	 * 
	 * @param ar
	 *            JSONArray to be converted to Java List
	 * @param ll
	 *            Java List to hold the converted JSONArray response
	 * @throws JSONException
	 **/
	public static List<Object> toJavaList(JSONArray ar) throws JSONException {
		List<Object> ll = new ArrayList<Object>();
		int i = 0;
		while (i < ar.length()) {
			Object val = ar.get(i);
			if (val.getClass() == JSONObject.class) {
				Map<String, Object> sub = new HashMap<String, Object>();
				sub = toJavaMap((JSONObject) val);
				ll.add(sub);
			} else if (val.getClass() == JSONArray.class) {
				List<Object> l = new ArrayList<Object>();
				JSONArray arr = (JSONArray) val;
				for (int a = 0; a < arr.length(); a++) {
					Map<String, Object> sub = new HashMap<String, Object>();
					Object element = arr.get(a);
					if (element instanceof JSONObject) {
						sub = toJavaMap((JSONObject) element);
						ll.add(sub);
					} else {
						ll.add(element);
					}
				}
				l.add(l);
			} else {
				ll.add(val);
			}
			i++;
		}
		return ll;
	}

	public static void main(String[] args) throws JSONException {

		User Mary = new User();
		JSONObject jsonObjectMary = new JSONObject(Mary);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Jacky");
		map.put("age", 30);
		map.put("gender", true);
		JSONObject jsonObjectJacky = new JSONObject(map);
		jsonObjectJacky.put("name", "Jacky2");

		jsonObjectJacky.put("height", 180);

		String jackyJsonString = jsonObjectJacky.toString();
		JSONObject jsonObjectJackyFromString = new JSONObject(jackyJsonString);

		JSONArray jsonArrayNames = jsonObjectJacky.names();

		jsonArrayNames.put("weight");
		jsonArrayNames.put(jsonObjectJacky);

	}
}
