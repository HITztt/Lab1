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
		while(root2.next.flag != 2){	//root2û�����һ��
			root2 = root2.next;

			if(root2.flag == 1)
				if(root2.op != '*')		//��ǰ����Ϊ+��-���ţ�ƥ�����
					break;
				else					//��ǰ����Ϊ'*'�� ֱ������
					continue;

			if(root2.var.equals(var)){	//����ͬ����
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
			if(tmp1.flag == 1 && tmp1.op != '*')	//Ϊ+��-����ʱ������ֻ�Ե���ʽ�Ƚ�
				break;
			length1++;
		}
		while(tmp2.next.flag != 2){
			tmp2 = tmp2.next;
			if(tmp2.flag == 1 && tmp2.op != '*')	//Ϊ+��-����ʱ������ֻ�Ե���ʽ�Ƚ�
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
					else				//+��-���ţ���������ʽ����
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
						else				//+��-���ţ���������ʽ����
							break;
					}
					var2 = tmp2.var;
					exponent2 = tmp2.exponent;
					if(var1.equals(var2) && exponent1 == exponent2){	//�ñ����ҵ�ͬ��������¸���������
						flag = true;
						break;
					}
				}
				if(flag == false)	//�ñ���δƥ��ɹ�
					return false;
				else
					flag = false;	//����flag��־�������¸���������
			}
			return true;	//ִ�е��ⲽ˵����ͬ����
		}	
	}
	
 	public polynomial merge_var(polynomial root_old){
		polynomial root_new = new polynomial();
		polynomial_method method = new polynomial_method();
		polynomial root_tmp = root_new;
		polynomial copy_tmp = root_tmp;
		float coef;

		//��һ��ĵ�һ������ ֱ�Ӹ���
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
		copy_tmp.next = new polynomial();	//root_new ���һ�����ֹ��
		if(root_old.flag == 1)
			root_old = root_old.next;
			
		while(root_old.next.flag != 2){		//root_old��û�����һ�ѭ������
			
			//������*���Ų��������¸��������жԱ�
			if(root_old.flag == 0){			//�����ո��Ƶı���
				root_old = root_old.next;
				continue;
			}
			else if(root_old.flag == 1){	//��������+��-���ţ�ֱ�Ӹ��ƣ�����ͬ���ź��һ������һ����
				
				if(root_old.op != '*'){
					method.copy(root_old, copy_tmp);	//����+��-����
					copy_tmp = copy_tmp.next;
					root_old = root_old.next;

					coef = 1;
					root_tmp = copy_tmp;
					method.copy(root_old, copy_tmp);	//����+��-���ź��һ������
					copy_tmp = copy_tmp.next;
					coef *= copy_tmp.coef;
					copy_tmp.next = new polynomial();

					continue;
				}
				else{
					if(!method.like_var(root_old, root_tmp)){	//�鿴�Ƿ���ͬ���������У�ָ�����(���������)�����ޣ�ֱ������
						method.copy(root_old, copy_tmp);	//����*��
						copy_tmp = copy_tmp.next;
						root_old = root_old.next;
						method.copy(root_old, copy_tmp);	//���ӱ���
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
		boolean flag = false;				//false-��ͬ����; true-��ͬ����

		//��һ�� ֱ�Ӹ���
		while(root_old.next.flag != 2){		//δ����β����������
			root_old = root_old.next;

			method.copy(root_old, root_tmp);

			root_tmp = root_tmp.next;
			
			if(root_old.next.flag == 1)			//����+��-���ţ������ж�
				if(root_old.next.op != '*')
					break;
		}
		root_tmp.next = new polynomial();	//root_new ���һ�����ֹ��

		
		while(root_old.next.flag != 2){		//root_old��û�����һ�ѭ������

			root_tmp = root_new;			//ÿ�δ�root_new��һ������ʽ��ʼ�Ƚ�

			//������+��-���Ż�root�ڵ㣨�����������¸�����ʽ���жԱ�
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
			//��root_old�ĵ�һ����root_new��ÿһ��ȶ�
			while(root_tmp.next.flag != 2){	//root_new��û�����һ�ѭ������
				//������+��-���Ż�root�ڵ㣨�����������¸�����ʽ���жԱ�
				if(root_tmp.flag == 0){	
					root_tmp = root_tmp.next;
					continue;
				}
				else if(root_tmp.op == '*'){
					root_tmp = root_tmp.next;
					continue;
				}

				if(method.like_term(root_old, root_tmp)){	//��ͬ���ϵ�����

					if(root_old.flag == 2)	//root�ڵ�
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
					root_old = root_old.next;	//������ǰroot��+��-�ڵ�
					break;
				}
				root_tmp = root_tmp.next;	//������ǰroot��+��-�ڵ�
			}
			
			//��ͬ���ֱ�����ӵ���ʽ(root_tmpλ��root_newĩ��)
			if(flag == false){

				// �����root/+��-���� ���濪ʼ���ӵ���ʽ������ʽ���ƽ�����־������ʽ��β/����+��-���ţ�

				while(root_old.next.flag != 2){		//δ����β����������

					method.copy(root_old, root_tmp);
					
					root_tmp = root_tmp.next;

					if(root_old.next.flag == 1)			//����+��-���ţ������ж�
						if(root_old.next.op != '*')
							break;
					root_old = root_old.next;
										
				}
				if(root_old.next.flag == 2){
					method.copy(root_old, root_tmp);	//��ʽ���������һ��
					root_tmp = root_tmp.next;
				}
				root_tmp.next = new polynomial();	//root_new ���һ�����ֹ��
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
 		while(root_old.next.flag != 2){	//û�����һ��
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
 				if(root_tmp == root_new){		//�µĵ�һ���Ϊ������λ����ڵ㣬����
 					if(root_old.flag == 1)		//'-'
 						if(root_old.op == '-'){
 		 					method.copy(root_old, root_tmp);	//���Ʒ���λ
 		 					root_tmp = root_tmp.next;
 		 					root_tmp.next = new polynomial();
 						}
 					root_old = root_old.next;
 				}
 				
 				else{
 					method.copy(root_old, root_tmp);	//���Ʒ���λ
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

