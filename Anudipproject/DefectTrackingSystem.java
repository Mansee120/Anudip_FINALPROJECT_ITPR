import java.sql.*;
import java.util.Scanner;

public class DefectTrackingSystem {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/defectdb";
        String user = "root"; // 🔑 Your MySQL username
        String pass = "M@nsi120"; // 🔑 Your MySQL password

        Scanner sc = new Scanner(System.in);

        try {
            // 1️⃣ Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2️⃣ Establish connection
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected to Defect Tracking Database");

            while (true) {
                System.out.println("\n=== DEFECT TRACKING SYSTEM MENU ===");
                System.out.println("1. Add New Defect");
                System.out.println("2. View All Defects");
                System.out.println("3. Update Defect");
                System.out.println("4. Delete Defect");
                System.out.println("5. Search Defect by ID");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> addDefect(con, sc);
                    case 2 -> viewDefects(con);
                    case 3 -> updateDefect(con, sc);
                    case 4 -> deleteDefect(con, sc);
                    case 5 -> searchDefect(con, sc);
                    case 6 -> {
                        System.out.println("🚪 Exiting... Goodbye!");
                        con.close();
                        sc.close();
                        System.exit(0);
                    }
                    default -> System.out.println("❌ Invalid choice! Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ➕ ADD NEW DEFECT
    public static void addDefect(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Defect ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Description: ");
        String desc = sc.nextLine();
        System.out.print("Enter Status (Open/In Progress/Closed): ");
        String status = sc.nextLine();
        System.out.print("Enter Priority (Low/Medium/High): ");
        String priority = sc.nextLine();
        System.out.print("Assigned To: ");
        String assignedTo = sc.nextLine();

        String query = "INSERT INTO defect VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, desc);
            ps.setString(4, status);
            ps.setString(5, priority);
            ps.setString(6, assignedTo);
            ps.executeUpdate();
            System.out.println("✅ Defect added successfully!");
        }
    }

    // 📋 VIEW ALL DEFECTS
    public static void viewDefects(Connection con) throws SQLException {
        String query = "SELECT * FROM defect";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            System.out.printf("%-5s %-20s %-10s %-10s %-15s%n", "ID", "Title", "Status", "Priority", "Assigned To");
            System.out.println("------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-10s %-10s %-15s%n",
                        rs.getInt("defect_id"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getString("assigned_to"));
            }
        }
    }

    // ✏️ UPDATE DEFECT
    public static void updateDefect(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Defect ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new Status: ");
        String status = sc.nextLine();
        System.out.print("Enter new Priority: ");
        String priority = sc.nextLine();
        System.out.print("Enter new Assigned To: ");
        String assignedTo = sc.nextLine();

        String query = "UPDATE defect SET status=?, priority=?, assigned_to=? WHERE defect_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setString(2, priority);
            ps.setString(3, assignedTo);
            ps.setInt(4, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Defect updated successfully!");
            else
                System.out.println("❌ No defect found with ID " + id);
        }
    }

    // ❌ DELETE DEFECT
    public static void deleteDefect(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Defect ID to delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM defect WHERE defect_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Defect deleted successfully!");
            else
                System.out.println("❌ No defect found with ID " + id);
        }
    }

    // 🔍 SEARCH DEFECT BY ID
    public static void searchDefect(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Defect ID: ");
        int id = sc.nextInt();

        String query = "SELECT * FROM defect WHERE defect_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("\n📋 Defect Details:");
                System.out.println("ID: " + rs.getInt("defect_id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Priority: " + rs.getString("priority"));
                System.out.println("Assigned To: " + rs.getString("assigned_to"));
            } else {
                System.out.println("❌ No defect found with ID " + id);
            }
        }
    }
}
