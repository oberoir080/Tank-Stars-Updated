package com.badlogic.tankStars;

import org.junit.Test;
import static com.badlogic.tankStars.GameScreen.currHealth1;
import static com.badlogic.tankStars.GameScreen.currHealth2;
import static org.junit.Assert.assertEquals;

public class MyTest {
    @Test
    public void testHealth1() {
        TestClass mySum = new TestClass(175,currHealth1);
        int health = mySum.currHealth1test();
        assertEquals(0, currHealth1); //if tank 1 health 0 test passes
    }
    @Test
    public void testHealth2() {
        TestClass mySum = new TestClass(175,currHealth2);
        int health = mySum.currHealth1test();
        assertEquals(0, currHealth2); //if tank 2 health 0 test passes
    }
}
