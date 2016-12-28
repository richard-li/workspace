/** An instance maintains info about the PhD of a person.*/

//Checked javadoc and it was good.

public class PhD {

    //Name of the person with a PhD, a String of length > 0
    private String name;

    //Year PhD was awarded
    private int year;

    //Month PhD was awarded, in range 1..12 with 1 being January, etc
    private int month;

    //Gender of person, 'M' means male and 'F' means female
    private char gender;

    //First advisor, the first PhD advisor of this person —null if unknown
    private PhD advisorOne;

    //Second advisor, The second advisor of this person —null if unknown or if the
    //person had only one advisor. If the first-advisor field is null, the second advisor field must be null
    private PhD advisorTwo;

    //Number of advisees of this person
    private int numberOfAdvisees = 0;

    /**
     * Constructor: an instance for a person with name n, gender g, PhD year y, and PhD
     * month m. Its advisors are unknown, and it has no advisees.
     * Precondition: n has at least 1 character, m is in 1..12, and g is 'M' for male or 'F' for female.
     */
    public PhD(String n, char g, int y, int m) {
        assert n.length() > 1;
        assert 1 <= m && m <= 12;
        assert g == 'M' || g == 'F';
        name = n;
        gender = g;
        year = y;
        month = m;
    }

    /**
     * Constructor: a PhD with name n, gender g, PhD year y, PhD month m, first advisor ad, and no second advisor.
     * Precondition: n has at least 1 char, g is 'F' for female or 'M' for male, m is in 1..12, and ad is not null.
     */
    public PhD(String n, char g, int y, int m, PhD ad) {
        assert n.length() > 1;
        assert 1 <= m && m <= 12;
        assert g == 'M' || g == 'F';
        assert ad != null;
        name = n;
        gender = g;
        year = y;
        month = m;
        advisorOne = ad;
        numberOfAdvisees++;
    }

    /**
     * Constructor: a PhD with name n, gender g, PhD year y, PhD month m, first advisor ad1, and second advisor ad2.
     * Precondition: n has at least 1 char, g is 'F' for female or 'M' for male, m is in 1..12, ad1 and ad2 are not
     * null, and ad1 and ad2 are different.
     */
    public PhD(String n, char g, int y, int m, PhD ad1, PhD ad2) {
        assert n.length() > 1;
        assert 1 <= m && m <= 12;
        assert g == 'M' || g == 'F';
        assert ad1 != null && ad2 != null;
        assert ad1 != ad2;
        name = n;
        gender = g;
        year = y;
        month = m;
        advisorOne = ad1;
        advisorTwo = ad2;
        numberOfAdvisees += 2;
    }

    /**
     * Return this person's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the year this person got their PhD.
     */
    public int getYear() {
        return year;
    }

    /**
     * Return the month this person got their PhD.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Return the value of "this person is a female".
     */
    public boolean isFemale() {
        return (gender == 'F');
    }

    /**
     * Return this Phd's first advisor (null if unknown).
     */
    public PhD getAdvisor1() {
        return advisorOne;
    }

    /**
     * Return this PhD's second advisor (null if unknown or non- existent).
     */
    public PhD getAdvisor2() {
        return advisorTwo;
    }

    /**
     * Return the number of PhD advisees of this person.
     */
    public int numAdvisees() {
        return numberOfAdvisees;
    }

    /**
     * Add p as this person's first PhD advisor.
     * Precondition: this person's first advisor is unknown and p is not null.
     */
    public void addAdvisor1(PhD p) {
        assert advisorOne == null && p != null;
        advisorOne = p;
        numberOfAdvisees++;
    }

    /**
     * Add p as this persons second advisor.
     * Precondition: This person's first advisor is known, their second advisor is unknown,
     * p is not null, and p is different from this person's first advisor.
     */
    public void addAdvisor2(PhD p) {
        assert advisorOne != null && advisorTwo == null && p != null && !p.equals(advisorOne);
        advisorTwo = p;
        numberOfAdvisees++;
    }

    /**
     * Return value of "this person got their PhD before p did."
     * Precondition: p is not null.
     */
    public boolean isOlderThan(PhD p) {
        assert p != null;
        return this.getYear() < p.getYear();
    }

    /**
     * Return value of "this person and p are intellectual siblings.”
     * Precondition: p is not null.
     */
    public boolean isPhDSibling(PhD p) {
        assert p != null;
        return !p.equals(this) && (p.getAdvisor1() != null || p.getAdvisor2() != null)
                && (p.getAdvisor1() == this.getAdvisor1() || p.getAdvisor1() == this.getAdvisor2()
                || p.getAdvisor2() == this.getAdvisor1() || p.getAdvisor2() == this.getAdvisor2());
    }
}