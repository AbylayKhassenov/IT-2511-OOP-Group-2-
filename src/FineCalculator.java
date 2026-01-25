import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class FineCalculator {
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
