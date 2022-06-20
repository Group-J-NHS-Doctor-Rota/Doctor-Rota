import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Enumeration;

public class Rules {
    private ArrayList<JuniorDoctor> doctors;
    private boolean[] someoneOnShift;
    private LocalDate startDate;
    private int numberOfDays;

    public boolean Rules(ArrayList<JuniorDoctor> doctors, LocalDate startDate, int numberOfDays){
        this.doctors = doctors;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
        someoneOnShift = new boolean[numberOfDays];

        return allDaysCovered();
    }

    private boolean allDaysCovered(){
        for(JuniorDoctor doctor: doctors){
            Enumeration<LocalDate> day = doctor.shifts.keys();
            while (day.hasMoreElements()){
                LocalDate each = day.nextElement();
                Enum e = doctor.getShifts(each);
                if(e.equals(Shifts.DAYON) || e.equals(Shifts.NIGHT)){
                    long daysBetween = ChronoUnit.DAYS.between(startDate, each);
                    someoneOnShift[(int)daysBetween] = true;
                }
            }
        }

        int falseCounter = 0;
        for (int i=0; i<numberOfDays; i++){
            if(!someoneOnShift[i]){
                falseCounter++;
            }
        }
        System.out.println("number of days not covered = "+falseCounter);
        return falseCounter == 0;
    }

}
