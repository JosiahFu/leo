package org.davincischools.leo.database.admin;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackageClasses = Database.class)
public class AdminUtils {

  private static final Logger log = LogManager.getLogger();

  @Value("${districtName:}")
  String districtName;

  @Value("${emailAddress:}")
  String emailAddress;

  private void createDistrict(ApplicationContext context, List<String> args) {
    Database db = context.getBean(Database.class);
    checkArgument(districtName != null, "--districtName required.");

    log.atInfo().log("Creating district: " + districtName);

    db.getDistrictRepository()
        .save(new District().setCreationTime(Instant.now()).setName(districtName));

    log.atInfo().log("Done.");
  }

  private void createAdmin(ApplicationContext context, List<String> args) {
    Database db = context.getBean(Database.class);
    checkArgument(districtName != null, "--districtName required.");
    checkArgument(emailAddress != null, "--emailAddress required.");

    District district = db.getDistrictRepository().findByName(districtName);
    checkArgument(district != null, "--districtName doesn't exist.");

    String password =
        new RandomStringGenerator.Builder()
            .withinRange(new char[] {'a', 'z'}, new char[] {'0', '9'})
            .build()
            .generate(20);
    db.getUserXRepository()
        .save(
            UserUtils.setPassword(
                new UserX()
                    .setCreationTime(Instant.now())
                    .setAdminX(
                        db.getAdminXRepository().save(new AdminX().setCreationTime(Instant.now())))
                    .setDistrict(district)
                    .setAdminX(
                        db.getAdminXRepository().save(new AdminX().setCreationTime(Instant.now())))
                    .setFirstName("NEW ADMIN")
                    .setLastName("NEW ADMIN")
                    .setEmailAddress(emailAddress),
                password));

    log.atInfo().log("Done. Created user {} with password {}.", emailAddress, password);
  }

  public static void main(String[] argsArray) throws IOException {
    ApplicationContext context = SpringApplication.run(AdminUtils.class, argsArray);
    List<String> args =
        new ArrayList<>(context.getBean(ApplicationArguments.class).getNonOptionArgs());
    checkArgument(args.size() >= 1);

    switch (args.get(0)) {
      case "createAdmin":
        context.getBean(AdminUtils.class).createAdmin(context, args.subList(1, args.size()));
        break;
      case "createDistrict":
        context.getBean(AdminUtils.class).createDistrict(context, args.subList(1, args.size()));
        break;
      default:
        throw new IllegalArgumentException("Usage: AdminUtils command [args].");
    }

    System.exit(0);
  }
}
