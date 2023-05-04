import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Mandelbrot extends Application
{
	private static final int WIDTH = 1600;
	private static final int HEIGHT = 960;
	private static final double ASPECT_RATIO = (double)HEIGHT / (double)WIDTH;
	private static final double RADIUS = 2.2;
	private static final int DEFAULT_MAX_ITERATIONS = 70;
	private static final Complex TOP_LEFT = new Complex(-RADIUS, RADIUS * ASPECT_RATIO);
	private static final Complex BOTTOM_RIGHT = new Complex(RADIUS, -RADIUS * ASPECT_RATIO);
	
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("The Mandelbrot Set");
		primaryStage.setResizable(false);
		
		var layoutPane = new BorderPane();
		
		//
		// add the fractal UI
		//
		
		var fractalPane = new Pane();
		var fractalView = new FractalView(WIDTH, HEIGHT, TOP_LEFT, BOTTOM_RIGHT, DEFAULT_MAX_ITERATIONS);
		fractalPane.getChildren().add(fractalView);
		layoutPane.setCenter(fractalPane);
		
		//
		// add the UI controls
		//
		
		var controlPane = new HBox(4.0);
		controlPane.setAlignment(Pos.CENTER_LEFT);
		
		var iterationsLabel = new Label(" Iterations:");
		var iterationsTf = new TextField(Integer.toString(DEFAULT_MAX_ITERATIONS));
		iterationsTf.setFocusTraversable(false);
		
		var refreshBtn = new Button("Refresh");
 		refreshBtn.setOnAction(event -> {
			try
			{
				final var iterations = Integer.parseInt(iterationsTf.getText().trim());
				fractalView.draw(iterations);
			}
			catch (final NumberFormatException ex)
			{
				iterationsTf.setText("Error!");
			}
		});
		
		var zoomInBtn = new Button("Zoom In");
		zoomInBtn.setOnAction(event -> {
			fractalView.zoom(0.75);
		});
		
		controlPane.getChildren().addAll(iterationsLabel, iterationsTf, refreshBtn, zoomInBtn);
		layoutPane.setBottom(controlPane);
		
		primaryStage.setScene(new Scene(layoutPane));
        primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}