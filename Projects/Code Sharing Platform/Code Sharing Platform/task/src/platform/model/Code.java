package platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@JsonDeserialize
@JsonSerialize
@Entity
public class Code implements AutoCloseable {

    @JsonIgnore
    public static final String DATE_FORMATTER = "yyyy/MM/dd HH:mm:ss.SSS";

    @Id
    @Column(unique = true)
    @JsonIgnore
    private String id = UUID.randomUUID().toString();
    private String code;
    private String date;
    private Integer views;
    private Integer time;
    @JsonIgnore
    private Integer timeOut;
    @JsonIgnore
    private Boolean restrictedViews;

    public Code() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMATTER);
        this.date = formatter.format(new Date());
        this.code = new String();
        this.time = 0;
        this.views = 0;
        this.timeOut = 0;
        this.restrictedViews = false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer numberOfViews) {

        this.views = numberOfViews;
        if (numberOfViews > 0) {
            restrictedViews = true;
        }
    }

    public String getId() {
        return id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer expiresAfter) {
        this.time = expiresAfter;
        this.timeOut = expiresAfter;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public void setOneVisit() {
        this.views = views - 1 >= 0 ? views - 1 : 0;
    }

    public void subtractTimeDelta() {
        this.time = this.time.equals(0) ? 0 :
            timeOut - (int) ChronoUnit.SECONDS.between(getTimestamp(), LocalDateTime.now());
    }

    public LocalDateTime getTimestamp() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return LocalDateTime.parse(this.date, dateFormatter);
    }

    public String toString() {
        return String.format("{" +
                                 "\n\t\"code\": \"%s\"," +
                                 "\n\t\"date\": \"%s\"," +
                                 "\n\t\"time\": %d," +
                                 "\n\t\"views\": %s" +
                                 "\n}", code, date, time, views);
    }

    public boolean isRestrictedCompletely() {
        return !timeOut.equals(0) || !views.equals(0) || this.restrictedViews;
    }

    public boolean isViewRestricted() {
        return this.restrictedViews;
    }

    public boolean isStillValid() {
        int timeDelta = (int) ChronoUnit.SECONDS.between(getTimestamp(), LocalDateTime.now());
        boolean isValid = timeOut != 0 && timeDelta >= timeOut ? false : true;
        return restrictedViews == true && views == 0 ? false : isValid;
    }

    @Override
    public void close() throws Exception {
    }
}
