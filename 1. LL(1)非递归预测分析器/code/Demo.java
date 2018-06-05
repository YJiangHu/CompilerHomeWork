package test2;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @author Huyuanjiang
 * @function �ǵݹ�Ԥ�������
 * @date 2018/5/1
 */
public class Demo {
	
	//map���ڴ���ķ����š�keyΪ����ֵ��valueΪ��Ӧ���ķ�����
	private Map<Integer, String> map = new TreeMap<Integer, String>();	
	//Yy_pushtab[][]���ڴ���ķ�����ʽ��ÿ�������Ÿò���ʽ�Ҳ������ŵĳ���ֵ��
	private int[][] Yy_pushtab = {{257, 1, 258, 0},
		{256, 0}, {0}, {260, 259, 0}, {0}, {260, 259, 2, 0},
		{0}, {262, 261, 0}, {262, 261, 2, 0}, {0}, {5, 258, 4, 0}, {6, 0}, {256, 0}};	
	//Yy_d[]���ڴ���ķ�LL(1)������
	private int[][] Yy_d = {{-1, 0, -1, -1, 0, -1, 0},
			{2, 1, -1, -1, 1, -1, 1}, {-1, 4, -1, -1, 3, 4, 3},
			{-1, -1, -1, -1, 7, -1, 7}, {-1, 6, 5, -1, -1, 6, -1},
			{-1, -1, -1, -1, 10, -1, 11}, {-1, 9, 9, 8, -1, 9, -1},
			{-1, 12, -1, -1, 12, -1, 12}		
	};	
	//stack��Ϊ����ջ
	private Stack<Integer> stack = new Stack<Integer>();
	
	public Demo() {
		//�ڹ������ʱ��ʼ��map��ֵ
		map.put(0, "#");
		map.put(1, ";");
		map.put(2, "+");
		map.put(3, "*");
		map.put(4, "(");
		map.put(5, ")");
		map.put(6, "Num");
		map.put(256, "prgm");
		map.put(257, "prgm'");
		map.put(258, "expr");
		map.put(259, "term");
		map.put(260, "expr'");
		map.put(261, "factor");
		map.put(262, "term'");
		map.put(263, "system_goal");
	}
	
	/**
	 * ����ʧ��
	 */
	public void error() {
		System.out.println("");
		System.err.println("��");
	}
	
	/**
	 * �����ɹ�
	 */
	public void success() {
		System.err.println("��");
	}
	
	/**
	 * ��ָ�������ַ����д�����ȡ�������ķ����ŵĳ���ֵ
	 */
	public int deal(char ch) {
		String val; //val������ַ���Ӧ���ķ�����
		//������ַ������֣���val=Num������Num��
		if(ch >= '0' && ch <= '9') {
			val = "Num";
		} else {
			//����val=�ַ�����
			val = String.valueOf(ch);
		}
		//��val(�ķ����ţ�Ϊvalue�� ȥmap���Ҷ�Ӧ��key(����)
		return getKey(val);
	}
	
	/**
	 * ͨ��ָ��value����map�л�ȡ��Ӧ��key
	 */
	public int getKey(String val) {
		Set<Integer> keySet = map.keySet();
		Iterator<Integer> it = keySet.iterator();
		while(it.hasNext()) {
			Integer key = it.next();
			String value = map.get(key);
			if(value.equals(val)) {
				return key;
			}
		}
		return -1;
	}
	
	/**
	 * �ж�ָ���ַ��Ƿ�Ϊ���ķ����ķ�����
	 */
	public boolean isMember(char ch) {
		//��ָ���ַ������ķ����ż��У�����false
		if(deal(ch) == -1) {
			return false;
		}
		return true;
	}
	/**
	 * ��ȡYy_d���б�ţ����ڷ��ս��Ϊ�б꣬�Ҵ�256��ʼ�����ȥ256��Ϊ�����±�
	 */
	public int column(int col) {
		return col - 256;
	}
	
	/**
	 * ��ʽ����ӡ�ַ��������е���ֵ��ÿһ�е�����ַ�������ֵ-length���Ǵ���Ϊ���뻹���ӡ�Ŀո���
	 */
	public void print(char[] in, int index) {
		int length = 0;
		for(Integer a: stack) {
			System.out.print(map.get(a) + " ");
			length += map.get(a).length() + 1;
		}
		for(int j = 0; j < 30 - length; j++) {
			System.out.print(" ");
		}
		length = 0;
		for(Integer b: stack) {
			System.out.print(b + " ");
			length += String.valueOf(b).length() + 1;
		}
		for(int j = 0; j < 20 - length; j++) {
			System.out.print(" ");
		}
		length = 0;
		for(int j = index; j < in.length; j++) {
			System.out.print(in[j] + " ");
			length += String.valueOf(in[j]).length() + 1;
		}
		for(int j = 0; j < 10 - length; j++) {
			System.out.print(" ");
		}
	}
	
	/**
	 * �������ܿس���
	 */
	public boolean analyze(char[] in) {
		int index = 0; //in�����±�
		stack.push(263);
		System.out.printf("%-28s%-18s%-20s%s", "ջ(����)", "ջ(����)", "���봮", "what_to_do");
		System.out.println("");
		while(!stack.isEmpty()) {
			int what_to_do = -2; //what_to_do��Ĭ��Ϊ-2��Ϊ-2����ջ��Ϊ���ս��������Ҫwhat_to_do��
			//��ӡ����ջ�е��ķ�����
			print(in, index);
			/******��������*******/
			//������ע�ӵ��ַ����Ǵ��ķ��е��ķ����ţ������ʧ��
			if(!isMember(in[index])) {
				error();
				return false;
			}
			if(stack.peek() > 0 && stack.peek() <= 6) {
				if(stack.peek() != deal(in[index])) {
					//�﷨���󣬷���ʧ�ܣ�ֱ���˳�
					error();
					return false;
				} else {
					stack.pop();
					index++;
				}
			} else {
				what_to_do = Yy_d[column(stack.peek())][deal(in[index])];
				if(what_to_do == -1) {
					//�﷨���󣬷���ʧ�ܣ�ֱ���˳�
					error();
					return false;
				} else {
					stack.pop();
					for(int i = 0; i < Yy_pushtab[what_to_do].length; i++) {
						if(Yy_pushtab[what_to_do][i] == 0) {
							break;
						}
						stack.push(Yy_pushtab[what_to_do][i]);
					}
				}
			}
			/*******************/
			System.out.print("    ");
			//��ӡwhat_to_do��ֵ
			if(what_to_do != -2) {
				//������η���������what_to_do����ջ��Ϊ���ս�����ʹ�ӡwhat_to_do��ֵ
				System.out.println(what_to_do);
			} else {
				//������η���û��what_to_do����ջ��Ϊ�ս�����ʹ�ӡ�հ�
				System.out.println(" ");
			}
		}
		//�����ɹ�
		success();
		return true;
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Demo demo = new Demo();
		String s = sc.next();
		char[] in = s.toCharArray();
		demo.analyze(in);
	}
}
