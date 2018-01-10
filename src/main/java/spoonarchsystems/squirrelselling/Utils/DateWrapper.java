package spoonarchsystems.squirrelselling.Utils;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DateWrapper {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}