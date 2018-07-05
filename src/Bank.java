import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bank {

    public static MyResult BestInterest(ArrayList<Investor> InvestorsArray, Client c1) {
        int k = 0;
        double csum = 0, su = 0, rate = 0;
        for (int i = 0; i < InvestorsArray.size(); i++) {

            su = InvestorsArray.get(i).getSum() + su + ((InvestorsArray.get(i).getSum() * InvestorsArray.get(i).getInterest() / 100) / 12 * c1.getPeriod());
            rate += InvestorsArray.get(i).getSum() * InvestorsArray.get(i).getInterest() / 100;

            if (csum < c1.getSum()) {
                if (csum + InvestorsArray.get(i).getSum() > c1.getSum()) {
                    InvestorsArray.get(i).setSum(InvestorsArray.get(i).getSum() - (c1.getSum() - csum));
                    csum += c1.getSum() - csum;
                    break;
                } else {
                    csum += InvestorsArray.get(i).getSum();
                    InvestorsArray.get(i).setSum(0);
                }
            }
            if (csum >= c1.getSum()) break;
        }
        if (csum > c1.getSum()) return new MyResult(0, 0, 0);
        else return new MyResult(csum, su, rate);
    }
    public static void Compute(int ClientsNr,int TotalSum, ArrayList<Investor> InvestorsArray, ArrayList<Client> ClientsArray)
    {
        if (ClientsNr > 0) {
            for (int i = 0; i < ClientsNr; i++) {
                MyResult myResult = BestInterest(InvestorsArray, ClientsArray.get(i));
                DecimalFormat df = new DecimalFormat("#0.000");
                TotalSum = TotalSum - ClientsArray.get(i).getSum();

                if (TotalSum < 0)
                    System.out.print("INSUFFICIENT FOUNDS for client named " + ClientsArray.get(i).getName() + " surnamed " + ClientsArray.get(i).getSurname());
                else {
                    System.out.print("For client named " + ClientsArray.get(i).getName() + " surnamed " + ClientsArray.get(i).getSurname());
                    System.out.print("total sum is " + myResult.getSecond() + " lei ");
                    System.out.print("with the interest rate " + df.format(myResult.getThird() * 100 / ClientsArray.get(i).getSum())+"% ");
                    System.out.print("and a monthly rate of " + df.format(myResult.getSecond() / ClientsArray.get(i).getPeriod()) + " lei");
                }}
        }
    }

    public void BankInitialize(Scanner scanner, Scanner scan){

        scanner.useDelimiter(",");
        int NrInv = 0;
        if (scanner.hasNextLine()) {
            NrInv = Integer.parseInt(scanner.nextLine());
        }

        ArrayList<Investor> InvestorsArray = new ArrayList<Investor>();
        ArrayList<Client> ClientsArray = new ArrayList<Client>();
        String text;
        int TotalSum = 0;

        for (int i = 1; i <= NrInv; i++) {
            Investor i1 = new Investor();
            text = scanner.nextLine();
            String[] line = text.split(",");
            i1.setName(line[0]);
            i1.setSurname(line[1]);
            i1.setNumber(line[2]);
            i1.setEmail(line[3]);
            i1.setSum(Integer.parseInt(line[4]));
            i1.setInterest(Integer.parseInt(line[5]));
            TotalSum += i1.getSum();
            InvestorsArray.add(i1);
        }

        Collections.sort(InvestorsArray);

        int ClientsNr = 0;
        boolean ok = false;
        System.out.println("HELLO FRIEND!");
        while (ok == false) {
            System.out.print("Enter desired number of clients=");
            String x = scan.nextLine();
            if (x.matches(".*\\d+.*")) {
                ClientsNr = Integer.parseInt(x);
                ok = true;
            } else {
                System.out.println("Please insert a number!");
            }
        }

        for (int i = 1; i <= ClientsNr; i++) {
            Client c1 = new Client();

            //Reading valid client name
            ok = false;
            while (ok == false) {
                System.out.print("Client name=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && !text.matches(".*\\d+.*")) {
                    c1.setName(text);
                    ok = true;
                } else System.out.println("Please enter the name!");
            }

            //Reading valid client surname
            ok = false;
            while (ok == false) {
                System.out.print("Client surname=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && !text.matches(".*\\d+.*")) {
                    c1.setSurname(text);
                    ok = true;
                } else System.out.println("Please enter the surname!");
            }

            //Reading valid phone number
            ok = false;
            while (ok == false) {
                System.out.print("Client phone number=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && (Pattern.matches("[a-zA-Z]", text) == false) && text.length() >= 10 && text.length() <= 11) {
                    c1.setNumber(text);
                    ok = true;
                } else System.out.println("Please enter a valid number!");
                c1.setNumber(text);
            }

            //Reading valid client email
            ok = false;
            while (ok == false) {
                System.out.print("Client email=");
                text = scan.nextLine();
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(text);
                boolean matchFound = m.matches();
                if (matchFound) {
                    c1.setEmail(text);
                    ok = true;
                } else {
                    System.out.println("Please enter a valid email address!");
                }
            }

            //Reading valid client ammount
            ok = false;
            while (ok == false) {
                System.out.print("Enter desired ammount=");
                String x = scan.nextLine();
                if (x.matches(".*\\d+.*")) {
                    c1.setSum(Integer.parseInt(x));
                    ok = true;
                } else {
                    System.out.println("Please insert a number!");
                }
            }

            //Reading valid period in months
            ok = false;
            while (ok == false) {
                System.out.print("Enter desired period in months=");
                String x = scan.nextLine();
                if (x.matches(".*\\d+.*")) {
                    c1.setPeriod(Integer.parseInt(x));
                    ok = true;
                } else {
                    System.out.println("Please insert a number!");
                }
            }

            ClientsArray.add(c1);
        }
        scanner.close();
        Compute(ClientsNr,TotalSum,InvestorsArray,ClientsArray);
    }
}
