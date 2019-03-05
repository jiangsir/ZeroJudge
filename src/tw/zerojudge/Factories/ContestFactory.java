package tw.zerojudge.Factories;

import java.util.Comparator;

import tw.zerojudge.Tables.Contest;

public class ContestFactory extends SuperFactory<Contest> {

	public static class DESCComparator<T> implements Comparator<Integer> {
		public int compare(Integer i1, Integer i2) {
			if (i1.intValue() > i2.intValue()) {
				return -1;
			} else if (i1.intValue() < i2.intValue()) {
				return 1;
			} else {
				return 0;
			}
		}
	}


	public static Contest getNullcontest() {
		return new Contest();
	}


	//
	//
	//


	/**
	 * 建立及初始化一個 contest
	 * 
	 * @param contest
	 */
	//

}
