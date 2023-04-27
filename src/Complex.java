public class Complex
{
	private double a, b;
	
	public Complex()
	{
		a = 0.0;
		b = 0.0;
	}
	
	public Complex(double a, double b)
	{
		this.a = a;
		this.b = b;
	}
	
	public Complex setReal(double real)
	{
		return new Complex(real, b);
	}
	
	public double getReal()
	{
		return a;
	}
	
	public Complex setImaginary(double imaginary)
	{
		return new Complex(a, imaginary);
	}
	
	public double getImaginary()
	{
		return b;
	}
	
	public Complex square()
	{
		double real = (a * a) - (b * b);
		double imaginary = 2 * a * b;
		
		return new Complex(real, imaginary);
	}
	
	public Complex add(Complex c)
	{
		double real = a + c.getReal();
		double imaginary = b + c.getImaginary();
		
		return new Complex(real, imaginary);
	}
	
	public Complex addReal(double real)
	{
		return new Complex((a + real), b);
	}
	
	public Complex addImaginary(double imaginary)
	{
		return new Complex(a, (b + imaginary));
	}
	
	public double magnitude()
	{
		return Math.sqrt((a * a) + (b * b));
	}
	
	public double magnitudeSquared()
	{
		return (a * a) + (b * b);
	}
}