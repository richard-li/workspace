import static org.junit.Assert.*;
import org.junit.Test;

public class PhDTester {
    @Test
    public void testConstructorA() {
        //Group A
        PhD doctor = new PhD("Richard", 'M', 2015, 5);
        assertEquals("Richard", doctor.getName());
        assertEquals(false, doctor.isFemale());
        assertEquals(2015, doctor.getYear());
        assertEquals(5, doctor.getMonth());
        assertEquals(null, doctor.getAdvisor1());
        assertEquals(null, doctor.getAdvisor2());
        assertEquals(0, doctor.numAdvisees());
        PhD doctorTwo = new PhD("Diane", 'F', 2012, 5);
        assertEquals(true, doctorTwo.isFemale());
    }
    @Test
    public void testConstructorB() {
        //Group B
        PhD doctor = new PhD("Richard", 'M', 2015, 5);
        PhD advisorOne = new PhD("Teacher", 'M', 2000, 10);
        PhD advisorTwo = new PhD("Professor", 'F', 1996, 6);
        doctor.addAdvisor1(advisorOne);
        doctor.addAdvisor2(advisorTwo);
        assertEquals(advisorOne, doctor.getAdvisor1());
        assertEquals(advisorTwo, doctor.getAdvisor2());
        assertEquals(2, doctor.numAdvisees());
    }
    @Test
    public void testConstructorC() {
        //Group C
        PhD doctor = new PhD("Richard", 'M', 2015, 5);
        PhD doctorTwo = new PhD("Diane", 'F', 2012, 5);
        PhD advisorOne = new PhD("Teacher", 'M', 2000, 10);
        PhD advisorTwo = new PhD("Professor", 'F', 1996, 6);
        doctor.addAdvisor1(advisorOne);
        doctor.addAdvisor2(advisorTwo);
        PhD test1 = new PhD("Bob", 'M', 4983, 1, advisorOne);
        PhD test2 = new PhD("Mom", 'F', 9384, 7, doctor, null);
        assertEquals("Bob", test1.getName());
        assertEquals(false, test1.isFemale());
        assertEquals(4983, test1.getYear());
        assertEquals(1, test1.getMonth());
        assertEquals(advisorOne, test1.getAdvisor1());
        assertEquals(null, test1.getAdvisor2());
        assertEquals(1, test1.numAdvisees());
        assertEquals("Mom", test2.getName());
        assertEquals(true, test2.isFemale());
        assertEquals(9384, test2.getYear());
        assertEquals(7, test2.getMonth());
        assertEquals(doctor, test2.getAdvisor1());
        assertEquals(doctorTwo, test2.getAdvisor2());
        assertEquals(2, test2.numAdvisees());
    }
    @Test
    public void testConstructorD() {
        //Group D
        PhD doctor = new PhD("Richard", 'M', 2015, 5);
        PhD doctorTwo = new PhD("Diane", 'F', 2012, 5);
        PhD advisorOne = new PhD("Teacher", 'M', 2000, 10);
        PhD advisorTwo = new PhD("Professor", 'F', 1996, 6);
        doctor.addAdvisor1(advisorOne);
        doctor.addAdvisor2(advisorTwo);
        PhD test1 = new PhD("Bob", 'M', 4983, 1, advisorOne);
        PhD test2 = new PhD("Mom", 'F', 9384, 7, doctor, doctorTwo);
        assertEquals(true, test1.isOlderThan(test2));
        assertEquals(false, doctor.isOlderThan(doctorTwo));
        assertEquals(true, test1.isPhDSibling(doctor));
        assertEquals(false, test2.isPhDSibling(test1));
    }
}