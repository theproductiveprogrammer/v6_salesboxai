package importer;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import importer.csv.Lead;
import importer.db.Import;
import importer.repo.ImportRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
public class Importer {
    private static final Logger logger = LoggerFactory.getLogger(Importer.class);

    @Inject
    ImportRepository importRepository;

    @Post("/importleads")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public boolean importLeads(Authentication authentication, CompletedFileUpload importFile) throws IOException {
        Long tenantId = (Long) authentication.getAttributes().get("tenant");

        Reader reader = new BufferedReader(new InputStreamReader(importFile.getInputStream()));
        CsvToBean<Lead> csv = new CsvToBeanBuilder<Lead>(reader).withType(Lead.class).build();

        Import import_ = recordStart(tenantId, importFile.getFilename());
        Long num = 0L;
        for(Lead lead : csv) {
            lead.tenantId = tenantId;
            System.out.println(lead.firstName + " " + lead.lastName + " " + lead.email + " (tenant " + lead.tenantId + ")");
            num++;
        }
        import_.setCount(num);
        import_ = importRepository.save(import_);
        logger.info("Import(" + import_.getId() + ") done with " + num + " leads from " + import_.getImportFile());
        return true;
    }

    private Import recordStart(Long tenantId, String filename) {
        logger.info("Starting import for " + filename + " (tenant: " + tenantId + ")");

        Import import_ = new Import();
        import_.setStarted(new Date());
        import_.setTenantId(tenantId);
        import_.setImportFile(filename);
        return import_;
    }

    @Get("/imports")
    public List<Import> imports(Authentication authentication) {
        Long tenantId = (Long)authentication.getAttributes().get("tenant");
        return importRepository.findByTenantId(tenantId);
    }
}
