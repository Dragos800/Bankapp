import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Bank {
    static ArrayList<Client> ClientsArray = new ArrayList<Client>();
    static ArrayList<Investor> InvestorsArray = new ArrayList<Investor>();
    static Connection conn;
    static int TotalSum = 0;
    String myDriver = "com.mysql.cj.jdbc.Driver";
    String myUrl = "jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true";

    {
        try {
            conn = DriverManager.getConnection(myUrl, "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int AddClientToDB(Client client) {
        //ADD CLIENTS TO DATABASE
        DecimalFormat formatter = new DecimalFormat("#0.00");
        try {
            String querry = "SELECT * FROM client";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(querry);
            boolean ok = true;
            while (rs.next()) {
                String phone = rs.getString("phone");
                if (phone.matches(client.getNumber())) {ok=false;return 0; }
            }
            st.close();
            if (ok==true) {
                String query = " insert into client (name, surname, phone, email, sum, period)"
                        + " values (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, client.getName());
                preparedStmt.setString(2, client.getSurname());
                preparedStmt.setString(3, client.getNumber());
                preparedStmt.setString(4, client.getEmail());
                preparedStmt.setDouble(5, client.getSum());
                preparedStmt.setInt(6, client.getPeriod());
                preparedStmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void AddInvestorToDB(Investor investor) {
        try {
            String querry = "SELECT * FROM investor";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(querry);
            boolean ok = true;
            while (rs.next()) {
                String phone = rs.getString("phone");
                if (phone.matches(investor.getNumber())) ok = false;
            }
            st.close();
            if (ok == true) {

                String query = " insert into investor (name, surname, phone, email, sum, interest)"
                        + " values (? , ?, ?, ?, ?, ?)";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, investor.getName());
                preparedStmt.setString(2, investor.getSurname());
                preparedStmt.setString(3, investor.getNumber());
                preparedStmt.setString(4, investor.getEmail());
                preparedStmt.setDouble(5, investor.getSum());
                preparedStmt.setDouble(6, investor.getInterest());
                preparedStmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Investor createInvestor(String[] part) {
        return new Investor(part[0], part[1], part[2], part[3],
                Double.parseDouble(part[4]),
                Double.parseDouble(part[5]));
    }

    public void InitializeInvestors() {
        String fileName = "D:/Projects/abc.csv";
        try (Stream<String> readStream = Files.lines(Paths.get(fileName))){

            readStream.map(element -> element.split(","))
                    .forEach(parts -> {
                            Investor investor=createInvestor(parts);
                            boolean ok = true;
                            for (int j = 0; j <= InvestorsArray.size() - 1; j++)
                                if (InvestorsArray.get(j).getNumber() == investor.getNumber()) ok = false;
                            if (ok == true) {
                                TotalSum += investor.getSum();
                                InvestorsArray.add(createInvestor(parts));
                                AddInvestorToDB(investor);
                            }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BankInitializeClients(Scanner scan) {

        String text;
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
                if (text != null && !text.isEmpty() && !text.matches(".*\\d+.*")
                        && Character.isUpperCase(text.charAt(0))) {
                    c1.setName(text);
                    ok = true;
                } else System.out.println("Please enter a valid name, with first letter uppercase!");
            }

            //Reading valid client surname
            ok = false;
            while (ok == false) {
                System.out.print("Client surname=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && !text.matches(".*\\d+.*")
                        && Character.isUpperCase(text.charAt(0))) {
                    c1.setSurname(text);
                    ok = true;
                } else System.out.println("Please enter a valid surname, with first letter uppercase!");
            }

            //Reading valid phone number
            ok = false;
            while (ok == false) {
                System.out.print("Client phone number=");
                text = scan.nextLine();
                if (text != null && !text.isEmpty() && (Pattern.matches("[a-zA-Z]", text) == false)
                        && text.length() >= 10 && text.length() <= 11) {
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
            if (AddClientToDB(c1)==1){
                ClientsArray.add(c1);
            }
            else {
                i--;
                System.out.println("Client is already in the database, please enter a new one!");
            }

        }
        scan.close();
    }

    public static MyResult BestInterest(ArrayList<Investor> InvestorsArray, Client c1) {
        int k = 0;
        double csum = 0, su = 0, rate = 0;
        for (int i = 0; i < InvestorsArray.size(); i++) {

            su += InvestorsArray.get(i).getSum() + ((InvestorsArray.get(i).getSum()
                    * InvestorsArray.get(i).getInterest() / 100) / 12 * c1.getPeriod());
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

    public void Compute() {
        int ClientsNr = ClientsArray.size();
        if (ClientsNr > 0) {
            for (int i = 0; i < ClientsNr; i++) {
                MyResult myResult = BestInterest(InvestorsArray, ClientsArray.get(i));
                DecimalFormat df = new DecimalFormat("#0.00");
                TotalSum = TotalSum - ClientsArray.get(i).getSum();

                if (TotalSum < 0)
                    System.out.print("INSUFFICIENT FOUNDS for client named "
                            + ClientsArray.get(i).getName() + " surnamed " + ClientsArray.get(i).getSurname());
                else {
                    System.out.println("For client named " + ClientsArray.get(i).getName()
                            + " surnamed " + ClientsArray.get(i).getSurname());
                    System.out.println("Total sum is " + myResult.getSecond() + " lei ");

                    double aux1 = myResult.getThird() * 100 / ClientsArray.get(i).getSum();
                    System.out.println("With the interest rate " + df.format(aux1) + "% ");

                    double aux2 = myResult.getSecond() / ClientsArray.get(i).getPeriod();
                    System.out.println("And a monthly rate of " + df.format(aux2) + " lei");
                    System.out.println(" ");

                    try {
                    String query = " UPDATE client set interest=?, total_sum=?, monthly_rate=? WHERE phone=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setDouble(1, aux1);
                    preparedStmt.setDouble(2, myResult.getSecond());
                    preparedStmt.setDouble(3, Double.parseDouble(df.format(aux2)));
                    preparedStmt.setString(4,ClientsArray.get(i).getNumber());
                    preparedStmt.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}