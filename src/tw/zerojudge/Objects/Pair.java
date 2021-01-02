package tw.zerojudge.Objects;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Utils.Utils;

public class Pair<L, R> implements Map.Entry<L, R> {
	ObjectMapper mapper = new ObjectMapper(); 
	private final L left;
	private R right;

	private Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public static <L, R> Pair<L, R> create(L left, R right) {
		return new Pair<L, R>(left, right);
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	@Override
	public L getKey() {
		return left;
	}

	@Override
	public R getValue() {
		return right;
	}

	@Override
	public R setValue(R value) {
		R old = this.right;
		this.right = value;
		return old;
	}

	@Override
	public String toString() {
		return "(" + this.getLeft() + ", " + this.getRight() + ")";

//		if (this.right instanceof String) {
//			return this.getValue().toString();
//		} else if (this.right instanceof Boolean) {
//			return String.valueOf(this.getValue());
//		} else if (this.right instanceof Integer) {
//			return String.valueOf(this.getValue());
//		} else if (this.right instanceof Double) {
//			return String.valueOf(this.getValue());
//		} else if (this.right instanceof LinkedHashSet) {
//			return new Utils().LinkedHashSet2CSV((LinkedHashSet<String>) this.right);
//		} else {
//			try {
//				return mapper.writeValueAsString(this.right);
//			} catch (JsonGenerationException e) {
//				e.printStackTrace();
//			} catch (JsonMappingException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return this.getValue().toString();
//		}
	}
}
