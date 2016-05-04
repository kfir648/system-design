package sysDesign;

public class Date {

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
	@Override
	public String toString() {
		return  day + "," + month + "," + year ;
	}
	
	public String getDate(){
		return this.toString();
	}
	
}
