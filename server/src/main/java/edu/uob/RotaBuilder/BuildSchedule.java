package edu.uob.RotaBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class BuildSchedule {
    private static LocalDate startDate;
    private static LocalDate endDate;
    private static int numberOfDoctors;
    private static int numberOfDays;
    private static int rulesBroken;
    private static ArrayList<String> descriptions;
    private static ArrayList<JuniorDoctor> doctors;
    private static Hashtable<LocalDate, ArrayList<Shifts>> fwp;

    public BuildSchedule(LocalDate start, LocalDate end, int days, ArrayList<JuniorDoctor> doc,
                         Hashtable<LocalDate, ArrayList<Shifts>> fixedWorkingPattern){
        startDate = start;
        endDate = end;
        numberOfDays = days;
        numberOfDoctors = doc.size();
        doctors = doc;
        fwp = fixedWorkingPattern;


        addShifts();
        Rules rulesBrokenCount = new Rules(doctors, startDate, endDate);
        rulesBroken = rulesBrokenCount.getRulesBroken();
        //descriptions = rulesBrokenCount.getRulesBrokenDescriptions();

    }

    public int getRulesCount(){
        return rulesBroken;
    }

    public ArrayList<String> getDescriptions(){return descriptions;}

    private static void addShifts(){

        while(true) {
            //Reset shifts - used for if previous iteration was unable to find solution and need to start again
            resetDoctors();
            //inputs any annual and study leave
            checkStudyAndAnnualLeave();
            // inputs any not on call requests
            checkNotOnCall();
            //checks if doctor need pain week and sets it if needed
            painWeekCheck();
            //adds weekend shifts
            if(selectWeekends()){
                //adds night shifts
               if (calculateNightShifts()) {
                   //adds long day shifts
                    if (addLongDayShifts()) {
                        //alters amount of theatre shifts depending on currently assigned on call shifts
                        reduceTheatreShifts();
                        //adds remaining theatre shifts
                        if(addTheatreShifts()) {
                            //sets the rest of the days to days off
                            setOffDays(startDate, endDate, doctors);
                            break;
                        }
                    }
                }
            }
        }
    }


    private static void resetDoctors(){
        for(JuniorDoctor doctor : doctors){
            doctor.resetDoctor();
        }
    }

    private static void checkNotOnCall(){
        for(JuniorDoctor doctor : doctors){
            Hashtable<LocalDate, NotOnCallRequestType> notOnCall = doctor.getNotOnCallRequest();
            if(!notOnCall.isEmpty()){
                Set<LocalDate> setOfKeys = notOnCall.keySet();
                for (LocalDate date : setOfKeys) {
                    if(doctor.getNotOnCallRequestType(date).equals(NotOnCallRequestType.DAY)) {
                        doctor.setShifts(date, Shifts.NOCR);
                    }else if(doctor.getNotOnCallRequestType(date).equals(NotOnCallRequestType.HALF)) {
                        doctor.setShifts(date, Shifts.THNOCR);
                        doctor.reduceTheatre();
                    }
                }
            }
        }
    }

    private static void checkStudyAndAnnualLeave(){
        for(JuniorDoctor doctor : doctors){
            Hashtable<LocalDate, LeaveType> leave = doctor.getAnnualOrStudyLeaveRequest();
            if(!leave.isEmpty()){
                Set<LocalDate> setOfKeys = leave.keySet();
                for (LocalDate date : setOfKeys) {
                    if(doctor.getAnnualOrStudyLeaveRequestType(date).equals(LeaveType.ANNUAL)) {
                        doctor.setShifts(date, Shifts.ANNUAL);
                        doctor.reduceTheatre();
                    }else if(doctor.getAnnualOrStudyLeaveRequestType(date).equals(LeaveType.STUDY)) {
                        doctor.setShifts(date, Shifts.STUDY);
                        doctor.reduceTheatre();
                    }else if(doctor.getAnnualOrStudyLeaveRequestType(date).equals(LeaveType.STUDYHALF)) {
                        doctor.setShifts(date, Shifts.THSL);
                        doctor.reduceTheatre();
                    }else if(doctor.getAnnualOrStudyLeaveRequestType(date).equals(LeaveType.ANNUALHALF)) {
                        doctor.setShifts(date, Shifts.THAL);
                        doctor.reduceTheatre();
                    }
                }
            }
        }
    }

    private static void painWeekCheck(){
        ArrayList<LocalDate> painWeeksTaken = new ArrayList<>();
        for(JuniorDoctor doctor : doctors){
            if(doctor.getPainWeek()){
                painWeeksTaken.add(addPainWeek(doctor, painWeeksTaken));
            }
        }
    }

    private static LocalDate addPainWeek(JuniorDoctor doctor, ArrayList<LocalDate> painWeeksTaken){
        LocalDate date;
        while(true){
            int daySelection = ThreadLocalRandom.current().nextInt(0, numberOfDays +1);
            date = startDate.plusDays(daySelection);
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && date.plusDays(8).isBefore(endDate)
                    && !painWeeksTaken.contains(date.plusDays(2)) && checkShiftFree(doctor,date, 8)){
                doctor.setPainWeekStartDate(date.plusDays(2));
                for(int i=0; i< 9; i++){
                    if(i<2 || i > 6) {
                        doctor.setShifts(date, Shifts.DAYOFF);
                    }else{
                        doctor.setShifts(date, Shifts.THEATRE);
                        doctor.reduceTheatre();
                    }
                    date = date.plusDays(1);
                }
                break;
            }
        }
        return doctor.getPainWeekStartDate();
    }

    private static boolean selectWeekends(){
        int counter = 0;
        int weekendsCovered = getWeekendsCovered();
        LocalDate date = startDate;
        while (date.isBefore(endDate.plusDays(1)) && counter < weekendsCovered){
            if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
                //sees if this weekend is already covered by fixed working pattern worker
                if(fwp.containsKey(date)) {
                    ArrayList<Shifts> shiftType = fwp.get(date);
                    if (shiftType.contains(Shifts.NIGHT) && !shiftType.contains(Shifts.DAYON)) {
                        if (addWeekends(date, Shifts.DAYON)) {
                            date = date.plusDays(1);
                            counter++;
                            continue;
                        } else {
                            return false;
                        }
                    }
                    if (shiftType.contains(Shifts.DAYON) && !shiftType.contains(Shifts.NIGHT)) {
                        if (addWeekends(date, Shifts.NIGHT)) {
                            date = date.plusDays(1);
                            counter++;
                            continue;
                        } else {
                            return false;
                        }
                    }
                }
                //check can work weekends
                if (addWeekends(date, Shifts.NIGHT) && addWeekends(date, Shifts.DAYON)) {
                    //date = date.plusDays(1);
                    counter++;
                } else {
                    return false;
                }

            }
            date =date.plusDays(1);
        }
        int totalWeekends = numberOfWeekends();
        if(totalWeekends > weekendsCovered){
            addExtraWeekends(totalWeekends-weekendsCovered, date.plusDays(1));
        }

        return true;
    }

    private static boolean addWeekends(LocalDate date, Shifts shift){
        int errorCounter = 0;
        while(errorCounter < 50) {
            //chooses a random doctor
            int selectDoctor1 = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
            JuniorDoctor doctor = doctors.get(selectDoctor1);
            //if doctor has no more available weekends, it restarts the loop, selecting a new doctor
            if (doctor.getWeekends() == 0) {
                errorCounter++;
                continue;
            }
            if(shift.equals(Shifts.NIGHT)){
                if(!checkShiftFree(doctor, date, 6)){
                    errorCounter++;
                    continue;
                }
            }else{
                if(!checkShiftFree(doctor, date, 5)){
                    errorCounter++;
                    continue;
                }
            }
            //sets the weekend shifts
            for (int i=0; i<3; i++){
                doctor.setShifts(date, shift);
                date = date.plusDays(1);
            }
            //night shifts require 3 days off afterwards, day shifts only require 2 days off
            //reduced the number of remaining shifts for the doctor of that type
            int daysOff;
            if (shift.equals(Shifts.NIGHT)) {
                doctor.reduceNights(3);
                doctor.reduceTotalOnCall(3);
                daysOff = 3;
            }else{
                daysOff = 2;
                doctor.reduceLongDays(3);
                doctor.reduceTotalOnCall(3);
            }
            //adds the off days
            for (int i=0; i<daysOff; i++){
                if (shift.equals(Shifts.NIGHT)) {
                    doctor.setShifts(date, Shifts.NAOFF);
                }else{
                    doctor.setShifts(date, Shifts.DAOFF);
                }
                date = date.plusDays(1);
            }
            doctor.reduceWeekends();
            break;
        }
        return errorCounter != 50;
    }

    private static int getWeekendsCovered(){
        int counter = 0;
        for(JuniorDoctor doctor: doctors){
            counter += doctor.getWeekends();
        }
        return counter/2;
    }

    private static int numberOfWeekends(){
        //adds up the total number of weekends within the rota time period
        LocalDate date = startDate;
        int counter = 0;
        while(date.isBefore(endDate.plusDays(1))){
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
                counter++;
            }
            date = date.plusDays(1);
        }
        return counter;
    }

    private static void addExtraWeekends(int weekends, LocalDate date){
        //randomly assigns any weekends to junior doctors that are not currently covered
        //meaning that some doctors will end up having to work more weekends
        int counter = 0;
        while(date.isBefore(endDate.plusDays(1)) && counter < weekends){
            if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                if (fwp.containsKey(date)) {
                    ArrayList<Shifts> shiftType = fwp.get(date);
                    if (shiftType.contains(Shifts.NIGHT) && !shiftType.contains(Shifts.DAYON)) {
                        while (true) {
                            if (addExtraWeekendsDays(date)) {
                                break;
                            }
                        }
                    }else if (!shiftType.contains(Shifts.NIGHT) && shiftType.contains(Shifts.DAYON)) {
                        while (true) {
                            if (addExtraWeekendsNights(date)) {
                                break;
                            }
                        }
                    }
                }else {
                    //check can work weekends
                    while (true) {
                        if (addExtraWeekendsNights(date)) {
                            break;
                        }
                    }
                    while (true) {
                        if (addExtraWeekendsDays(date)) {
                            break;
                        }
                    }
                }
                counter++;
            }
            date = date.plusDays(1);
        }
    }

    private static boolean addExtraWeekendsNights(LocalDate date){
        int selectDoctor1 = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
        JuniorDoctor doctor = doctors.get(selectDoctor1);
        if(!checkShiftFree(doctor, date, 6)){
            return false;
        }else{
            for (int i=0; i<3; i++){
                doctor.setShifts(date, Shifts.NIGHT);
                date = date.plusDays(1);
            }
            for (int i=0; i<3; i++){
                doctor.setShifts(date, Shifts.NAOFF);
                date = date.plusDays(1);
            }
        }
        return true;
    }

    private static boolean addExtraWeekendsDays(LocalDate date){
        int selectDoctor1 = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
        JuniorDoctor doctor = doctors.get(selectDoctor1);
        if(!checkShiftFree(doctor, date, 5)){
            return false;
        }else{
            for (int i=0; i<3; i++){
                doctor.setShifts(date, Shifts.DAYON);
                date = date.plusDays(1);
            }
            for (int i=0; i<2; i++){
                doctor.setShifts(date, Shifts.DAOFF);
                date = date.plusDays(1);
            }
        }
        return true;
    }


    private static boolean calculateNightShifts(){
        int errorCounter = 0;
        LocalDate date = startDate;

        ArrayList<Integer> index = indexDoctors();

        while (date.isBefore(endDate.plusDays(1)) && errorCounter < 500) {

            if(index.size() == 0){
                return addExtraNights(date);
            }

            if(fwp.containsKey(date)){
                ArrayList<Shifts> shiftTypes = fwp.get(date);
                if(shiftTypes.contains(Shifts.NIGHT)){
                    date = date.plusDays(1);
                    continue;
                }
            }

            int shiftPairing = ThreadLocalRandom.current().nextInt(0, index.size());
            JuniorDoctor doctor = doctors.get(index.get(shiftPairing));

            if(doctor.getNights() <= 0){
                index.remove(shiftPairing);
                continue;
            }

            if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                // add option for 4 nights in a row request
                if(checkShiftFree(doctor, date, 4) && addNightShifts(doctor, date)) {
                    date = date.plusDays(1);
                }
                else {
                    errorCounter++;
                }
                continue;
            }
            else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
                if(checkShiftFree(doctor, date, 4) && addNightShifts(doctor, date)) {
                    date = date.plusDays(1);
                }else {
                    errorCounter++;
                }
                continue;
            }
            else{
                date = date.plusDays(1);
            }
            errorCounter++;


        }
        return errorCounter != 500;
    }

    private static boolean addExtraNights(LocalDate date){
        //assigns any uncovered night shifts
        int errorCounter = 0;
        while (date.isBefore(endDate.plusDays(1)) && errorCounter < 100){
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
            JuniorDoctor doctor = doctors.get(shiftPairing);

            if(fwp.containsKey(date)){
                ArrayList<Shifts> shifts = fwp.get(date);
                if(shifts.contains(Shifts.NIGHT)) {
                    date = date.plusDays(1);
                    continue;
                }
            }
            if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                // add option for 4 nights in a row request
                if(checkShiftFree(doctor, date, 4) && addNightShifts(doctor, date)) {
                    date = date.plusDays(1);
                }
                else {
                    errorCounter++;
                }
            }
            else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
                if(checkShiftFree(doctor, date, 4) && addNightShifts(doctor, date)) {
                    date = date.plusDays(1);
                }else {
                    errorCounter++;
                }
            }
            else{
                date = date.plusDays(1);
            }
        }
        return errorCounter != 100;
    }

    private static void reduceTheatreShifts(){
        //if junior doctor is working extra on call shifts then their theatre shifts are reduced to compensate the hours
        //if not working enough on call - extra theatre shifts assigned
        double hours = 0;
        for(JuniorDoctor doctor: doctors){
            Set<LocalDate> setOfKeys = doctor.returnAllShifts().keySet();
            int counter = 0;
            for (LocalDate key : setOfKeys) {
                if(doctor.getShiftType(key).equals(Shifts.NIGHT) ||
                        doctor.getShiftType(key).equals(Shifts.DAYON)){
                    counter++;
                }
            }
            if(counter > (doctor.getSetLongDays() + doctor.getSetNights())){
                counter = counter - (doctor.getSetLongDays() + doctor.getSetNights());
                hours = 12.5 * counter;
                int reduction = (int) Math.round(hours/10);
                doctor.reduceTheatre(reduction);
            }
            else if(counter < (doctor.getSetLongDays() + doctor.getSetNights())){
                counter = (doctor.getSetLongDays() + doctor.getSetNights()) - counter;
                hours = 12.5 * counter;
                int increase = (int) Math.round(hours/10);
                doctor.increaseTheatre(increase);
            }
        }
    }

    private static boolean addNightShifts(JuniorDoctor doctor, LocalDate shift){


        LocalDate date = shift;

        for (int i = 0; i< 2; i++){
            doctor.setShifts(date, Shifts.NIGHT);
            date = date.plusDays(1);
        }

        for (int i = 0; i< 2; i++){
            doctor.setShifts(date, Shifts.NAOFF);
            date = date.plusDays(1);
        }
        doctor.reduceNights(2);
        doctor.reduceTotalOnCall(2);

        return  true;
    }

    private static boolean checkShiftFree(JuniorDoctor doctor, LocalDate shift, int numberOfDays){
        //checks that this date has not already been assigned a shift
        //also checks that date is in doctors start and end date - needed in case doctor changes rota or doesn't work full rota
        for(int i=0; i<numberOfDays; i++){
            if(doctor.shiftTaken(shift) || shift.isBefore(doctor.getStartDate())){
                return false;
            }
            if(!shift.isBefore(doctor.getEndDate()) && shift.isBefore(endDate)){
                return false;
            }
            shift = shift.plusDays(1);
        }
        return true;
    }

    public static void setOffDays(LocalDate startDate, LocalDate endDate, ArrayList<JuniorDoctor> doctors){
        for(JuniorDoctor doctor : doctors) {
            LocalDate date = startDate;
            while (date.isBefore(endDate.plusDays(1))) {
                if (!doctor.shiftTaken(date)) {
                    doctor.setShifts(date, Shifts.DAYOFF);
                }
                date = date.plusDays(1);
            }
        }
    }


    private static ArrayList<Integer> indexDoctors(){
        ArrayList<Integer> index = new ArrayList<>();
        for(int i=0; i<doctors.size(); i++){
            index.add(i);
        }
        return index;
    }

    private static boolean addLongDayShifts(){
        LocalDate date = startDate;
        int errorCounter = 0;

        ArrayList<Integer> index = indexDoctors();

        while (date.isBefore(endDate.plusDays(1)) && errorCounter < 500) {

            if(index.size() == 0){
                addExtraDays(date);
                break;
            }
            else {

                if (fwp.containsKey(date)) {
                    ArrayList<Shifts> shiftType = fwp.get(date);
                    if (shiftType.contains(Shifts.DAYON) && date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                        date = date.plusDays(3);
                        continue;
                    } else if (shiftType.contains(Shifts.DAYON)) {
                        date = date.plusDays(1);
                        continue;
                    }
                }

                if (date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                    date = date.plusDays(3);
                    continue;
                }
                int shiftPairing = ThreadLocalRandom.current().nextInt(0, index.size());

                JuniorDoctor doctor = doctors.get(index.get(shiftPairing));

                if (doctor.getTotalOnCall() <= 0) {
                    index.remove(shiftPairing);
                    continue;
                }

                if (doctor.shiftTaken(date)) {
                    errorCounter++;
                } else {
                    doctor.setShifts(date, Shifts.DAYON);
                    doctor.reduceLongDays();
                    doctor.reduceTotalOnCall();
                    date = date.plusDays(1);
                }
            }

        }
        return  errorCounter != 500;
    }



    private static void addExtraDays(LocalDate date){
        while(date.isBefore(endDate.plusDays(1))){
            if(fwp.containsKey(date)){
                ArrayList<Shifts> shifts = fwp.get(date);
                if(shifts.contains(Shifts.DAYON)) {
                    date = date.plusDays(1);
                    continue;
                }
            }
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
            JuniorDoctor doctor = doctors.get(shiftPairing);

            if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY) || date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                date = date.plusDays(1);
                continue;
            }

            if(!doctor.shiftTaken(date)) {
                doctor.setShifts(date, Shifts.DAYON);
                doctor.reduceLongDays();
                doctor.reduceTotalOnCall();
                date = date.plusDays(1);
            }
        }
    }

    private static boolean addTheatreShifts(){
        int errorCounter;
        for(JuniorDoctor doctor : doctors){
            errorCounter = 0;
            while(doctor.getTheatre() > 0 && errorCounter < 500){
                int daySelection = ThreadLocalRandom.current().nextInt(0, numberOfDays +1);
                LocalDate date = startDate.plusDays(daySelection);
                if(!doctor.shiftTaken(date) && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                        && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                    doctor.setShifts(date, Shifts.THEATRE);
                    doctor.reduceTheatre();
                }else{
                    errorCounter++;
                }
            }
            if(errorCounter == 500){
                return false;
            }
        }
        return true;

    }
}
