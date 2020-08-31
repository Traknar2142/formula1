package ua.com.foxminded.task6.formula1;

public class Abbreviations {    
    private String abbreviation;
    private String racer;
    private String car;
    private String time;
    
    public Abbreviations(String stringLine) {
        String[] racerInfo = stringLine.split("_");
        abbreviation = racerInfo[0] ;
        racer = racerInfo[1];
        car = racerInfo[2];
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getRacer() {
        return racer;
    }

    public String getCar() {
        return car;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
}
