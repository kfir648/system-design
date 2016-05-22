package logic.classes;

public class Date implements Cloneable, Comparable<Date> {

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
		if (day <= 31)
			if (day > 0)
				this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		if (month < 13)
			if (month > 0)
				this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		if (year >= 2016)
			this.year = year;
	}

	public String name() {
		return day + "/" + month + "/" + year;
	}

	public String formatDate() {
		return String.format("%d,%d,%d", day, month, year);
	}

	public static Date getDateFromString(String str) {
		String[] date = str.split(",");
		int day = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int year = Integer.parseInt(date[2]);
		return new Date(day, month, year);
	}

	@Override
	public Date clone() {
		return new Date(day, month, year);
	}

	@Override
	public String toString() {
		return day + "/" + month + "/" + year;
	}

	public Date next() {
		int newMonth;
		int newYear;
		if (this.month == 12) {
			newMonth = 1;
			newYear = this.year + 1;
		} else {
			newMonth = this.month + 1;
			newYear = this.year;
		}
		Date next = new Date(this.day, newMonth, newYear);
		return next;

	}

	public int howManyMonths(Date date) {
		int numOfMonths = 0;
		while (this.compareTo(date) < 0) {
			numOfMonths++;
		}
		return numOfMonths;
	}

	@Override
	public int compareTo(Date o) {
		int rs = year - o.year;
		if (rs != 0)
			return rs;
		rs = month - o.month;
		if (rs != 0)
			return rs;
		rs = day - o.day;
		return rs;
	}

	@SuppressWarnings("deprecation")
	public static Date getNow() {
		java.util.Date calendar = new java.util.Date(System.currentTimeMillis());
		return new Date(calendar.getDay(), calendar.getMonth(), calendar.getYear());
	}

}
