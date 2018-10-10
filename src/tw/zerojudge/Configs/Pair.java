package tw.zerojudge.Configs;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Utils.Utils;

public final class Pair<K, V> implements Map.Entry<K, V> {
	ObjectMapper mapper = new ObjectMapper(); 
	private final K key;
	private V value;

	private Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public static <K, V> Pair<K, V> create(K key, V value) {
		return new Pair<K, V>(key, value);
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V old = this.value;
		this.value = value;
		return old;
	}

	@Override
	public String toString() {
		if (this.value instanceof String) {
			return this.getValue().toString();
		} else if (this.value instanceof Boolean) {
			return String.valueOf(this.getValue());
		} else if (this.value instanceof Integer) {
			return String.valueOf(this.getValue());
		} else if (this.value instanceof Double) {
			return String.valueOf(this.getValue());
		} else if (this.value instanceof LinkedHashSet) {
			return new Utils()
					.LinkedHashSet2CSV((LinkedHashSet<String>) this.value);
		} else {
			try {
				return mapper.writeValueAsString(this.value);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return this.getValue().toString();
		}
	}
}
