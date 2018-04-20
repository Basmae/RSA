import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
public class RSA {
	static BigInteger p,q,n,phi,e,d,B,a,B2,r,two,z,x,y;
	static Scanner input=new Scanner(System.in);

		public static String Miller()
	{
		
		long u=0;
		Random random = new Random();
		B=new BigInteger(512, random);
		B2=B.subtract(BigInteger.ONE);
		r=B2;
		two=BigInteger.ONE;
		two=two.add(two);
		while(r.mod(two)==BigInteger.ZERO)
		{
			r=r.divide(two);
			u+=1;
		}
		B2=B2.subtract(BigInteger.ONE);
		
		for(int i=1;i<=10;i++)
		{
			do
			{
			random = new Random();
			a=new BigInteger(512, random);
			}
			while(a.compareTo(BigInteger.ONE)!=1||a.compareTo(B2)==1);
			z=squareMultiply(a,r,B);
			z=z.mod(B);
			if(!z.equals(BigInteger.ONE)&&!z.equals(B.subtract(BigInteger.ONE)))
			{
				for(int j=1;j<=u-1;j++)
				{
					z=z.pow(2);
					z=z.mod(B);
					if(z.equals(BigInteger.ONE))
						return("composite");
				}
				if(!z.equals(B.subtract(BigInteger.ONE)))
					return("composite");
					
			}
		}
		return("p is prime");
	}
	public static	BigInteger modMulInverse(BigInteger a,BigInteger m)
		{
		    BigInteger x = BigInteger.ZERO,y = BigInteger.ONE,u = BigInteger.ONE,v = BigInteger.ZERO; 
		    BigInteger e = m,f = a;
		    BigInteger c,d,q,r;
		    while(!f.equals(BigInteger.ONE))
		    {
		        q =e.divide(f) ;
		        r = e.mod(f);
		        c = x.subtract(q.multiply(u));
		        d = y.subtract(q.multiply(v));
		        x = u;
		        y = v;
		        u = c;
		        v = d;
		        e = f;
		        f = r;
		    } 
		    u =u.add(m);
		    u=u.mod(m);
		    while(u.compareTo(BigInteger.ZERO)==-1)
		    {
		    	 u =u.add(m);
				 u=u.mod(m);
		    }
		    return u;
		}
	public static BigInteger squareMultiply(BigInteger x,BigInteger e,BigInteger pr)
	{
		BigInteger z=x;
		String s=e.toString(2);
		for(int i=1;i<s.length();i++)
		{
			if(s.charAt(i)=='1')
			{
				z=z.pow(2);
				z=z.multiply(x);
			}
			else
				z=z.pow(2);
			z=z.mod(pr);
		}
		return z;
			
	}
	public static void encrypt(BigInteger n,BigInteger e)
	{
		y=squareMultiply(x,e,n);
		y=y.mod(n);
	}
	public static BigInteger CRT()
	{
		BigInteger yp,yq,dp,dq,xp,xq,cp,cq,dec1,dec2;
		yp=y.mod(p);
		yq=y.mod(q);
		dp=d.mod(p.subtract(BigInteger.ONE));
		dq=d.mod(q.subtract(BigInteger.ONE));
		xp=squareMultiply(yp,dp,p);
		xp=xp.mod(p);
		xq=squareMultiply(yq,dq,q);
		xq=xq.mod(q);
		cp=modMulInverse(q,p);
		cq=modMulInverse(p,q);
		dec1=q.multiply(cp);
		dec1=dec1.multiply(xp);
		dec2=p.multiply(cq);
		dec2=dec2.multiply(xq);
		dec1=dec1.add(dec2);
		dec1=dec1.mod(n);
		return dec1;
	}
	public static void main(String args[])
	{
		String type;
		type=Miller();
		while(type=="composite")
			type=Miller();
		p=B;
		type=Miller();
		while(type=="composite")
			type=Miller();
		q=B;
		n=p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		Random random = new Random();
		//;
		e = new BigInteger(512, random);
		
	/*e=BigInteger.ONE;
	e=e.add(BigInteger.ONE);
	*/
		while(!phi.gcd(e).equals(BigInteger.ONE)&&e.compareTo(phi)==-1)
		{
			e=e.add(BigInteger.ONE);
		}
		d=modMulInverse(e,phi);
		System.out.println("Enter your plain text:");
		String s,s2="",s3="";
		s=input.next();
		x=new BigInteger(s.getBytes());
		encrypt(n,e);
		s2=new String( y.toByteArray());
		System.out.println("your encrypted text: "+s2);
		BigInteger dec=CRT();
		s2=new String(dec.toByteArray());
		System.out.println("your decrypted text: "+s2);
		
	}

}
