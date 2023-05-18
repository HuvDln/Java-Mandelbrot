import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FractalView extends ImageView
{	
	private final int width, height;
	private int maxIterations;
	private Complex topLeft, centre, bottomRight, range;
	private boolean busy;
	
	public FractalView(int width, int height, Complex topLeft, Complex bottomRight, int defaultMaxIterations)
	{
		this.width = width;
		this.height = height;
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		maxIterations = defaultMaxIterations;
		centre = new Complex();
		range = new Complex();
		busy = false;
		
		final var centreX = width >> 1;
		final var centreY = height >> 1;
		setOnMouseClicked(event -> {
			if (event.isShiftDown() && !busy)
			{
				final var scaleX  = (centreX - event.getX()) / width;
				final var scaleY  = (centreY - event.getY()) / height;
				
				centre.setReal(centre.getReal() + (range.getReal() * scaleX));
				centre.setImaginary(centre.getImaginary() + (range.getImaginary() * scaleY));
				
				topLeft.setReal(centre.getReal() + (range.getReal() / 2.0));
				topLeft.setImaginary(centre.getImaginary() + (range.getImaginary() / 2.0));
				bottomRight.setReal(centre.getReal() - (range.getReal() / 2.0));
				bottomRight.setImaginary(centre.getImaginary() - (range.getImaginary() / 2.0));
				
				draw(maxIterations);
			}
		});
		
		draw(defaultMaxIterations);
	}
	
	public final void draw(final int maxIterations)
	{
		busy = true;
		this.maxIterations = maxIterations;
		
		range.setReal(topLeft.getReal() - bottomRight.getReal());
		range.setImaginary(topLeft.getImaginary() - bottomRight.getImaginary());

		final var image = new WritableImage(width, height);
		final var pixelWriter = image.getPixelWriter();
		
		final var cxStep =  Math.abs(range.getReal() / width);
		final var cyStep = -Math.abs(range.getImaginary() / height);
//		var c = new Complex(topLeft.getReal(), topLeft.getImaginary());
		
		final var sliceHeight = height >> 3;
		final var cpu = new Thread[8];
		for (var slice = 0; slice < 8; slice++)
		{
			final var yStart = slice * sliceHeight;
			final var yEnd = yStart + sliceHeight;
			cpu[slice] = new Thread(() -> drawSlice(yStart, yEnd, cxStep, cyStep, pixelWriter));
			cpu[slice].start();
		}
		
		for (var slice = 0; slice < 8; slice++)
		{
			try
			{
				cpu[slice].join();
			}
			catch(final InterruptedException ignored)
			{
			}
		}
		
		setImage(image);
		busy = false;
	}
	
	public final void drawSlice(final int yStart, final int yEnd, final double cxStep, final double cyStep, final PixelWriter pixelWriter)
	{
		var c = new Complex(topLeft.getReal(), topLeft.getImaginary() + ((double)yStart * cyStep));
		for (int y = yStart; y < yEnd; y++)
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
				
				c.addReal(cxStep);
			}
			
			c.setReal(topLeft.getReal());
			c.addImaginary(cyStep);
		}
	}
	
	public final boolean isBusy()
	{
		return busy;
	}
	
	//
	// original, non thread draw method
	//
	
/*	public final void draw(final int maxIterations)
	{
		this.maxIterations = maxIterations;
		
		range.setReal(topLeft.getReal() - bottomRight.getReal());
		range.setImaginary(topLeft.getImaginary() - bottomRight.getImaginary());

		var image = new WritableImage(width, height);
		var pixelWriter = image.getPixelWriter();
		
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
				
				c.addReal(cxStep);
			}
			
			c.setReal(topLeft.getReal());
			c.addImaginary(cyStep);
		}
		
		setImage(image);
	} */
	
	public void zoom(final double zoomFactor)
	{
		topLeft.setReal(centre.getReal() + zoomFactor * range.getReal() / 2.0);
		topLeft.setImaginary(centre.getImaginary() + zoomFactor * range.getImaginary() / 2.0);
		
		bottomRight.setReal(centre.getReal() - zoomFactor * range.getReal() / 2.0);
		bottomRight.setImaginary(centre.getImaginary() - zoomFactor * range.getImaginary() / 2.0);
				
		draw(maxIterations);
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