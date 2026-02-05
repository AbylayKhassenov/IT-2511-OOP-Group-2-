package cfg;

import entities.Loan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FinePolicy {

    private static final FinePolicy instance = new FinePolicy();

    private final int finePerDay;
    private final int maxLoanDays;

    private FinePolicy() {
        this.finePerDay = 10000;
        this.maxLoanDays = 7;
    }

    public static FinePolicy getInstance() {
        return instance;
    }

    public int getFinePerDay() {
        return finePerDay;
    }

    public int getMaxLoanDays() {
        return maxLoanDays;
    }

    public static class FineCalculator {
        public int loanFine(Loan loan, LocalDate returnDate){

                long returnDelay = ChronoUnit.DAYS.between(loan.getBorrowDate().plusDays(7), returnDate);

                if (returnDelay>0) {

                    long bookFine = (returnDelay * 10000);
                    return Math.toIntExact(bookFine);

                }
            return  0;
        }

        public int calculateFineSoFar(Loan loan){

            if(loan.getReturnDate()!=null){
                return loanFine(loan, loan.getReturnDate());
            } else if (isOverdue(loan, LocalDate.now())) {
                return loanFine(loan, LocalDate.now());
            }

            return 0;
        }

        public boolean isOverdue(Loan loan, LocalDate today){
            long returnDelay = ChronoUnit.DAYS.between(loan.getBorrowDate().plusDays(7), today);
            if (returnDelay>0) {
                return true;
            }
            return false;
        }


    }
}
