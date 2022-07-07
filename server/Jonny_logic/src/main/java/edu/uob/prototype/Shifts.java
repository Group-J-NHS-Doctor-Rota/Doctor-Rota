package edu.uob.prototype;

public enum Shifts {
    DAYON,
    NIGHT,
    THEATRE,
    DAYOFF,
    NAOFF,
    AorSL,
    NOCR,
    THNOCR
    //NAOFF - Night After OFF (requirement that after 2 night shifts, have 2 shifts off & 3 night shifts
    //means 3 days off)
    //AorSl - annual or study leave
    //NOCR not on call request
    //THNOCR - theatre / not on call request - used for half day requests
}
