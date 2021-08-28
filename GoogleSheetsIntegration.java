import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.*;


public class GoogleSheetsIntegration {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * param //HTTP_TRANSPORT The network HTTP Transport.
     * return An authorized Credential object.
     * throws IOException If the credentials.json file cannot be found.
     */

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleSheetsIntegration.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void googleSheetsMailDropHandler() throws IOException, GeneralSecurityException, SQLException {
        ArrayList<MailDropObj> mailDropObjs = new DatabaseFunctions().gatheredRequiredDataFromMailDropTable();
        Ranges_SpreadsheetIDs ranges_spreadsheetIDs = new Ranges_SpreadsheetIDs();


        for (MailDropObj mailDropObj : mailDropObjs) {
            Sheets service = OpenGoogleSheets();

            List<List<Object>> values = Collections.singletonList(
                    Arrays.asList(
                            mailDropObj.getMailDropID(),
                            mailDropObj.getMailDropBusinessName(),
                            mailDropObj.getMailDropAddress(),
                            mailDropObj.getNotes()
                    )
                    // Additional rows ...
            );


            ValueRange body = new ValueRange()
                    .setValues(values);
            AppendValuesResponse result =
                    service.spreadsheets().values().append(ranges_spreadsheetIDs.MAIL_DROP_SPREADSHEETID, ranges_spreadsheetIDs.MAIL_DROP_RANGE, body)
                            .setValueInputOption("RAW")
                            .execute();
            System.out.printf("%d cells appended.", result.getUpdates().getUpdatedCells());
        }
        DatabaseFunctions databaseFunctions = new DatabaseFunctions();
        databaseFunctions.setIsPassedToGoogleSheetsTrue("MailDrop"); //Change to variable instead of string reference.
    }


    public static void main(String... args) throws IOException, GeneralSecurityException, SQLException {
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Would you like to update MailDrops Y/N");
        //String mailDropDecision = scanner.next().toLowerCase(Locale.ROOT);
        String mailDropDecision = "y";

        if (mailDropDecision.equals("y"))
        {
            googleSheetsMailDropHandler();
        }
        else
        {
            System.out.println("No table selected for updates, exiting...");
        }

        }

        private static Sheets OpenGoogleSheets() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
    }


}




