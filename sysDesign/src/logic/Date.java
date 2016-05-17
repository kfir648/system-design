package logic;

public class Date implements Cloneable , Comparable<Date>{

	int day;
	int month;
	int year;

	public Date(int day, int month, int year) {
		setDay(day);
		setMonth(month);
		setYear(year);
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		if(day<=31)
			if(day>0)
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		if (month<13)
			if(month>0)
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		if(year>=2016)
		this.year = year;
	}
	
	public String  name() {
		return day + "/" + month + "/" + year;
	}
	
	public String formatDate() {
		return  String.format("%2d%2d%4d", day , month , year);
	}
	
	public static Date getDateFromString(String str){
		int day =Integer.parseInt("" + str.charAt(0) + (str.charAt(1)));
		int month =Integer.parseInt("" + str.charAt(2) + (str.charAt(3)));
		int year =Integer.parseInt("" + str.charAt(4) + (str.charAt(5))+ str.charAt(6) + (str.charAt(7)));
		return new Date(day,month,year);
	}
	
	@Override
	public Date clone()
	{
		return new Date(day , month , year);
	}

	@Override
	public String toString() {
		return day + "/" + month + "/" + year;
	}

	@Override
	public int compareTo(Date o) {
		int rs = year - o.year;
		if(rs != 0)
			return rs;
		rs = month - o.month;
		if(rs != 0)
			return rs;
		rs = day - o.day;
		return rs;
	}
	
	@SuppressWarnings("deprecation")
	public static Date getNow() {
		java.util.Date calendar = new java.util.Date (System.currentTimeMillis());
		return new Date(calendar.getDay(), calendar.getMonth(), calendar.getYear());
	}
}
