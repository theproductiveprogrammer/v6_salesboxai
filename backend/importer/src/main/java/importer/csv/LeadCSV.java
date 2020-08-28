package importer.csv;

import com.opencsv.bean.CsvBindByName;

public class LeadCSV {
    @CsvBindByName(column="First Name")
    public String firstName;
    @CsvBindByName(column="Last Name")
    public String lastName;
    @CsvBindByName
    public String email;
}
