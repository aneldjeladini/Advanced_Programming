import java.util.*;

class InvalidWithdrawException extends Exception{
    public InvalidWithdrawException(String message){
        super(message);
    }
    public void message(){
        System.out.println(getMessage());
    }
}

class InvalidDepositException extends Exception{
    public InvalidDepositException(String message){
        super(message);
    }

    public void message(){
        System.out.println(getMessage());
    }
}

interface InterestBearingAccount{
    public void addInterest();
}



abstract class Account{
    protected String owner;
    protected int number;
    protected double balance;
    protected static int numAccounts = 0;

    public Account(String owner,double balance){
        this.owner = owner;
        this.balance = balance;
        this.number = numAccounts+1;
        numAccounts++;
    }

    public void withdraw(double amount) throws InvalidWithdrawException {
        if (amount <= 0){
            throw new InvalidWithdrawException("Amount must be positive");
        }
        if (amount > balance){
            throw new InvalidWithdrawException("Insufficient funds");
        }

        this.balance -= amount;
    }

    public void deposit(double amount) throws InvalidDepositException {
        if (amount <= 0){
            throw new InvalidDepositException("Amount must be positive");
        }
        this.balance += amount;
    }

    public double getBalance(){
        return balance;
    }

    public String toString(){
        return String.format("Account type: %s\nAccount owner: %s\nAccount number: %d\nBalance: $%.2f%n", this.getClass().getSimpleName(), owner,number,balance);
    }

}

class InterestCheckingAccount extends Account implements InterestBearingAccount{

    protected static double interest = 0.03;

    public InterestCheckingAccount(String owner,double balance){
        super(owner,balance);
    }

    @Override
    public void addInterest() {
        this.balance *= (1+interest);
    }

}

class PlatinumCheckingAccount extends InterestCheckingAccount{

    public PlatinumCheckingAccount(String owner,double balance){
        super(owner,balance);
    }

    @Override
    public void addInterest(){
        this.balance *= (1+(2*interest));
    }
}

class NonInterestCheckingAccount extends Account{
    public NonInterestCheckingAccount(String owner,double balance){
        super(owner,balance);
    }
}

class Bank {
    private ArrayList<Account> accounts;

    public Bank(){
        this.accounts = new ArrayList<>();
    }

    public Bank(ArrayList<Account>accounts){
        this.accounts = accounts;
    }

    public void addAccount(Account account){
        this.accounts.add(account);
    }

    public double totalAssests(){
        double sum = 0;
        for (Account account : accounts){
            sum += account.getBalance();
        }
        return sum;
    }

    public void addInterest(){
        for (Account account : accounts){
            if (account instanceof InterestBearingAccount){
                ((InterestBearingAccount) account).addInterest();
            }
        }
    }

    public ArrayList<Account> getAccounts(){
        return accounts;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Account acc : accounts){
            sb.append(acc.toString()).append('\n');
        }
        return sb.toString();
    }

}


public class Bank_Test {
    public static void main(String[] args) {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new InterestCheckingAccount("John Smith",20000));
        accounts.add(new NonInterestCheckingAccount("Andy Taylor", 10000));
        accounts.add(new PlatinumCheckingAccount("William Johnson",50000));

        Bank bank = new Bank(accounts);
        System.out.println(bank);

        System.out.println("===========================================");
        System.out.println("Adding interest to accounts");
        System.out.println("===========================================\n");

        bank.addInterest();
        System.out.println(bank);
        System.out.println();

        System.out.println("===========================================");
        System.out.println("Withdrawing valid amount from all accounts");
        System.out.println("===========================================\n");


        for (Account acc : bank.getAccounts()){
            try{
                System.out.println(acc);
                acc.withdraw(2500);
                System.out.println("Withdrew 2,500$");
                System.out.println("Account after withdrawal:");
                System.out.println(acc);
                System.out.println();
            } catch (InvalidWithdrawException e) {
                e.message();
            }
        }

        System.out.println("===========================================");
        System.out.println("Withdrawing illegal amount");
        System.out.println("===========================================\n");

        try {
            System.out.println(bank.getAccounts().getFirst());
            System.out.println("Trying to withdraw 300,000$");
            bank.getAccounts().getFirst().withdraw(300000);
        } catch (InvalidWithdrawException e) {
            e.message();
        }

    }



}
