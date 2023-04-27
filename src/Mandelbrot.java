import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Mandelbrot extends Application
{
	private static int WIDTH = 1600;
	private static int HEIGHT = 960;
	
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("The Mandelbrot Set");
		
		var pane = new Pane();
		var fractalView = new FractalView(WIDTH, HEIGHT, 70);
		pane.getChildren().add(fractalView);
		
		primaryStage.setScene(new Scene(pane, WIDTH, HEIGHT));
        primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}