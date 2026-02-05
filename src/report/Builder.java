package report;

import entities.Member;
import entities.Loan;

import java.util.List;

public class Builder {

    private Member member;
    private List<Loan> loans;

    public void setMember(Member member) {
        this.member = member;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public MemberSummary build() {
        if (member == null) {
            throw new IllegalStateException("Member is not set");
        }
        if (loans == null) {
            throw new IllegalStateException("Loans are not set");
        }

        MemberSummary summary = new MemberSummary();
        summary.setMemberId(member.getId());
        summary.setName(member.getName());
        summary.setEmail(member.getEmail());
        summary.setTotalLoans(loans.size());

        int active = 0;
        for (int i=0; i<loans.size(); i++) {
            if (loans.get(i).getReturnDate() == null) {
                active++;
            }
        }
        summary.setActiveLoans(active);

        return summary;
    }
}
