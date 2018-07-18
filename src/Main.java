import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.InitializeInvestors();
        Scanner scan = new Scanner(System.in);
        bank.BankInitializeClients(scan);
        bank.Compute();
    }
}

