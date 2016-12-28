import static org.junit.Assert.*;

import org.junit.Test;

public class A2Tester {

    @Test
    public void testA2() {
        // testing method isPalindrome() from class A2
        assertEquals(false, A2.isPalindrome("bananahammock"));
        assertEquals(true, A2.isPalindrome(""));
        assertEquals(false, A2.isPalindrome("ab"));
        assertEquals(true, A2.isPalindrome("aba"));
        assertEquals(true, A2.isPalindrome("abba"));
        assertEquals(false, A2.isPalindrome("Madam, I'm Adam"));
        assertEquals(false, A2.isPalindrome("MadamImAdam"));
        assertEquals(true, A2.isPalindrome("madamimadam"));

        // testing method numOccurrences() from class A2
        assertEquals(2, A2.numOccurrences("bananahammock", "na"));
        assertEquals(1, A2.numOccurrences("ab", "b"));
        assertEquals(2, A2.numOccurrences("Luke Skywalker", "ke"));
        assertEquals(3, A2.numOccurrences("abababab", "aba"));
        assertEquals(0, A2.numOccurrences("bananahammock", "poop"));
        assertEquals(0, A2.numOccurrences("", "a"));

        // testing method fixName() from class A2
        assertEquals("Gries, David", A2.fixName("    David Gries"));
        assertEquals("Chaudhuri, Sid", A2.fixName("sid       chaudhuri "));
        assertEquals("Gosling, James A.", A2.fixName("James Arthur Gosling"));
        assertEquals("Li, Peter", A2.fixName("      Peter li"));
        assertEquals("Kelland, John A.", A2.fixName("john a kelland    "));
        assertEquals("B, A", A2.fixName("a b"));
        assertEquals("B, A C.", A2.fixName("a C b"));
        assertEquals("Sedita, Lauren", A2.fixName("         LAUREN          SEDiTa"));

        // testing method areAnagrams() from class A2
        assertEquals(true, A2.areAnagrams("mary", "army"));
        assertEquals(true, A2.areAnagrams("tom marvolo riddle", "i am lordvoldemort"));
        assertEquals(true, A2.areAnagrams("", ""));
        assertEquals(false, A2.areAnagrams("bananahammock", "i like big fat cock"));
        assertEquals(false, A2.areAnagrams("", "poop"));
        assertEquals(false, A2.areAnagrams("poop, I have", "Ihave, poop"));

        // testing method replaceConsonants() from class A2
        assertEquals("^i_e__a__", A2.replaceConsonants("Minecraft"));
        assertEquals("A_a_ ^u__i__", A2.replaceConsonants("Alan Turning"));
        assertEquals("^a_a_a^a__o__", A2.replaceConsonants("BananaHammock"));
        assertEquals("aaaaa", A2.replaceConsonants("aaaaa"));
        assertEquals("^^^^^", A2.replaceConsonants("CCCBC"));
        assertEquals("_____", A2.replaceConsonants("cbvrp"));
        assertEquals("", A2.replaceConsonants(""));

        // testing method decompress() from class A2
        assertEquals("bbbcbbbbbxx", A2.decompress("3b1c5b2x0a"));
        assertEquals("", A2.decompress("0a0b0c0d0e"));
        assertEquals("22233355", A2.decompress("323325"));
        assertEquals("   ..poop", A2.decompress("3 2.1p2o1p"));
        assertEquals("", A2.decompress(""));
    }

}
