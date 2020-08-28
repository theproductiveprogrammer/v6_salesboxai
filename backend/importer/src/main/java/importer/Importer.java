package importer;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import importer.csv.LeadCSV;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
public class Importer {
    @Post("/importleads")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public boolean importLeads(CompletedFileUpload importFile) throws IOException {
        System.out.println(importFile.getFilename());
        Reader reader = new BufferedReader(new InputStreamReader(importFile.getInputStream()));
        CsvToBean<LeadCSV> csv = new CsvToBeanBuilder<LeadCSV>(reader).withType(LeadCSV.class).build();
        for(LeadCSV leadCSV : csv) {
        }
        return true;
    }
}
