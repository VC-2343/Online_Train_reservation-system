import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Online_Reservation_System
{
    private static int minimum=1000;
    private static int maximum=9999;

    public static class userdetails
    {
        private String usernameID;
        private String password;
        Scanner sc=new Scanner(System.in);

        public userdetails(){
        }

        public String getusername()
        {
            System.out.println("Enter the username:=");
            usernameID=sc.nextLine();
            return usernameID;
        }

        public String getpassword()
        {
            System.out.println("Enter the password:=");
            password=sc.nextLine();
            return password;
        }
    }

    public static class records{
        private int PNRnumber;
        private String Nameofpassenger;
        private String TrainNumber;
        private String From;
        private String To;
        private String journeydate;
        private String classType;

        Scanner sc=new Scanner(System.in);

        public int getPnrnumber() {
            Random random = new Random();
            int pnrNumber = random.nextInt(maximum - minimum + 1) + minimum;
            return pnrNumber;
        }

        public String getPassengerName() {
            System.out.println("Enter the passenger name -- ");
            Nameofpassenger = sc.nextLine();
            return Nameofpassenger;
        }

        public String gettrainNumber()
        {
            System.out.println("Enter the train number -- ");
            TrainNumber = sc.nextLine();
            return TrainNumber;
        }

        public String getclassType() 
        {
            System.out.println("Enter the class type -- ");
            classType = sc.nextLine();
            return classType;
        }

        public String getDateOfJourney() 
        {
            System.out.println("Enter the Journey date as format:'YYYY-MM-DD'--");
            journeydate = sc.nextLine();
            return journeydate;
        }

        public String getStarting() 
        {
            System.out.println("Enter the starting place -- ");
            From = sc.nextLine();
            return From;
        }

        public String getDestination() 
        {
            System.out.println("Enter the destination place --  ");
            To = sc.nextLine();
            return To;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        userdetails u1 = new userdetails();
        String username = u1.getusername();
        String password = u1.getpassword();

        String url = "jdbc:mysql://localhost:3306/treservation";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("User Connection Granted.\n");
                while (true) {
                    String InsertQuery = "insert into reservations values (?, ?, ?, ?, ?, ?, ?)";
                    String DeleteQuery = "DELETE FROM reservations WHERE PNRnumber = ?";
                    String ShowQuery = "Select * from reservations";

                    System.out.println("Enter the choice : ");
                    System.out.println("1. Insert Record.\n");
                    System.out.println("2. Delete Record.\n");
                    System.out.println("3. Show All Records.\n");
                    System.out.println("4. Exit.\n");
                    int choice = sc.nextInt();
                               
                    if (choice == 1) {
                        records p1 = new records();
                        int PNRnumber = p1.getPnrnumber();
                        String NameOfPassenger = p1.getPassengerName();
                        String TrainNumber = p1.gettrainNumber();
                        String classType = p1.getclassType();
                        String journeydate = p1.getDateOfJourney();
                        String From = p1.getStarting();
                        String To = p1.getDestination();

                        try (PreparedStatement preparedStatement = connection.prepareStatement(InsertQuery)) {
                            preparedStatement.setInt(1,PNRnumber);
                            preparedStatement.setString(2,NameOfPassenger); 
                            preparedStatement.setString(3,TrainNumber);
                            preparedStatement.setString(4,classType);
                            preparedStatement.setString(5, journeydate);
                            preparedStatement.setString(6, From);
                            preparedStatement.setString(7, To);

                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Record added successfully.");
                            } else {
                                System.out.println("No records were added.");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }

                    } else if(choice==2){
                        System.out.println("enter the pnrnumber to delete the record");
                        int PNRnumber=sc.nextInt();
                        try (PreparedStatement preparedStatement = connection.prepareStatement(DeleteQuery)) {
                            preparedStatement.setInt(1,PNRnumber);
                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Record deleted successfully.");
                            } else{
                                System.out.println("No records were deleted,check the pnr number.");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    } else if(choice==3){
                        try (PreparedStatement preparedStatement = connection.prepareStatement(ShowQuery);
                                        ResultSet resultSet = preparedStatement.executeQuery()) {
                                    System.out.println("\nPRINTING ALL RECORDS.\n");
                                    while(resultSet.next()){
                                        String PNRnumber= resultSet.getString("PNRnumber");
                                        String NameOfPassenger = resultSet.getString("NameOfPassenger");
                                        String TrainNumber = resultSet.getString("TrainNumber");
                                        String classType = resultSet.getString("classType");
                                        String journeydate = resultSet.getString("journeydate");
                                        String From = resultSet.getString("FromPlace");
                                        String To = resultSet.getString("ToPlace");

                                        System.out.println("pnr number.\n :-"+PNRnumber);
                                        System.out.println("passenger's name :-"+NameOfPassenger);
                                        System.out.println("train number :-"+TrainNumber);
                                        System.out.println("class type :-"+classType);
                                        System.out.println("date of journey :-"+journeydate);
                                        System.out.println("from location :-"+From);
                                        System.out.println("to location.\n:-"+To);
                        }}
                        catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    } else if (choice == 4) {
                        System.out.println("Exit.\n");
                        break;
                    } else {
                        System.out.println("Choice entered is invalid.\n");
                    }
                }

            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC driver: " + e.getMessage());
        }

        sc.close();
    }
}
