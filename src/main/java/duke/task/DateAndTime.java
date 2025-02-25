package duke.task;

import duke.DukeException;

/**
 * Converts dd/mm/yyyy XXXX hours to Date of Month, Year, Time in 12-hour format.
 */
public class DateAndTime {
    protected String dateAndTime;
    protected String time;

    protected int date;
    protected int month;
    protected int year;
    protected int hourNumber;
    protected int minNumber;
    protected String timeDay;

    protected String[] monthMappings = new String[]{"", "January", "February", "March", "April", "May", "June",
                                                    "July", "August", "September", "October", "November", "December"};
    protected int[] monthToMaxDays = new int[]{0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public DateAndTime(String dateAndTime) throws DukeException {
        this.dateAndTime = dateAndTime;

        try {
            String[] split = dateAndTime.split(" ");
            String date[] = split[0].split("/");

            this.time = split[1];
            int hours = Integer.parseInt(this.time) / 100;
            int mins = Integer.parseInt(this.time) % 100;

            int dateToCheck = Integer.parseInt(date[0]);
            int monthToCheck = Integer.parseInt(date[1]);
            int yearToCheck = Integer.parseInt(date[2]);

            validateDate(dateToCheck, monthToCheck, yearToCheck);
            validateTime(hours, mins);

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DukeException("Date and time not in correct format.");
        }

    }

    private void validateDate(int date, int month, int year) throws DukeException {
        boolean isDateIncorrect = date > monthToMaxDays[month] || date <= 0;
        boolean isMonthIncorrect = month <= 0 || month > 12;
        boolean isDateIncorrectForNonLeapYear = (month == 2) && (year % 4 == 0) && (date > 28);

        if (isDateIncorrect || isMonthIncorrect || isDateIncorrectForNonLeapYear) {
            throw new DukeException("Incorrect date entered");
        }

        this.date = date;
        this.month = month;
        this.year = year;
    }

    private void validateTime(int hour, int min) throws DukeException {
        if (hour < 12 && hour >= 0) {
            this.hourNumber = (hour == 0) ? 12 : hour;
            this.timeDay = "am";
        } else if (hour < 24 && hour >= 12) {
            this.hourNumber = (hour == 12) ? 12 : hour - 12;
            this.timeDay = "pm";
        } else {
            throw new DukeException("Incorrect time entered");
        }

        if (min < 60 && min >= 0) {
            this.minNumber = min;
        } else {
            throw new DukeException("Incorrect time entered");
        }
    }

    private String getDateSuffix() {
        return (date == 1) ? "st" : (date == 2) ? "nd" : (date == 3) ? "rd" : "th";
    }

    public String getNext(int freq) {
        int nextDate = date + freq;
        int nextMonth = month;
        int nextYear = year;

        int diff = nextDate - monthToMaxDays[month];

        while (diff > 0) {
            nextDate = diff;
            nextMonth += 1;

            if (nextMonth > 12) {
                nextYear += 1;
                nextMonth = 1;
            }

            diff = nextDate - monthToMaxDays[nextMonth];
        }

        return nextDate + "/" + nextMonth + "/" + nextYear + " " + time;
    }

    /**
     * Saves the original dd/mm/yyyy XXXX hours as a string to use when saving these tasks into
     * the hard drive.
     *
     * @return String format in which Date and Time was received in the constructor.
     */
    public String toSave() {
        return dateAndTime;
    }

    @Override
    public String toString() {
        String extra = getDateSuffix();
        String min = String.format("%02d", minNumber);
        return date + extra + " of " + monthMappings[month] + " " + year + ", " + hourNumber + ":" + min+ timeDay;
    }
}
