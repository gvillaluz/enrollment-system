module com.enrollmentsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires fr.brouillard.oss.cssfx;
    requires java.sql;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires mysql.connector.j;
    requires java.compiler;
    requires jbcrypt;

    opens com.enrollmentsystem to javafx.fxml;
    exports com.enrollmentsystem;
    exports com.enrollmentsystem.controllers.login;
    opens com.enrollmentsystem.controllers.login to javafx.fxml;
    opens com.enrollmentsystem.controllers.dashboard.core to javafx.fxml;
    opens com.enrollmentsystem.controllers.dashboard.academic to javafx.fxml;
    opens com.enrollmentsystem.controllers.dashboard.enrollment to javafx.fxml, org.controlsfx.controls;
    opens com.enrollmentsystem.controllers.dashboard.admin to javafx.fxml;
    opens com.enrollmentsystem.controllers.base to javafx.fxml;
    opens com.enrollmentsystem.controllers.shared to javafx.fxml;
}