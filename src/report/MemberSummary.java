package report;

public class MemberSummary {
    private int memberId;
    private String name;
    private String email;
    private int activeLoans;
    private int totalFines;       // Новое поле
    private String lastLoanData;  // Новое поле

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

    public void setActiveLoans(int activeLoans) {
        this.activeLoans = activeLoans;
    }

    public void setTotalFines(int totalFines) {
        this.totalFines = totalFines;
    }

    public void setLastLoanData(String lastLoanData) {
        this.lastLoanData = lastLoanData;
    }



    public String Report() {
        return  "Repotr on Member - ID: " + memberId + "\n" +
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Active Loans: " + activeLoans + "\n" +
                "Total Fines: " + totalFines + " KZT\n" +
                "Last Loan: " + (lastLoanData != null ? lastLoanData : "No active loans") + "\n" +
                "Report Date: " + java.time.LocalDate.now();
    }
}