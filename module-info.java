module BreakOutTest {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.media;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml;
}
