package lab11;
import lab11.polynomial_method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DecimalFormat;
public class polynomial {
	float coef;
	int exponent;
	int flag;	// 0 -> item; 1 -> op; 2-> end sign;
	char op;
	String var;
	polynomial next;
	
	public polynomial(){
		flag = 2;
	}
	
	public polynomial(char o){
		flag = 1;
		op = o;
	}
	
	public polynomial(float c, String v, int e){
		flag = 0;
		coef = c;
		exponent = e;
		var = v;
		next = new polynomial();			//将next初始化为结束标志
	}
		
	
	public void display(){
		DecimalFormat df=(DecimalFormat) DecimalFormat.getInstance();
		polynomial tmp = this;
		if(tmp.next == null)
			return;
		while(tmp.next.flag != 2){			//只要没到终点就执行线性访问，对系数和指数为1的情况输出格式化
			tmp = tmp.next;
			if(tmp.flag == 1)				//多余符号处理
				if(tmp.next.var.equals(""))
					if(tmp.next.coef == 1)
						if(tmp.next.next.flag == 2)
							tmp.flag = 2;
						else{
							tmp.op = tmp.next.next.op;
							tmp.next = tmp.next.next.next;
						}
			if(tmp.flag == 0){
				if(tmp.var.equals("")){	//纯系数
					System.out.print(df.format(Math.pow(tmp.coef, tmp.exponent)));
				}
				else{
					if(tmp.exponent == 1)
						if(tmp.coef == 1)		//可能为空
								System.out.print(tmp.var);
						else if(tmp.coef == -1)
							System.out.print("-"+tmp.var);
						else
							System.out.print(df.format(tmp.coef) + tmp.var);
					else
						if(tmp.coef == 1)
							System.out.print(tmp.var + "^" + tmp.exponent);
						else if(tmp.coef == -1)
							System.out.print("-" + tmp.var + "^" + tmp.exponent);
						else
							System.out.print(df.format(tmp.coef) + tmp.var + "^" + tmp.exponent);
				}
			}
			else if (tmp.flag == 1)
				System.out.print(tmp.op);
			else
				break;
		}
	}
	
	public polynomial expression(String str){
		polynomial tmp = this;
		polynomial root_new = this;
		String str_pd = "[\\+\\-]";					//将多项式根据+，-符号分成各单项
		Pattern p_pd = Pattern.compile(str_pd);
		String []item = str.split(str_pd);
		Matcher m_pd = p_pd.matcher(str);
		
		String str_mul = "[\\*]";					//将各单项根据*符号分成各变量
		Pattern p_mul = Pattern.compile(str_mul);
		
		String str_coef = "^[0-9\\.]+";				//对各变量根据起始数字确定系数
		Pattern p_coef = Pattern.compile(str_coef);
		String str_var = "[a-zA-Z]+";				//对各变量根据字母确定变量
		Pattern p_var = Pattern.compile(str_var);
		String str_exponent = "[\\^]";			//对各变量根据^后数字确定指数
		Pattern p_exponent = Pattern.compile(str_exponent);
		float coef = 1;
		float c;
		int e;
		int num_mul;
		String v;
		
		polynomial tmp_coef = tmp;
		for(String elm_item : item){				//对每一项
			num_mul = 0;
			String []var = elm_item.split(str_mul);
			Matcher m_mul = p_mul.matcher(elm_item);
			while(m_mul.find())				//求*符号数
				num_mul++;
			for(String elm_var : var){
				Matcher m_var = p_var.matcher(elm_var);
				if(!m_var.find())
					num_mul--;
			}
			for(String elm_var : var){				//对每一项中每个变量
				Matcher m_coef = p_coef.matcher(elm_var);
				Matcher m_exponent = p_exponent.matcher(elm_var);
				if(m_coef.find())					//求和系数综合到第一项
					coef *= Float.parseFloat(m_coef.group(0));
				c = 1;
				Matcher m_var = p_var.matcher(elm_var);
				if(m_var.find())					//确定变量
					v = m_var.group(0);
				else{								//纯系数

					if(m_exponent.find()){			//若有指数
						String []item_exponent = elm_var.split(str_exponent);
						e = Integer.parseInt(item_exponent[1], 10);
					}
					else
						e = 1;
					coef = (float)Math.pow(coef, e);
					if(coef == 1)
						coef = 0;

					continue;
				}
				
				if(m_exponent.find()){				//确定指数
					String []item_exponent = elm_var.split(str_exponent);
					e = Integer.parseInt(item_exponent[1], 10);
				}
				else
					e = 1;
				tmp.next = new polynomial(c, v, e);
				tmp = tmp.next;
				
				if(num_mul > 0){					//*号连接
					tmp.next = new polynomial('*');	
					num_mul--;
					tmp = tmp.next;
				}
			}
			if(tmp == tmp_coef){			//常数项
				tmp.next = new polynomial(1, "", 1);
				tmp = tmp.next;
			}			
			tmp_coef = tmp_coef.next;
			tmp_coef.coef *= coef;
			coef = 1;
			
			if(m_pd.find()){						//连接+，-符号
				tmp.next = new polynomial(m_pd.group().charAt(0));	
				tmp = tmp.next;
			}
			tmp_coef = tmp;
		}
			polynomial_method  method = new polynomial_method();	
			return method.merge(root_new);
	}
	
	public polynomial simplify(String input, polynomial root){
		 polynomial temp = root;
		 polynomial_method method = new polynomial_method();
		 String[] count = input.split(" ");//按空格分开
		 int num = count.length;
		 String[] s_var= new String[num-1];
		 float []value= new float[100];
		 for (int i = 1; i < num; i++)
		 {
			 s_var[i-1] = method.GetVarStr(count[i],0);			 
			 int len = count[i].length();
			 String n = count[i].substring(s_var[i-1].length()+1, len);
			 float v =  Float.parseFloat(n);
			 value[i-1] = v;
		 }
		 boolean havevar = false;
		 while(temp.next.flag != 2)//表达式没结束
		 {
			 temp=temp.next;
			 if(temp.flag==0)
			 {
				 for(int k=0; k < num-1; k++)
				 {
					 if(temp.var.equals(s_var[k]))
					 {
						 havevar = true;
						 if(temp.exponent == 1)
						 {
							 temp.coef = temp.coef * value[k];
							 temp.var ="";
							 break;
						 }
						 else
						 {
							 temp.coef = temp.coef * (float)Math.pow(value[k],temp.exponent);
							 temp.var ="";
							 temp.exponent=1;
							 break;
						 }	
					 }
				 }
			}
		}
		 if(havevar == false)
		 {
			 System.out.println("Error, no variable!"); 
		 }

		return method.merge(root);
	}
	
	
	public polynomial diff(String input, polynomial root){
	 	
		 polynomial temp = root;
		 polynomial_method method = new polynomial_method();
		 polynomial tra = root;
		 String[] count = input.split(" ");//按 空格 分开
		 int len = count.length;
		 boolean havevar = false;
		 boolean havev = false;
		 if(len == 2)
		 {
			 if(count[0].equals("!d/d"))
			 {
				 String d_var = count[1] ;//变量	

				 while(temp.next.flag != 2)//表达式没结束
				 {			 
					 havevar = false;
					 temp=temp.next;

					 while(temp.flag != 2)
					 {
						 if(temp.flag == 1)
							 if(temp.op == '+' || temp.op == '-')
								 break;
						if(temp.flag == 0)
						{
							if(temp.var != "")//havevar
							 { 
								 if(temp.var.equals(d_var))
								 { 

									 havevar = true;
									 havev = true;
									 if(temp.exponent == 1)
									 { 
										 temp.var ="";
										 
									 }
									 else
									 {
										 temp.coef = temp.coef *temp.exponent;
										 temp.exponent = temp.exponent -1;
									 }
								 }
							 }
						}
						temp=temp.next;
						System.out.println("222222222");
					 }
					 if(havevar == true){
						 tra = temp;
						 if(temp.flag == 2){
							 tra.flag = 2;
							 break;
						 }
					 }
					 else
					 {
						 if(temp.flag == 2)
						 {
							 tra.flag =2;
							 if(havev == false)
								 tra.next.flag = 2;
							 break;
						 }
						 else
						 {
							 tra.op = temp.op;
							 tra.next = temp.next;
						 }
					 }

				 }		 	 
					
				 if(havev == false)
					 System.out.println("Error!!!!, no variable!"); 		 
			 }
			 else
				 System.out.println("Error input!"); 
		 }
		 else
			 System.out.println("Error input!");

		 return method.merge(root);
		 
	}
}


	
