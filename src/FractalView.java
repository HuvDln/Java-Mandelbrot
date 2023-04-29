import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FractalView extends ImageView
{	
	private int width, height;
	private Complex topLeft, bottomRight;
	
	public FractalView(int width, int height, Complex topLeft, Complex bottomRight)
	{
		this.width = width;
		this.height = height;
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	
	public final void draw(final int maxIterations)
	{
		var image = new WritableImage(width, height);
		var pixelWriter = image.getPixelWriter();
		
		var range = topLeft.subtract(bottomRight);
		var cxStep =  Math.abs(range.getReal() / width);
		var cyStep = -Math.abs(range.getImaginary() / height);
		var c = new Complex(topLeft.getReal(), topLeft.getImaginary());
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{	
				var z = new Complex();
				var iterations = maxIterations;
				while ((z.magnitudeSquared() < 4.0) && (iterations > 0))
				{
					z = z.square().add(c);
					iterations--;
				}
				
				var colour = chooseColour(iterations, maxIterations);
				pixelWriter.setColor(x, y, colour);
				
				c = c.addReal(cxStep);
			}
			
			c = c.setReal(topLeft.getReal());
			c = c.addImaginary(cyStep);
		}
		
		setImage(image);
	}
	
	private final Color chooseColour(final int iterations, final int maxIterations)
    {
        final var t = (double)iterations / (double)maxIterations;

        // using a smooth bernstein polynomial to generate RGB
        //
        final var r = (int)(9 * (1 - t) * t * t * t * 255);
        final var g = (int)(15 * (1 - t) * (1 - t) * t * t * 255);
        final var b =  (int)(8.5 * (1 - t) * (1 - t) * (1 - t) * t * 255);

		return Color.rgb(r, g, b);
    }
}