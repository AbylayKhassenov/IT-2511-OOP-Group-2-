package report;

public class MemberSummary {

    private int memberId;
    private String name;
    private String email;
    private int totalLoans;
    private int activeLoans;

    public MemberSummary() {
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTotalLoans(int totalLoans) {
        this.totalLoans = totalLoans;
    }

    public void setActiveLoans(int activeLoans) {
        this.activeLoans = activeLoans;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getTotalLoans() {
        return totalLoans;
    }

    public int getActiveLoans() {
        return activeLoans;
    }

}

