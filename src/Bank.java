import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bank {

    public static MyResult bestinterest(ArrayList<Investor> InvestorsArrayAux,Client c1)
    {
        int k=0;
        double csum=0,su=0,rate=0;
        for (int i=0;i<InvestorsArrayAux.size();i++)
        {
            su=InvestorsArrayAux.get(i).getSum()+su+((InvestorsArrayAux.get(i).getSum()*InvestorsArrayAux.get(i).getInterest()/100)/12*c1.getPeriod());
            rate+= InvestorsArrayAux.get(i).getSum()*InvestorsArrayAux.get(i).getInterest()/100;
            if (csum<c1.getSum())
            {
                if(csum+InvestorsArrayAux.get(i).getSum()>c1.getSum()){
                    InvestorsArrayAux.get(i).setSum(InvestorsArrayAux.get(i).getSum()-(c1.getSum()-csum));
                    csum+=c1.getSum()-csum;
                    break;
                }
                else {
                    csum += InvestorsArrayAux.get(i).getSum();
                    InvestorsArrayAux.get(i).setSum(0);
                }
            }
            if (csum>=c1.getSum()) break;
        }
        if (csum>c1.getSum()) return new MyResult(0,0,0);
        else return new MyResult(csum,su,rate);
    }

    public static void main(String [ ] args)
    {
        Scanner scan= new Scanner(System.in);
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("D:/Projects/abc.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        int numinv=0;
        if (scanner.hasNextLine())
        {
            numinv= Integer.parseInt(scanner.nextLine());
        }

        ArrayList<Investor> InvestorsArray = new ArrayList<Investor>();
        ArrayList<Client> ClientsArray = new ArrayList<Client>();
        String text;
        int suminv=0;
        for (int i=1;i<=numinv;i++)
        {
            Investor i1=new Investor();
            text=scanner.nextLine();
            String[] line= text.split(",");
            i1.setName(line[0]);

            i1.setSurname(line[1]);
            i1.setNumber(line[2]);
            i1.setEmail(line[3]);
            i1.setSum(Integer.parseInt(line[4]));
            i1.setInterest(Integer.parseInt(line[5]));
            suminv+=i1.getSum();
            InvestorsArray.add(i1);
        }

        Collections.sort(InvestorsArray);

        int numcli=0;
        boolean ok=false;
        System.out.println("HELLO FRIEND!");
        while (ok==false) {
            System.out.print("Enter desired number of clients=");
            String x = scan.nextLine();
            if (x.matches(".*\\d+.*"))
            {
                numcli = Integer.parseInt(x);
                ok=true;
            }
            else {
                System.out.println("Please insert a number!");
            }
        }

        for (int i=1;i<=numcli;i++)
        {
            Client c1 = new Client();

            //Reading valid client name
            ok=false;
            while (ok==false) {
                System.out.print("Client name=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && !text.matches(".*\\d+.*")) {
                    c1.setName(text);
                    ok = true;
                }
                else System.out.println("Please enter the name!");
            }

            //Reading valid client surname
            ok=false;
            while (ok==false) {
                System.out.print("Client surname=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && !text.matches(".*\\d+.*")) {
                    c1.setSurname(text);
                    ok = true;
                }
                else System.out.println("Please enter the surname!");
            }

            //Reading valid phone number
            ok=false;
            while (ok==false)
            {
                System.out.print("Client phone number=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && (Pattern.matches("[a-zA-Z]", text) == false) && text.length()>=10 && text.length()<=11) {
                    c1.setNumber(text);
                    ok = true;
                }
                else System.out.println("Please enter a valid number!");
                c1.setNumber(text);
            }

            //Reading valid client email
            ok=false;
            while (ok==false) {
                System.out.print("Client email=");
                text = scan.nextLine();
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(text);
                boolean matchFound = m.matches();
                if (matchFound) {
                    c1.setEmail(text);
                    ok=true;
                }
                else {
                    System.out.println("Please enter a valid email address!");
                }
            }

            //Reading valid client ammount
            ok=false;
            while (ok==false) {
                System.out.print("Enter desired ammount=");
                String x = scan.nextLine();
                if (x.matches(".*\\d+.*"))
                {
                    c1.setSum(Integer.parseInt(x));
                    ok=true;
                }
                else {
                    System.out.println("Please insert a number!");
                }
            }

            //Reading valid period in months
            ok=false;
            while (ok==false) {
                System.out.print("Enter desired period in months=");
                String x = scan.nextLine();
                if (x.matches(".*\\d+.*"))
                {
                    c1.setPeriod(Integer.parseInt(x));
                    ok=true;
                }
                else {
                    System.out.println("Please insert a number!");
                }
            }

            ClientsArray.add(c1);
        }
        scanner.close();

        if (numcli>0) {
            for (int i = 0; i < numcli; i++) {
                MyResult myResult =  bestinterest(InvestorsArray, ClientsArray.get(i));
                DecimalFormat df = new DecimalFormat("#0.000");
                suminv=suminv-ClientsArray.get(i).getSum();

                if (suminv<0)
                    System.out.print("INSUFFICIENT FOUNDS for client named "+ClientsArray.get(i).getName()+" surnamed "+ClientsArray.get(i).getSurname());
                else
                    System.out.println("For client named "+ClientsArray.get(i).getName()+" surnamed "+ClientsArray.get(i).getSurname()+" total sum is "+myResult.getSecond()+" lei with the interest rate "+df.format(myResult.getThird()*100/ClientsArray.get(i).getSum())+"% and a monthly rate of "+df.format(myResult.getSecond()/ClientsArray.get(i).getPeriod())+" lei");
            }
        }

    }
}
