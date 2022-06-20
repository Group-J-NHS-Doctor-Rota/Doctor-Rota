import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class IndividualRota{
    private static LocalDate startDate;
    private static LocalDate endDate;
    private static int numberOfDays;
    private static ArrayList<LocalDate> nightShiftsTaken;


    public JuniorDoctor IndividualRota(LocalDate begin, LocalDate end, int days, ArrayList<LocalDate> dates){

        startDate = begin;
        endDate = end;
        numberOfDays = days;

        while (true) {
            JuniorDoctor doctor = new JuniorDoctor();
            nightShiftsTaken = dates;
            addDaysToRota(doctor);
            if (correctNumberOfShifts(doctor)) {
                return doctor;
            }
        }
    }
    private static boolean correctNumberOfShifts(JuniorDoctor doctor){

        LocalDate date = startDate;
        int nightShift = 0;
        int dayShift = 0;
        int theatreShift = 0;

        while(!date.isEqual(endDate.plusDays(1))){
            if(doctor.getShifts(date) != null){
                if(doctor.getShifts(date).equals(Shifts.DAYON)){
                    dayShift++;
                }
                if(doctor.getShifts(date).equals(Shifts.NIGHT)){
                    nightShift++;
                }
                if(doctor.getShifts(date).equals(Shifts.THEATRE)){
                    theatreShift++;
                }
            }
            date = date.plusDays(1);
        }
        return dayShift == 11 && nightShift == 11 && theatreShift == 32;
    }


    private static void addDaysToRota(JuniorDoctor doctor) {
        setNights(doctor);
        setLongDays(doctor);
        setTheatre(doctor);
        setOffDays(doctor);
    }


    private static void setLongDays(JuniorDoctor doctor) {
        while(doctor.getLongDays() != 0) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, numberOfDays + 1);
            if(!doctor.shiftTaken(startDate.plusDays(randomNum))){
                doctor.setShifts(startDate.plusDays(randomNum), Shifts.DAYON);
                doctor.reduceLongDays();
            }
        }
    }
    private static void setNights(JuniorDoctor doctor) {
        while(doctor.getNights() >= 0) {
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDays + 1);
            int randomNum = ThreadLocalRandom.current().nextInt(0, numberOfDays + 1);
            //double shift
            if(shiftPairing %2 == 0){
                if(!doctor.shiftTaken(startDate.plusDays(randomNum)) && (numberOfDays - randomNum) >= 4){
                    doctor.setShifts(startDate.plusDays(randomNum), Shifts.NIGHT);
                    doctor.setShifts(startDate.plusDays(randomNum+1), Shifts.NIGHT);
                    doctor.setShifts(startDate.plusDays(randomNum+2), Shifts.NAOFF);
                    doctor.setShifts(startDate.plusDays(randomNum+3), Shifts.NAOFF);
                    doctor.reduceNights();
                    doctor.reduceNights();
                }
            }
            //triple shift
            else{
                if(!doctor.shiftTaken(startDate.plusDays(randomNum)) && (numberOfDays - randomNum) >= 6){
                    doctor.setShifts(startDate.plusDays(randomNum), Shifts.NIGHT);
                    doctor.setShifts(startDate.plusDays(randomNum+1), Shifts.NIGHT);
                    doctor.setShifts(startDate.plusDays(randomNum+2), Shifts.NIGHT);
                    doctor.setShifts(startDate.plusDays(randomNum+3), Shifts.NAOFF);
                    doctor.setShifts(startDate.plusDays(randomNum+4), Shifts.NAOFF);
                    doctor.setShifts(startDate.plusDays(randomNum+5), Shifts.NAOFF);
                    doctor.reduceNights();
                    doctor.reduceNights();
                    doctor.reduceNights();
                }
            }
        }
    }
    private static void setTheatre(JuniorDoctor doctor) {
        while(doctor.getTheatre() != 0) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, numberOfDays + 1);
            if(!doctor.shiftTaken(startDate.plusDays(randomNum))){
                doctor.setShifts(startDate.plusDays(randomNum), Shifts.THEATRE);
                doctor.reduceTheatre();
            }
        }
    }

    private static void setOffDays(JuniorDoctor doctor){
        LocalDate date = startDate;
        while(!date.isEqual(endDate.plusDays(1))){
            if(doctor.getShifts(date) == null){
                doctor.setShifts(date, Shifts.DAYOFF);
            }
            date = date.plusDays(1);
        }
    }
}
