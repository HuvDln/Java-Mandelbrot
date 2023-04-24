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
	
	public double getReal()
	{
		return a;
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
}