package setup.db;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            showHelp();
            return;
        }
        Properties properties = new Properties();
        InputStream is = App.class.getClassLoader().getResourceAsStream("application.properties");
        properties.load(is);

        switch(args[0]) {
            case "create-dbs":
                createDBs(getConn(properties));
                break;
            case "create-tenants":
                createTenants(properties);
                break;
            default:
                System.out.println("Did not understand: " + args[0]);
                showHelp();
        }
    }

    private static void showHelp() {
        System.out.println("Usage:");
        System.out.println("    java App <task>");
        System.out.println("    or");
        System.out.println("    ./gradlew run --args=<task>");
        System.out.println("  where <task>:");
        System.out.println("        create-dbs:\t Create Required Databases");
        System.out.println("        create-tenants:\t Populate Sample Tenants");
    }

    private static Connection getConn(Properties properties) throws Exception {
        Class.forName(properties.getProperty("db.driverClassName"));
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    private static void createDBs(Connection conn) throws Exception {
        String[] dbs = {
          "v6_authenticator",
        };
        for(String db : dbs) {
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate("create database " + db);
            } catch(Throwable t) {
                System.out.println(t);
            }
            stmt.close();
        }
    }

    private static void createTenants(Properties properties) throws Exception {
        String[] tenants = {
                "IBM (India)",
                "Dell (Singapore)",
                "Oracle Corp.",
                "Rehmans Restaurant",
        };
        URL url = new URL(properties.getProperty("tenant.url"));
        for(String tenant : tenants) {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write("{\"name\":\"".getBytes());
            out.write(tenant.getBytes());
            out.write("\"}".getBytes());
            out.flush();
            out.close();
            int status = conn.getResponseCode();
            if(status < 200 || status > 299) throw new Exception("Tenant creation failed:" + tenant);
        }
    }

}
