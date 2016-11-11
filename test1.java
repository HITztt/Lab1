package lab1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class test1 {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSimplify1() {
		String result = "";
		String strinput = "xxx+4 *y   -6.0";
		polynomial test = new polynomial();
		polynomial root=test.expression(strinput);
		String stri = "!simplify";
		root.simplify(stri,root);
	    result= root.display();
		assertEquals("xxx+4y-6",result);

		
	}
	@Test
	public void testSimplify2() {
		String result = "";
		String strinput = "xxx+4  *y";
		polynomial test = new polynomial();
		polynomial root=test.expression(strinput);
		String stri = "!simplify xxx=2";
		root.simplify(stri,root);
	    result= root.display();
		assertEquals("2+4y",result);
	}
	@Test
	public void testSimplify3() {
		String result = "";
		String strinput = "xxx+4 *y +6x-7*aa^2";
		polynomial test = new polynomial();
		polynomial root=test.expression(strinput);
		String stri = "!simplify xxx=2 y=2.1";
		root.simplify(stri,root);
	    result= root.display();
		assertEquals("2+8.4+6x-7aa^2",result);
	}


}
