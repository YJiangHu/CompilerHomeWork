package test3;

import java.util.*;

/**
 * @author Huyuanjiang
 * @function LR������
 * @date 2018-6-1
 */
public class Demo {
	
	private static int[][] Yy_action; //��һ����ά����������������
	private static int[][] Yy_goto; //��һ����ά������״̬ת�Ʊ�
	private static int[] Yy_lhs; //��һ��һά������ÿ������ʽ�󲿷�������ֵ
	private static int[] Yy_reduce; //��һ��һά������ÿ������ʽ�Ҳ����Ÿ���
	private static Stack<Integer> statusStack = new Stack<>(); //״̬ջ
	private static Stack<Integer> symbolStack = new Stack<>(); //����ջ
	private static Map<String, Integer> map = new HashMap<>(); //������������ӳ���
	//Yyan������Yy_action�����Ԫ��
	private int Yya000[]={2,4,2,1,1};
	private int Yya001[]={4,5,-6,3,-6,2,-6,0,-6};
	private int Yya003[]={2,0,0,2,7};
	private int Yya004[]={4,5,-2,2,-2,0,-2,3,8};
	private int Yya005[]={4,5,-4,3,-4,2,-4,0,-4};
	private int Yya006[]={2,5,9,2,7};
	private int Yya009[]={4,5,-5,3,-5,2,-5,0,-5};
	private int Yya010[]={4,5,-1,2,-1,0,-1,3,8};
	private int Yya011[]={4,5,-3,3,-3,2,-3,0,-3};
	//Yygn��Yy_goto�����Ԫ��
	private int Yyg000[]={3,3,5,2,4,1,3};
	private int Yyg002[]={3,3,5,2,4,1,6};
	private int Yyg007[]={2,3,5,2,10};
	private int Yyg008[]={1,3,11};
	//state�ǵ�ǰ�������̲���
	private static int state = 1;
	
	public Demo() {
		//Yy_action��ֵ
		Yy_action = new int[][]{Yya000, Yya001, Yya000, Yya003, Yya004, Yya005,
		      Yya006, Yya000, Yya000, Yya009, Yya010, Yya011};
		//Yy_goto��ֵ		
		Yy_goto = new int[][] {Yyg000, null, Yyg002, null, null, null,
			null, Yyg007, Yyg008, null, null, null};
		//Yy_lhs��ֵ	
		Yy_lhs = new int[] {0, 1, 1, 2, 2, 3, 3};
		//Yy_reduce��ֵ
		Yy_reduce = new int[] {1, 3, 1, 3, 1, 3, 1};
		//����ս��������ֵ��ӳ��
		map.put("#", 0);
		map.put("ID", 1);
		map.put("+", 2);
		map.put("*", 3);
		map.put("(", 4);
		map.put(")", 5);
		//��ŷ��ս����������ӳ�䣨���ｫ���ս���ĳ��������10���Ƿ�ֹͨ������ȡ���Ų�����ͻ��
		map.put("S", 10);
		map.put("E", 11);
		map.put("T", 12);
		map.put("F", 13);
	}
	/**
	 * �ж�ָ���ַ��Ƿ�Ϊ�ս��
	 * @param ch ��Ҫ�жϵ��ַ�
	 */
	public boolean isVT(char ch) {
		//������ַ���A~Z֮����ַ��������ս�������Ƿ��ս��
		if(ch >= 'A' && ch <= 'Z') {
			return false;
		}
		return true;
	}
	/**
	 * ����ս������ս����Ӧ������ֵ
	 * @param ch ��Ҫ������ַ�
	 * @return �ս������ս����Ӧ������ֵ
	 */
	public int getNum(char ch) {
		//������ַ������֣��򷵻�"ID"��Ӧ������ֵ
		if(ch >= '0' && ch <= '9') {
			return map.get("ID");
		}
		//���map�к��д��ַ���ֱ�ӷ��ش��ַ���Ӧ������ֵ
		if(map.containsKey(String.valueOf(ch)))
			return map.get(String.valueOf(ch));
		//���򷵻�-1���������
		else
			return -1;
	}
	/**
	 * ͨ�����ų�������map���Ҷ�Ӧ�ķ���
	 * @param num ���ų���
	 * @return ���ų�����map�ж�Ӧ�ķ���
	 */
	public String getSymbol(int num) {
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String key = it.next();
			if(map.get(key) == num) {
				return key;
			}
		}
		return "";
	}
	/**
	 * ����״̬��������ţ����Ӧ��ȡ�Ķ�����ת�����һ״̬
	 * @param table Ҫ��ı�
	 * @param cur_state �кţ�״̬��
	 * @param symbol �кţ�ָ�����ţ�
	 * @return Ӧ��ȡ�Ķ�����ת�����һ״̬
	 */
	public int Yy_next(int[][] table, int cur_state, int symbol) {
		//������ָ���в�Ϊ��
		if(table[cur_state] != null) {
			//ָ���еĵ�һ��Ԫ�أ���ż����������count
			int count = table[cur_state][0];
			//����ָ���е�������ż�ĵ�һ���������ų�����
			for(int i = 1; i <= 2*count; i += 2) {
				//������ƥ��ķ��ų������򷵻���ż�ĵڶ�������Ӧ��ȡ�Ķ�����ת�����һ״̬��
				if(symbol == table[cur_state][i]) {
					return table[cur_state][i+1];
				}
			}
		}
		//������ָ����Ϊ�գ���û���ҵ���Ӧ�Ķ�����״̬������255���������
		return 255;
	}
	/**
	 * ��ӡ��ǰ����������̣��������衢״̬ջ���ݡ�����ջ���ݡ��������Ŵ�����������
	 * @param arr ���봮�ַ�����
	 * @param index ����ע�ӵ��ַ�
	 * @param next action����
	 */
	public void print(char[] arr, int index, int next) {
		System.out.print(state++ + "\t"); //��ǰ����
		//������ӡ״̬ջ����
		for(Integer x : statusStack) {
			System.out.print(x);
		}
		System.out.print("\t");
		//������ӡ����ջ����
		for(Integer y : symbolStack) {
			System.out.print(getSymbol(y));
		}
		System.out.print("\t");
		//��ӡ�������Ŵ�
		for(int i = index; i < arr.length; i++) {
			System.out.print(arr[i]);
		}
		System.out.print("\t");
		//��ӡ��������
		if(next == 255 ) {
			System.out.print("����");
			System.out.print("\t");
			System.out.println("");
		} else if(next == 0) {
			System.out.print("acc");
			System.out.print("\t");
			System.out.println("");
		} else if(next > 0) {
			System.out.print("s" + next);
			System.out.print("\t");
			System.out.println(next);
		} else if(next < 0) {
			System.out.print("r" + (0 - next));
			System.out.print("\t");
			System.out.print("GOTO[" + statusStack.peek() 
					+ "," + getSymbol(symbolStack.peek()) + "]=");
		}
	}
	/**
	 * ��ӡ��Լ����״̬ת�Ƶ���״̬
	 * @param gotoStatus goto����ת�Ƶ���״̬
	 */
	public void prints(int gotoStatus) {
		if(gotoStatus != 0) {
			System.out.println(gotoStatus);
		}
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Demo demo = new Demo();
		String input = in.next(); //������Ŵ�
		char[] arr = input.toCharArray(); //������Ŵ�ת��Ϊ�ַ�����
		int index = 0; //�ַ������±�
		boolean error = false; //�����־λ����Ϊtrue��������������ֱ�ӱ���
		/** ������ʼ  **/
		statusStack.push(0); //״̬ջѹ��״̬0
		symbolStack.push(0); //����ջѹ�����0����#����
		System.out.println("����\t״̬ջ\t����ջ   �������Ŵ� ��������\t��һ״̬");
		//���ҪѰ�ҵ�״̬����action��������ж�
		while(demo.Yy_next(Yy_action, statusStack.peek(),
				demo.getNum(arr[index])) != 0) {		
			int gotoStatus = 0; //goto״̬ת�Ʒ���
			int cur_state = statusStack.peek(); //ҪѰ�Ҷ�����״̬�������±�
			int symbol = demo.getNum(arr[index]); //ҪѰ�Ҷ������ս��
			//��map��û���ҵ����ַ�
			if(symbol == -1) {
				error = true;
				break;
			}
			int next = demo.Yy_next(Yy_action, cur_state, symbol); //�������Ķ���
			demo.print(arr, index, next);
			if(next == 255) {
				//�������������255�������������ñ����־λ������������
				error = true;
				break;
			} else if(next > 0) {		
				//��������������������ƽ�������״̬�ƽ�״̬ջ���ַ��ƽ�����ջ��ͬʱ�±��һ
				statusStack.push(next);
				symbolStack.push(symbol);
				index++;
			} else if(next < 0) {
				//������������Ǹ�������Լ��������ȡ�ò���ʽ��ţ���ȡ����Ӧ����ʽ�Ҳ����Ⱥ��󲿷���
				int rn = 0 - next;
				int rightLength = Yy_reduce[rn];
				int leftSymbol = Yy_lhs[rn] + 10;
				//��״̬ջ�ͷ���ջ��������ʽ�Ҳ����ȸ���Ԫ��
				for(int i = 0; i < rightLength; i++) {
					statusStack.pop();
					symbolStack.pop();
				}
				symbolStack.push(leftSymbol); //������ʽ�󲿷���ѹ�����ջ
				//��ȡ��״̬ջջ��״̬�ͷ���ջջ��Ԫ�ض�Ӧ��GOTO����
				gotoStatus = demo.Yy_next(Yy_goto, statusStack.peek(),
						symbolStack.peek() - 10);
				statusStack.push(gotoStatus); //������õ�GOTO״̬ѹ��״̬ջ
			}
			demo.prints(gotoStatus); //��ӡ��һ״̬
		}
		if(error) {
			System.err.println("��");			
		} else {
			demo.print(arr, index, 0);
			System.err.println("��");				
		}
		in.close();
	}
}
