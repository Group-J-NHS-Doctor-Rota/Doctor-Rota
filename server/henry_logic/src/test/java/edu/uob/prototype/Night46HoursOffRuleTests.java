package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class Night46HoursOffRuleTests {

    @Test
    void testBasicShiftsAndRules() {
        Shift s1 = new Shift(ShiftTypes.NotWorking);
        Shift s2 = new Shift(ShiftTypes.NightOnCall);
        Shift s3 = new Shift(ShiftTypes.DayOnCall);
        Shift s4 = new Shift(ShiftTypes.NormalShift);
        // Not working at all
        Rule r1 = new Night46HoursOffRule(s1, s1, s1);
        assertFalse(r1.isBroken(), "Not working should not break night rule");
        assertEquals(0, r1.getCost(), "0 cost expected");
        // Night then 2 days off
        Rule r2 = new Night46HoursOffRule(s2, s1, s1);
        assertFalse(r2.isBroken(), "Enough time off after night shift, should not break night rule");
        assertEquals(0, r2.getCost(), "0 cost expected");
        // Night then only 1 days off
        Rule r3 = new Night46HoursOffRule(s2, s1, s4);
        assertTrue(r3.isBroken(), "Not enough time off after night shift, should break night rule");
        assertTrue(r3.getCost()>0, "0 cost expected");
        // Day on call then only 1 days off
        Rule r4 = new Night46HoursOffRule(s3, s1, s3);
        assertFalse(r4.isBroken(), "First shift is not night, should not break night rule");
        assertEquals(0, r4.getCost(), "0 cost expected");
        // Normal shift then only 1 days off
        Rule r5 = new Night46HoursOffRule(s4, s1, s2);
        assertFalse(r5.isBroken(), "First shift is not night, should not break night rule");
        assertEquals(0, r5.getCost(), "0 cost expected");
        // Three night shifts
        Rule r6 = new Night46HoursOffRule(s2, s2, s2);
        assertFalse(r6.isBroken(), "First shift is not the end of a streak of nights, should not break night rule");
        assertEquals(0, r6.getCost(), "0 cost expected");
        // Two night shifts
        Rule r7 = new Night46HoursOffRule(s2, s2, s1);
        assertFalse(r7.isBroken(), "First shift is not the end of a streak of nights, should not break night rule");
        assertEquals(0, r7.getCost(), "0 cost expected");
    }
}
