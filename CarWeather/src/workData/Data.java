package workData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	
	public static int fasciaOraria(String data){
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		try {
			Date zero = sdf.parse("000000");
			Date sei = sdf.parse("060000");
			Date mezzogiorno = sdf.parse("120000");
			Date diciotto = sdf.parse("180000");
			Date mezzanotte = sdf.parse("240000");
			Date d = sdf.parse(data.substring(8));
			//System.out.println(d);
			if (d.equals(zero) || (d.after(zero) && d.before(sei)))
				return 0;
			if (d.equals(sei) || (d.after(sei) && d.before(mezzogiorno)))
				return 1;
			if (d.equals(mezzogiorno) || (d.after(mezzogiorno) && d.before(diciotto)))
				return 2;
			if (d.equals(diciotto) || (d.after(diciotto) && d.before(mezzanotte)))
				return 3;
			else
				return -1;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Data.isFestivity("20150314125559"));
		System.out.println(Data.fasciaOraria("20150314125559"));
	}

}
