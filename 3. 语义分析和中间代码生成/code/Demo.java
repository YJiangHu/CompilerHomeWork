package test6;

import java.util.*;

/**
 * @author Huyuanjiang
 * @date 2018-6-20
 * ����������м��������
 */
public class Demo {

	private static char[] symbol;//�����ַ�����
	private static int index = 0;//�����ַ�����ע�ӵ��α�	
	private static String[] current = new String[4];//��ʱ���飬���ڴ��op,arg0,arg1,result
	private static int currentIndex = 0;//��ʱ�����α�	
	private static Domain[] table = new Domain[100];//��Ԫʽ��
	private static int NXQ = 1;	
	private static int termFlag = 0;
	private static int factorFlag = 0;
	private static boolean loopFlag = true;//ѭ����־λ����Ϊtrue������ѭ����������ѭ��������һ����ת��ѭ������������ֱ�ӽ�����һ������		
	private static int tempIndex = 1;//��ʱ�����±꣨T1,T2.....��	
	private static Stack<Integer> stack = new Stack<>();//�����������ŵ�ջ
	/*
	 * ��̬�ڲ��࣬�൱��c�����нṹ�壬���ڴ�Ų������ʽTC,FC,Chain
	 */
	static class BoolExpr {
		int TC;
		int FC;
		int Chain;
	}
	/*
	 * ��̬�ڲ��࣬�൱��c�����нṹ�壬���ڴ����Ԫʽ����ÿ����Ԫʽ
	 */
	static class Domain {
		String op;
		String arg0;
		String arg1;
		String result;
		BoolExpr Ex;//�����Ե�Ϊ�������ʽ��ӵ��		
		public Domain(String op, String arg0,
				String arg1, String result) {
			this.op = op;
			this.arg0 = arg0;
			this.arg1 = arg1;
			this.result = result;
			this.Ex = new BoolExpr();
		}
		@Override
		public String toString() {
			return "(" + op + ", " + arg0 + ", " + arg1 + ", " + result + ")";
		}
		
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Demo demo = new Demo();
		String s;
		StringBuffer sb = new StringBuffer();
		while(!"#".equals(s = in.next())) {
			sb.append(s);
		}
		symbol = sb.toString().toCharArray();
		if(demo.procedure()) {
			System.out.println("��");
		} else {
			System.out.println("��");
		}
		for(int i = 1; i < NXQ; i++) {
			System.out.println(i + " " + table[i]);
		}
	}
	
	/*
	 * �ú����ǽ���Ԫʽ(op, arg1,arg2,result) �͵���Ԫʽ���С�
	 */
	public int gen(String op, String arg0, String arg1, String result) {
		int pre = NXQ;
		Domain current = new Domain(op, arg0, arg1, result);
		table[NXQ] = current;
		NXQ++;
		return pre;
	}	
	/*
	 * �ú�������һ���µ���ʱ����������ʱ������������˳��ΪT1��T2����
	 */
	public String newTemp() {
		return "T" + tempIndex++;
	}	
	/*
	 * �ú����Ĺ����ǰ�ָ��p�����ӵ�ÿ����Ԫʽ�ĵ������ζ���Ϊt��
	 */
	public int bp(int p, int t) {
		int q = p;
		while(q != 0) {
			int q1 = Integer.parseInt(table[q].result);
			table[q].result = String.valueOf(t);
			q = q1;			
		}
		return 1;
	}
	/**
	 * '����'���ս���ݹ��ӳ���
	 */
	public boolean procedure() {
		if(symbol[index] == 'm' && symbol[index + 1] == 'a'
				&& symbol[index + 2] == 'i' && symbol[index + 3] == 'n') {		
			index += 4;
			if(symbol[index] == '(') {
				index++;
				if(symbol[index] == ')') {
					index++;
					if(block()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/*
	 * '������'���ս���ݹ��ӳ���
	 */
	public boolean block() {
		if(symbol[index] == '{') {
			index++;
			while(string()) {
				if(symbol[index] == '}') {
					if(!stack.isEmpty()) {
						if(loopFlag) {
							bp(stack.pop(), NXQ+1);
						} else {
							bp(stack.pop(), NXQ);
						}
					}			
					index++;
					return true;
				} else {
					continue;
				}
			}
		}
		return false;
	}
	/*
	 * '��䴮'���ս���ݹ��ӳ���
	 */
	public boolean string() {
		if(statement()) {
			while(symbol[index] == ';') {
				index++;
				if(statement()) {
					continue;
				}
			}
			return true;
		}
		return false;
	}
	/*
	 * '���'���ս���ݹ��ӳ���
	 */
	public boolean statement() {
		if(assignment() || conditionStmt() || loop()) {
			return true;
		}
		return false;
	}
	/*
	 * '��ֵ���'���ս���ݹ��ӳ���
	 */
	public boolean assignment() {
		int pre = index;
		String left = "";
		String result = "";
		String op = "";
		if(symbol[index] >= 'a' && symbol[index] <= 'z'
				|| symbol[index] >= 'A' && symbol[index] <= 'Z') {
			result += String.valueOf(symbol[index]);
			index++;
			while(symbol[index] >= 'a' && symbol[index] <= 'z'
					|| symbol[index] >= 'A' && symbol[index] <= 'Z') {
				result += String.valueOf(symbol[index]);
				index++;
			}
			if(symbol[index] == '=') {
				op = String.valueOf(symbol[index]);
				index++;
				if(expression()) {
					if(termFlag == 1) {
						 left = table[NXQ - 1].result;
						 termFlag = 0;
					} else {
						left = String.valueOf(current[1]);
					}
					gen(op, left, "", result);
					return true;
				}
			}
		}
		index = pre;
		return false;
	}
	/*
	 * '�������'���ս���ݹ��ӳ���
	 */
	public boolean conditionStmt() {
		int pre = index;
		if(symbol[index] == 'i' && symbol[index+1] == 'f') {
			index += 2;
			if(symbol[index] == '(') {
				index++;
				if(condition()) {
					int con = NXQ - 1;
					table[NXQ-1].Ex.TC = NXQ - 1;
					table[NXQ-1].Ex.FC = NXQ;
					stack.push(NXQ);
					if(symbol[index] == ')') {
						gen("j", "", "", "0");
						index++;
						if(block()) {
							bp(con, NXQ - 1);
							return true;
						}
					}
				}
			}
		}
		index = pre;
		return false;
	}
	/*
	 * 'ѭ�����'���ս���ݹ��ӳ���
	 */
	public boolean loop() {
		int pre = index;
		if(symbol[index] == 'w' && symbol[index+1] =='h' 
				&& symbol[index+2] == 'i' && symbol[index+3] == 'l' 
					&& symbol[index+4] == 'e') {
			index += 5;
			if(condition()) {
				int con = NXQ - 1;
				table[NXQ-1].Ex.TC = NXQ - 1;
				table[NXQ-1].Ex.FC = NXQ;
				stack.push(NXQ);
				if(symbol[index] == ')') {
					gen("j", "", "", "0");
					index++;
					if(block()) {
						gen("j", "", "", String.valueOf(con));
						bp(con, con + 2);					
						return true;
					}
				}
			}
		}
		index = pre;
		return false;
	}
	/*
	 * '����'���ս���ݹ��ӳ���
	 */
	public boolean condition() {
		String cond = "";
		String arg1 = "";
		String arg2 = "";
		String label = "0";
		if(expression()) {
			if(factorFlag == 1) {
				arg1 = table[NXQ - 1].result;
			} else if(factorFlag == 2) {
				arg1 = table[NXQ - 2].result;
			} else {
				arg1 = current[1];
			}
			if(rop()) {
				cond = "j" + current[0];
				if(expression()) {
					if(factorFlag == 2) {
						arg2 = table[NXQ - 1].result;
					} else {
						arg2 = current[1];
					}
					factorFlag = 0;
					gen(cond, arg1, arg2, label);
					return true;
				}
			}
		}
		return false;
	}
	/*
	 * '���ʽ'���ս���ݹ��ӳ���
	 */
	public boolean expression() {
		String op = null;
		String arg1 = null;
		String arg2 = null;
		currentIndex = 1;
		if(term()) {	
			while(symbol[index] == '+' || symbol[index] == '-') {
				if(factorFlag == 1) {
					arg1 = table[NXQ - 1].result;
				} else if(factorFlag == 2) {
					arg1 = table[NXQ - 2].result;
				} else {
					arg1 = current[1];
				}
				termFlag = 1;
				op = String.valueOf(symbol[index]);
				index++;
				currentIndex = 1;
				if(!term()) {
					return false;
				}
				if(factorFlag == 2) {
					arg2 = table[NXQ - 1].result;
				} else {
					arg2 = current[1];
				}
				factorFlag = 0;
				String T = newTemp();
				gen(op, arg1, arg2, T);
			}
		}	
		currentIndex = 1;
		return true;
	}
	/*
	 * '��'���ս���ݹ��ӳ���
	 */
	public boolean term() {
	
		if(symbol[index + 1] == '*' || symbol[index + 1] == '/') {
			currentIndex = 1;
		}
		if(factor()) {
			while(symbol[index] == '*' || symbol[index] == '/') {
				factorFlag += 1; //����һ��term��������һ��*/���㣬��ȼ�һ
				current[0] = String.valueOf(symbol[index]);
				index++;
				if(!factor()) {
					return false;
				}
				String T = newTemp();
				current[currentIndex] = T;
				gen(current[0], current[1], current[2], current[3]);
			}
		}
		return true;
	}
	/*
	 * '����'���ս���ݹ��ӳ���
	 */
	public boolean factor() {
		String arg = "";
		if(symbol[index] >= 'a' && symbol[index] <= 'z'
				|| symbol[index] >= 'A' && symbol[index] <= 'Z') {
			arg += String.valueOf(symbol[index]);
			index++;
			while(symbol[index] >= 'a' && symbol[index] <= 'z'
					|| symbol[index] >= 'A' && symbol[index] <= 'Z') {
				arg += String.valueOf(symbol[index]);
				index++;
			}
			current[currentIndex++] = arg;
			return true;
		} else if(symbol[index] >= '0' && symbol[index] <= '9') {
			arg += String.valueOf(symbol[index]);
			index++;
			while(symbol[index] >= '0' && symbol[index] <= '9') {
				arg += String.valueOf(symbol[index]);
				index++;
			}
			current[currentIndex++] = arg;
			return true;
		} else if(symbol[index] == '(') {
			index++;
			if(expression()) {
				if(symbol[index] == ')') {
					return true;
				}
			}
		}
		return false;
	} 
	/*
	 * '��ϵ�����'���ս���ݹ��ӳ���
	 */
	public boolean rop() {
		String op = "";
		if(symbol[index] == '<' && symbol[index+1] == '=') {
			op = "<=";
			index += 2;
		} else if(symbol[index] == '<') {
			op = "<";
			index++;
		} else if(symbol[index] == '>' && symbol[index+1] == '=') {
			op = ">=";
			index += 2;
		} else if(symbol[index] == '>') {
			op = ">";
			index++;
		} else if(symbol[index] == '=' && symbol[index+1] == '=') {
			op = "==";
			index += 2;
		} else if(symbol[index] == '!' && symbol[index+1] == '=') {
			op = "!=";
			index += 2;
		} else {
			return false;
		}
		current[0] = op;
		return true;
	}	
}