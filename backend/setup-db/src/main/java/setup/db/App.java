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
            "v6_biz",
            "v6_importer",
            "v6_activities",
            "v6_conversations",
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
        createWorkflowSteps(props);
        createWorkflowEvents(props);
    }

    private static void createWorkflowSteps(Properties props) throws Exception {
        String url = props.getProperty("workflowmeta.steps.url");
        createWorkflowMeta(url, new WorkflowMeta(1L, "email", "Send Email", "workflow.engine.handler.SendEmail",168L));
        createWorkflowMeta(url, new WorkflowMeta(2L, "adaptive", "Adaptive", "workflow.engine.handler.SendAdaptive", 64L));
        createWorkflowMeta(url, new WorkflowMeta(3L, "chat", "Chat", "workflow.engine.handler.Chat", 168L));
        createWorkflowMeta(url, new WorkflowMeta(4L, "decide", "Decide",40L, "workflow.engine.handler.Decide", 2L));
        createWorkflowMeta(url, new WorkflowMeta(5L, "twitter", "Twitter", "workflow.engine.handler.Tweet", 96L));
        createWorkflowMeta(url, new WorkflowMeta(6L, "linkedin", "LinkedIn", "workflow.engine.handler.ConnectLinkedIn", 168L));
        createWorkflowMeta(url, new WorkflowMeta(7L, "salesforce", "Salesforce", "workflow.engine.handler.SendToSalesForce", 168L));
        createWorkflowMeta(url, new WorkflowMeta(8L, "sms", "SMS", "workflow.engine.handler.SendSMS", 168L));
        createWorkflowMeta(url, new WorkflowMeta(9L, "listadd", "Add To List", "workflow.engine.handler.AddToList", 96L));
        createWorkflowMeta(url, new WorkflowMeta(10L, "facebook", "Facebook", "workflow.engine.handler.ConnectFacebook", 168L));
        createWorkflowMeta(url, new WorkflowMeta(11L, "meeting", "Meeting", "workflow.engine.handler.SetupMeeting", 64L));
    }

    private static void createWorkflowEvents(Properties props) throws Exception {
        String url = props.getProperty("workflowmeta.events.url");
        createWorkflowMeta(url, new WorkflowMeta(1L, "evt/new.lead", "Event: New Lead", "workflow.engine.handler.EventStart", 96L));
        createWorkflowMeta(url, new WorkflowMeta(2L, "evt/email.open", "Event: Email Open", "workflow.engine.handler.EventStart",96L));
        createWorkflowMeta(url, new WorkflowMeta(3L, "evt/link.click", "Event: Link Click", "workflow.engine.handler.EventStart",96L));
        createWorkflowMeta(url, new WorkflowMeta(4L, "evt/email.reply", "Event: Email Reply", "workflow.engine.handler.EventStart",96L));
        createWorkflowMeta(url, new WorkflowMeta(5L, "evt/chat.reply", "Event: Chat Reply", "workflow.engine.handler.EventStart",96L));
    }

    private static void createWorkflowMeta(String url, WorkflowMeta workflowMeta) throws Exception {
        JSONObject data = new JSONObject();
        data.put("code", workflowMeta.code);
        data.put("name", workflowMeta.name);
        data.put("iconszhint", workflowMeta.iconsize);
        data.put("handler", workflowMeta.handler);
        if(workflowMeta.numlinks != null && workflowMeta.numlinks > 1) data.put("numlinks", workflowMeta.numlinks);
        int status = post(url, data.toString());
        if(status < 200 || status > 299) System.err.println("Failed to create step:" + workflowMeta.code);
    }

    static class WorkflowMeta {

        public WorkflowMeta(Long id, String code, String name, String handler, Long iconsize) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.iconsize = iconsize;
            this.handler = handler;
        }

        public WorkflowMeta(Long id, String code, String name, Long iconsize, String handler, Long numlinks) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.iconsize = iconsize;
            this.handler = handler;
            this.numlinks = numlinks;
        }

        Long id;
        String code;
        String name;
        Long iconsize;
        Long numlinks;
        String handler;
    }

}
