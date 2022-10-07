package tw.zerojudge.Factories;

import tw.zerojudge.Tables.School;

public class SchoolFactory extends SuperFactory<Object> {


	public static School getNullSchool() {
		return new School();
	}


}
