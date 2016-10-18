package lab11;
import lab11.polynomial;

public class polynomial_method {
	
	public void copy(polynomial root_old, polynomial root_new){
		System.out.println("fa");
		float c;
		int e;
		char op;
		String var;
		if(root_old.flag == 0){
			c = root_old.coef;
			e = root_old.exponent;
			var = root_old.var;
			root_new.next = new polynomial(c, var, e);
		}
		else if(root_old.flag == 1){
			op = root_old.op;
			root_new.next = new polynomial(op);
		}
		else
			root_new.next = new polynomial();
	}
	
	public boolean like_var(polynomial root1, polynomial root2){
		String var = root1.next.var;
		while(root2.next.flag != 2){	//root2没到最后一项
			root2 = root2.next;

			if(root2.flag == 1)
				if(root2.op != '*')		//当前符号为+，-符号，匹配结束
					break;
				else					//当前符号为'*'， 直接跳过
					continue;

			if(root2.var.equals(var)){	//有相同变量
				if(!var.equals(""))
					root2.exponent += root1.next.exponent;
				root2.coef *= root1.next.coef;
				return true;
			}		
		}
		return false;
	}
	
	public boolean like_term(polynomial root1, polynomial root2){
		int length1 = 0;
		int length2 = 0;
		polynomial tmp1 = root1;
		polynomial tmp2 = root2;
		while(tmp1.next.flag != 2){
			tmp1 = tmp1.next;
			if(tmp1.flag == 1 && tmp1.op != '*')	//为+，-符号时结束，只对单项式比较
				break;
			length1++;
		}
		while(tmp2.next.flag != 2){
			tmp2 = tmp2.next;
			if(tmp2.flag == 1 && tmp2.op != '*')	//为+，-符号时结束，只对单项式比较
				break;
			length2++;
		}
		if(length1 != length2)
			return false;
		else{
			tmp1 = root1;
			String var1, var2;
			int exponent1, exponent2;
			boolean flag = false;
			while(tmp1.next.flag != 2){
				tmp1 = tmp1.next;
				if(tmp1.flag == 1){
					if(tmp1.op == '*')
						continue;
					else				//+，-符号，表明单项式结束
						break;
				}
					
				var1 = tmp1.var;
				exponent1 = tmp1.exponent;
				
				tmp2 = root2;
				while(tmp2.next.flag != 2){
					tmp2 = tmp2.next;
					if(tmp2.flag == 1){
						if(tmp2.op == '*')
							continue;
						else				//+，-符号，表明单项式结束
							break;
					}
					var2 = tmp2.var;
					exponent2 = tmp2.exponent;
					if(var1.equals(var2) && exponent1 == exponent2){	//该变量找到同类项，进行下个变量查找
						flag = true;
						break;
					}
				}
				if(flag == false)	//该变量未匹配成功
					return false;
				else
					flag = false;	//重置flag标志，进行下个变量查找
			}
			return true;	//执行到这步说明是同类项
		}	
	}
	
 	public polynomial merge_var(polynomial root_old){
		polynomial root_new = new polynomial();
		polynomial_method method = new polynomial_method();
		polynomial root_tmp = root_new;
		polynomial copy_tmp = root_tmp;
		float coef;

		//第一项的第一个变量 直接复制
		root_old = root_old.next;
		while(root_old.flag == 1){
			if(root_old.op == '+')
				root_old = root_old.next;
			else{
				if(root_old.next.flag == 0)
					if(root_old.next.coef < 0)
						root_old = root_old.next;
					else{
						method.copy(root_old, copy_tmp);
						copy_tmp = copy_tmp.next;
						root_old = root_old.next;
						break;
					}
				else
					root_old = root_old.next;
			}
		}
		
						
		method.copy(root_old, copy_tmp);
		
		copy_tmp = copy_tmp.next;
		coef = copy_tmp.coef;	
		copy_tmp.next = new polynomial();	//root_new 最后一项的终止符
		if(root_old.flag == 1)
			root_old = root_old.next;
			
		while(root_old.next.flag != 2){		//root_old还没到最后一项，循环继续
			
			//遍历到*符号不处理，对下个变量进行对比
			if(root_old.flag == 0){			//跳过刚复制的变量
				root_old = root_old.next;
				continue;
			}
			else if(root_old.flag == 1){	//遍历遇到+，-符号，直接复制，且连同符号后第一个变量一起复制
				
				if(root_old.op != '*'){
					method.copy(root_old, copy_tmp);	//复制+，-符号
					copy_tmp = copy_tmp.next;
					root_old = root_old.next;

					coef = 1;
					root_tmp = copy_tmp;
					method.copy(root_old, copy_tmp);	//复制+，-符号后第一个变量
					copy_tmp = copy_tmp.next;
					coef *= copy_tmp.coef;
					copy_tmp.next = new polynomial();

					continue;
				}
				else{
					if(!method.like_var(root_old, root_tmp)){	//查看是否有同变量：若有，指数相加(函数内完成)；若无，直接连接
						method.copy(root_old, copy_tmp);	//连接*号
						copy_tmp = copy_tmp.next;
						root_old = root_old.next;
						method.copy(root_old, copy_tmp);	//连接变量
						copy_tmp = copy_tmp.next;
						coef = copy_tmp.coef;

						root_tmp.next.coef *= coef;
						copy_tmp.coef = 1;
						copy_tmp.next = new polynomial();
					}
					if(root_old.next.flag != 2)
						root_old = root_old.next;	
				}
			}
		}


		return root_new;
	}	
 	
 	public polynomial merge_term(polynomial root_old){
		polynomial root_new = new polynomial();
		polynomial_method method = new polynomial_method();
		polynomial root_tmp = root_new;
		boolean flag = false;				//false-无同类项; true-有同类项

		//第一项 直接复制
		while(root_old.next.flag != 2){		//未到结尾，继续复制
			root_old = root_old.next;

			method.copy(root_old, root_tmp);

			root_tmp = root_tmp.next;
			
			if(root_old.next.flag == 1)			//遇到+，-符号，复制中断
				if(root_old.next.op != '*')
					break;
		}
		root_tmp.next = new polynomial();	//root_new 最后一项的终止符

		
		while(root_old.next.flag != 2){		//root_old还没到最后一项，循环继续

			root_tmp = root_new;			//每次从root_new第一个多项式开始比较

			//遍历到+，-符号或root节点（不处理），对下个单项式进行对比
			if(root_old.flag == 0){	
				root_old = root_old.next;
				continue;
			}
			else if(root_old.flag == 1){
				if(root_old.op == '*'){
					root_old = root_old.next;
					continue;
				}
			}

			
			flag = false;
			//将root_old的第一项与root_new的每一项比对
			while(root_tmp.next.flag != 2){	//root_new还没到最后一项，循环继续
				//遍历到+，-符号或root节点（不处理），对下个单项式进行对比
				if(root_tmp.flag == 0){	
					root_tmp = root_tmp.next;
					continue;
				}
				else if(root_tmp.op == '*'){
					root_tmp = root_tmp.next;
					continue;
				}

				if(method.like_term(root_old, root_tmp)){	//有同类项，系数相加

					if(root_old.flag == 2)	//root节点
						root_tmp.next.coef += root_old.next.coef;
					else if(root_old.flag == 1 && root_old.op == '+' && (root_tmp.op == '+' || root_tmp.flag == 2) || (root_old.op == '-' && root_tmp.op == '-') )
						root_tmp.next.coef += root_old.next.coef;
					else if(root_old.flag == 1 && root_old.op == '-' && (root_tmp.op == '+' || root_tmp.flag == 2) || (root_old.op == '+' && root_tmp.op == '-') )
						root_tmp.next.coef -= root_old.next.coef;					
					else
						System.out.println("error occurred!");
					if(root_tmp.next.coef < 0){
						if(root_tmp.flag != 2){
							root_tmp.next.coef = -root_tmp.next.coef;
							root_tmp.op = root_tmp.op == '+'  ? '-' : '+';
						}
					}
					
					flag = true;
					root_old = root_old.next;	//跳过当前root或+，-节点
					break;
				}
				root_tmp = root_tmp.next;	//跳过当前root或+，-节点
			}
			
			//无同类项，直接连接单项式(root_tmp位于root_new末端)
			if(flag == false){

				// 处理好root/+，-符号 下面开始连接单项式（单项式复制结束标志：多项式结尾/遇到+，-符号）

				while(root_old.next.flag != 2){		//未到结尾，继续复制

					method.copy(root_old, root_tmp);
					
					root_tmp = root_tmp.next;

					if(root_old.next.flag == 1)			//遇到+，-符号，复制中断
						if(root_old.next.op != '*')
							break;
					root_old = root_old.next;
										
				}
				if(root_old.next.flag == 2){
					method.copy(root_old, root_tmp);	//格式化复制最后一项
					root_tmp = root_tmp.next;
				}
				root_tmp.next = new polynomial();	//root_new 最后一项的终止符
			}
		}
		return root_new;
	}
 	
	 public boolean Isletter(char a)
	 {
		 if ((a >= 'A' && a <= 'Z') || (a >= 'a' && a <= 'z')) 
			 return true;
		 else 
			 return false;
	 }

	 public String GetVarStr(String input, int i)
	 {
		 int j = i + 1;
		 for (;j < input.length() && Isletter(input.charAt(j)); j++);
		 return input.substring(i,j);
	 }
	 
	 public polynomial merge(polynomial root_old){
		 polynomial root_new;

		 root_new = merge_var(root_old);

		 root_new = merge_term(root_new);

		 root_new = clear(root_new);

		 return root_new;
	 }
	 
     public polynomial clear(polynomial root_old){
 		polynomial root_new = new polynomial();
 		polynomial root_tmp = root_new;
 		polynomial_method method = new polynomial_method();
 		while(root_old.next.flag != 2){	//没到最后一项
 			if(root_old.next.coef == 0){
 				root_old = root_old.next;
 				while(root_old.flag == 0 || root_old.flag == 1){
 					if(root_old.flag == 1)
 						if(root_old.op != '*')				
 							break;
 					root_old = root_old.next;
 				}
 				if(root_old.flag == 2)
 					break;
 				continue;
 			}
 			else{
 				if(root_tmp == root_new){		//新的第一项，如为正符号位或根节点，跳过
 					if(root_old.flag == 1)		//'-'
 						if(root_old.op == '-'){
 		 					method.copy(root_old, root_tmp);	//复制符号位
 		 					root_tmp = root_tmp.next;
 		 					root_tmp.next = new polynomial();
 						}
 					root_old = root_old.next;
 				}
 				
 				else{
 					method.copy(root_old, root_tmp);	//复制符号位
 					root_tmp = root_tmp.next;
 					root_tmp.next = new polynomial();
 					root_old = root_old.next;
 				}
 				while(root_old.flag == 0){
 					method.copy(root_old, root_tmp);
 					root_tmp = root_tmp.next;
 					root_tmp.next = new polynomial();
 					root_old = root_old.next;
 					if(root_old.flag == 2)
 						return root_new;
 				}
 			}
 		}
 		return root_new;
 	}
}

