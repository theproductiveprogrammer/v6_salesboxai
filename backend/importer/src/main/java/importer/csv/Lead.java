package importer.csv;

import com.opencsv.bean.CsvBindByName;

public class Lead {
    public Long tenantId;
    @CsvBindByName(column="First Name")
    public String firstName;
    @CsvBindByName(column="Last Name")
    public String lastName;
    @CsvBindByName
    public String email;
}
