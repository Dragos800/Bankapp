import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Bank bank=new Bank();
        Scanner KeyboardScanner = new Scanner(System.in);
        Scanner FileScanner = null;
        try {
            FileScanner = new Scanner(new File("D:/Projects/abc.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bank.BankInitialize(FileScanner,KeyboardScanner);
    }
}
