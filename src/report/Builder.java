package report;

import entities.Member;
import entities.Loan;
import services.FineCalculator;
import cfg.FinePolicy;
import java.util.List;
import java.time.LocalDate;

public class Builder {
    private Member member;
    private List<Loan> loans;
    private final FineCalculator fineCalculator = new FineCalculator();

    public void setMember(Member member) {
        this.member = member;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public MemberSummary build() {
        if (member == null || loans == null) throw new IllegalStateException("Data missing");

        MemberSummary summary = new MemberSummary();
        summary.setMemberId(member.getId());
        summary.setName(member.getName());
        summary.setEmail(member.getEmail());
        summary.setActiveLoans(loans.size());

        int finesSum = 0;
        Loan latestLoan = null;
        LocalDate latestDate = LocalDate.MIN;

        for (Loan l : loans) {
            finesSum += fineCalculator.calculateFineSoFar(l);

            if (l.getBorrowDate().isAfter(latestDate)) {
                latestDate = l.getBorrowDate();
                latestLoan = l;
            }
        }

        summary.setTotalFines(finesSum);


        if (latestLoan != null) {
            LocalDate dueDate = latestLoan.getBorrowDate().plusDays(FinePolicy.getInstance().getMaxLoanDays());

            String info = "Book Name (ID): " + latestLoan.getBook().getTitle() + " (" + latestLoan.getBook().getId() + ")\n" +
                    "Author: " + latestLoan.getBook().getAuthor() + "\n" +
                    "Borrow Date: " + latestLoan.getBorrowDate() + "\n" +
                    "Due Date: " + dueDate + "\n";

            summary.setLastLoanData(info);
        }

        return summary;
    }
}