package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class Max4LongShiftsRuleTests {

    @Test
    void testBasicShiftsAndRules() {
        Shift s1 = new Shift(ShiftTypes.NotWorking);
        Shift s2 = new Shift(ShiftTypes.NormalShift);
        Shift s3 = new Shift(ShiftTypes.NightOnCall);
        Shift s4 = new Shift(ShiftTypes.DayOnCall);
        // Non-working week, rule should be met
        Rule r1 = new Max4LongShiftsRule(s1, s1, s1, s1, s1, s1, s1);
        assertFalse(r1.isBroken(), "0 hour week should meet rule");
        assertEquals(0, r1.getCost(), "0 cost expected");
        // 4 night shifts then not working
        Rule r2 = new Max4LongShiftsRule(s3, s3, s3, s3, s1, s1, s1);
        assertFalse(r2.isBroken(), "4 night shifts followed by 3 days off should meet rule");
        assertEquals(0, r2.getCost(), "0 cost expected");
        // 4 day on call shifts then not working
        Rule r3 = new Max4LongShiftsRule(s4, s4, s4, s4, s1, s1, s4);
        assertFalse(r3.isBroken(), "4 day on call shifts followed by 2 days off should meet rule");
        assertEquals(0, r3.getCost(), "0 cost expected");
        // Same as last with different 7th day to show no effect
        Rule r4 = new Max4LongShiftsRule(s4, s4, s4, s4, s1, s1, s3);
        assertFalse(r4.isBroken(), "4 day on call shifts followed by 2 days off should meet rule");
        assertEquals(0, r4.getCost(), "0 cost expected");
        // 5 shifts breaks rule
        Rule r5 = new Max4LongShiftsRule(s4, s4, s4, s4, s4, s1, s1);
        assertTrue(r5.isBroken(), "5 day on call shifts should break rule");
        assertTrue(r5.getCost()>0, "0> cost expected");
        // 4 night shifts then 2 not working then night shift
        Rule r6 = new Max4LongShiftsRule(s3, s3, s3, s3, s1, s1, s3);
        assertFalse(r6.isBroken(), "4 night shifts followed by 2 days off then 1 night shift should meet rule");
        assertEquals(0, r6.getCost(), "0 cost expected");
        // 4 shifts but only 1 day off breaks rule
        Rule r7 = new Max4LongShiftsRule(s4, s4, s4, s4, s1, s4, s1);
        assertTrue(r7.isBroken(), "4 day on call shifts but only 1 not working day should break rule");
        assertTrue(r7.getCost()>0, "0> cost expected");
    }
}
