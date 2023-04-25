import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Mandelbrot extends Application
{
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("The Mandelbrot Set");
		
		var pane = new Pane();
		var fractalView = new FractalView();
		pane.getChildren().add(fractalView);
		
		primaryStage.setScene(new Scene(pane, 800, 600));
        primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}