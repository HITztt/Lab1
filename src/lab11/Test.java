package lab11;
import lab11.polynomial;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

	//ex.2xy^2*abc +	34p^1*d*q*u^2 - 24	d^7*i^7*o
	public static void main(String []args) throws IOException{
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		String str = "";
		String str_simplify = "!simplify";
		String str_diff = "!d/d";
		Pattern p_simplify = Pattern.compile(str_simplify);
		Pattern p_diff = Pattern.compile(str_diff);
		polynomial root = new polynomial();
		do{
			System.out.print("> ");
			str = br.readLine();
			Matcher m_simplify = p_simplify.matcher(str);
			Matcher m_diff = p_diff.matcher(str);
			if(m_simplify.find()){
				if(str.equals("!simplify")){
					root.display();
					System.out.println("");
				}
				else {
                        System.out.println("!!!!");
					root = root.simplify(str,root);
					root.display();
					System.out.println("");
				}

			}
			else if(m_diff.find()){
				if(str.equals("!d/d")){
					System.out.println("Error input!");

				}
				else
				{
					root = root.diff(str,root);
					root.display();
					System.out.println("");
				}
			}
			else{
				str = str.replaceAll("[\\s]", "");
				root = root.expression(str);
				root.display();
				System.out.println("");
			}
		}while(!str.equals("quit"));
		return;
	}
}
