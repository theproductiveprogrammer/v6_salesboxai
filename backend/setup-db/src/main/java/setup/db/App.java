package setup.db;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
            case "recreate-dbs":
                reCreateDBs(getConn(properties));
                break;
            case "create-tenants":
                createTenants(properties);
                break;
            case "create-workflow-meta":
                createWorkflowMeta(properties);
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
        System.out.println("        recreate-dbs:\t Drop & Create Required Databases");
        System.out.println("        create-tenants:\t Populate Sample Tenants");
        System.out.println("        create-workflow-meta:\t Populate Sample Workflow Rules");
    }

    private static Connection getConn(Properties properties) throws Exception {
        Class.forName(properties.getProperty("db.driverClassName"));
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    static final String[] dbs = {
            "v6_authenticator",
            "v6_biz"
    };
    private static void createDBs(Connection conn) throws Exception {
        for(String db : dbs) {
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate("create database " + db);
            } catch(Throwable t) {
                System.err.println(t);
            }
            stmt.close();
        }
    }

    private static void reCreateDBs(Connection conn) throws Exception {
        for(String db : dbs) {
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate("drop database " + db);
            } catch(Throwable t) {
                System.err.println(t);
            }
            stmt.close();
        }
        createDBs(conn);
    }

    private static void createTenants(Properties properties) throws Exception {
        String[] tenants = {
                "IBM (India)",
                "Dell (Singapore)",
                "Oracle Corp.",
                "Rehmans Restaurant",
        };
        for(String tenant : tenants) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"name\":\"");
            sb.append(tenant);
            sb.append("\"}");
            int status = post(properties.getProperty("tenant.url"), sb.toString());
            if(status < 200 || status > 299) System.err.println("Tenant creation failed:" + tenant);
        }
    }

    private static int post(String url_, String data) throws Exception {
        URL url = new URL(url_);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("content-type", "application/json");
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        out.write(data.getBytes());
        out.flush();
        out.close();
        int status = conn.getResponseCode();
        if(status < 200 || status > 299) {
            InputStream in = conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream();
            ByteArrayOutputStream body = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while((length = in.read(buffer)) != -1) body.write(buffer, 0, length);
            in.close();
            System.err.println(body.toString("UTF-8"));
        }
        return status;
    }

    private static void createWorkflowMeta(Properties props) throws Exception {
        String url = props.getProperty("workflowmeta.url");
        createWorkflowMeta(url, new WorkflowMeta("email", "Send Email", 168L));
        createWorkflowMeta(url, new WorkflowMeta("adaptive", "Adaptive", 64L));
        createWorkflowMeta(url, new WorkflowMeta("chat", "Chat", 168L));
        createWorkflowMeta(url, new WorkflowMeta("decide", "Decide", 40L, 2L));
        createWorkflowMeta(url, new WorkflowMeta("twitter", "Twitter", 96L));
        createWorkflowMeta(url, new WorkflowMeta("linkedin", "LinkedIn", 168L));
        createWorkflowMeta(url, new WorkflowMeta("salesforce", "Salesforce", 168L));
        createWorkflowMeta(url, new WorkflowMeta("sms", "SMS", 168L));
        createWorkflowMeta(url, new WorkflowMeta("listadd", "Add To List", 96L));
        createWorkflowMeta(url, new WorkflowMeta("facebook", "Facebook", 168L));
        createWorkflowMeta(url, new WorkflowMeta("meeting", "Meeting", 64L));
    }

    private static void createWorkflowMeta(String url, WorkflowMeta workflowMeta) throws Exception {
        JSONObject data = new JSONObject();
        data.put("code", workflowMeta.code);
        data.put("name", workflowMeta.name);
        data.put("iconszhint", workflowMeta.iconsize);
        if(workflowMeta.numlinks != null && workflowMeta.numlinks > 1) data.put("numlinks", workflowMeta.numlinks);
        int status = post(url, data.toString());
        if(status < 200 || status > 299) System.err.println("Failed to create step:" + workflowMeta.code);
    }

    static class WorkflowMeta {
        public WorkflowMeta(String code, String name, Long iconsize) {
            this.code = code;
            this.name = name;
            this.iconsize = iconsize;
        }

        public WorkflowMeta(String code, String name, Long iconsize, Long numlinks) {
            this.code = code;
            this.name = name;
            this.iconsize = iconsize;
            this.numlinks = numlinks;
        }

        String code;
        String name;
        Long iconsize;
        Long numlinks;
    }

}
