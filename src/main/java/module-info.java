module com.enrollmentsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires fr.brouillard.oss.cssfx;
    requires java.sql;

    opens com.enrollmentsystem to javafx.fxml;
    exports com.enrollmentsystem;
    exports com.enrollmentsystem.controllers.login;
    opens com.enrollmentsystem.controllers.login to javafx.fxml;
    opens com.enrollmentsystem.controllers.dashboard to javafx.fxml;
    opens com.enrollmentsystem.controllers to javafx.fxml;
}