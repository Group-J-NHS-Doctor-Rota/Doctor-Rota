import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;

public class Multithreading implements Runnable{
    private final int numberOfDays;
    private final int numOfJuniorDoctors;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ArrayList<JuniorDoctor> totalShifts;
    private final int counter;
    private final ArrayList<LocalDate> nightShiftsTaken;

    public Multithreading(int numOfJuniorDoctors, int numberOfDays, LocalDate startDate, LocalDate endDate, int counter){
        this.numberOfDays = numberOfDays;
        this.numOfJuniorDoctors = numOfJuniorDoctors;
        this.startDate = startDate;
        this.endDate = endDate;
        this.counter = counter;
        totalShifts = new ArrayList<>();
        nightShiftsTaken = new ArrayList<>();
    }

    @Override
    public void run(){
        try {
            System.out.println("Counter = " + counter);
            int completedSchedules = 0;
            while (completedSchedules != numOfJuniorDoctors) {
                IndividualRota rota = new IndividualRota();
                JuniorDoctor doctor = rota.IndividualRota(startDate, endDate, numberOfDays, nightShiftsTaken);
                totalShifts.add(doctor);
                updateNightShiftsTaken(doctor);
                completedSchedules++;
            }
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }

    public void updateNightShiftsTaken(JuniorDoctor doctor){
        Enumeration<LocalDate> e = doctor.shifts.keys();
        if(e == null){
            return;
        }
        while (e.hasMoreElements()){
            nightShiftsTaken.add(e.nextElement());
        }
    }

    public ArrayList<JuniorDoctor> schedule(){
        return totalShifts;
    }
}
