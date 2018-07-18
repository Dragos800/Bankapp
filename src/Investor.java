public class Investor implements Comparable<Investor>{
    private String name, surname, number, email;
    private double sum, interest;

    public Investor() {
    }

    public Investor(String s, String s1, String s2, String s3, double v, double v1) {
        this.name=s;
        this.surname=s1;
        this.number=s2;
        this.email=s3;
        this.sum=v;
        this.interest=v1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    @Override
    public int compareTo(Investor i1) {
        return (int)( interest-i1.getInterest());
    }

    public String toString() {
        return "[ Name=" + name + ", Surname=" + surname + ", Number=" + number + ", Email=" + email + ", Sum=" + sum + ", Interest=" + interest + "]";
    }
}
