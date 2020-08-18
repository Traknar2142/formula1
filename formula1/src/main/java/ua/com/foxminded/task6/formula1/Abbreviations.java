package ua.com.foxminded.task6.formula1;

public class Abbreviations {    
    private String abbreviation;
    private String racer;
    private String car;
    
    public Abbreviations(String abb) {
        String abbMass[] = abb.split("_");
        abbreviation = abbMass[0] ;
        racer = abbMass[1];
        car = abbMass[2];
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
}
