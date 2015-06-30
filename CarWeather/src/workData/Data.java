package workData;

import java.util.Calendar;

public class Data {
	
	public static boolean isFestivity(String data) {
		
		Calendar aprile6 = Calendar.getInstance();
		aprile6.set(2015, 3, 6);
		
		Calendar aprile25 = Calendar.getInstance();
		aprile25.set(2015, 3, 25);
		
		Calendar maggio1 = Calendar.getInstance();
		maggio1.set(2015, 4, 1);
		
		
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Integer.parseInt(data.substring(0, 4)), 
	    		Integer.parseInt(data.substring(4, 6))-1, 
	    		Integer.parseInt(data.substring(6, 8)));

	    if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
	    		calendar.equals(aprile6) || calendar.equals(aprile25) ||
	    		calendar.equals(maggio1)) {
	        return true;
	    }
	    return false;
	}
	
	public static void main(String[] args) {
		System.out.println(Data.isFestivity("20150501"));
	}

}
