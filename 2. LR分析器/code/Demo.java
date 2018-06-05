package test3;

import java.util.*;

/**
 * @author Huyuanjiang
 * @function LR分析器
 * @date 2018-6-1
 */
public class Demo {
	
	private static int[][] Yy_action; //用一个二维数组作分析动作表
	private static int[][] Yy_goto; //用一个二维数组作状态转移表
	private static int[] Yy_lhs; //用一个一维数组存放每个产生式左部符号整数值
	private static int[] Yy_reduce; //用一个一维数组存放每个产生式右部符号个数
	private static Stack<Integer> statusStack = new Stack<>(); //状态栈
	private static Stack<Integer> symbolStack = new Stack<>(); //符号栈
	private static Map<String, Integer> map = new HashMap<>(); //符号与整数的映射表
	//Yyan数组是Yy_action表的行元素
	private int Yya000[]={2,4,2,1,1};
	private int Yya001[]={4,5,-6,3,-6,2,-6,0,-6};
	private int Yya003[]={2,0,0,2,7};
	private int Yya004[]={4,5,-2,2,-2,0,-2,3,8};
	private int Yya005[]={4,5,-4,3,-4,2,-4,0,-4};
	private int Yya006[]={2,5,9,2,7};
	private int Yya009[]={4,5,-5,3,-5,2,-5,0,-5};
	private int Yya010[]={4,5,-1,2,-1,0,-1,3,8};
	private int Yya011[]={4,5,-3,3,-3,2,-3,0,-3};
	//Yygn是Yy_goto表的行元素
	private int Yyg000[]={3,3,5,2,4,1,3};
	private int Yyg002[]={3,3,5,2,4,1,6};
	private int Yyg007[]={2,3,5,2,10};
	private int Yyg008[]={1,3,11};
	//state是当前分析过程步骤
	private static int state = 1;
	
	public Demo() {
		//Yy_action表赋值
		Yy_action = new int[][]{Yya000, Yya001, Yya000, Yya003, Yya004, Yya005,
		      Yya006, Yya000, Yya000, Yya009, Yya010, Yya011};
		//Yy_goto表赋值		
		Yy_goto = new int[][] {Yyg000, null, Yyg002, null, null, null,
			null, Yyg007, Yyg008, null, null, null};
		//Yy_lhs表赋值	
		Yy_lhs = new int[] {0, 1, 1, 2, 2, 3, 3};
		//Yy_reduce表赋值
		Yy_reduce = new int[] {1, 3, 1, 3, 1, 3, 1};
		//存放终结符与整数值的映射
		map.put("#", 0);
		map.put("ID", 1);
		map.put("+", 2);
		map.put("*", 3);
		map.put("(", 4);
		map.put(")", 5);
		//存放非终结符与整数的映射（这里将非终结符的常数整体加10，是防止通过常数取符号产生冲突）
		map.put("S", 10);
		map.put("E", 11);
		map.put("T", 12);
		map.put("F", 13);
	}
	/**
	 * 判断指定字符是否为终结符
	 * @param ch 需要判断的字符
	 */
	public boolean isVT(char ch) {
		//如果此字符是A~Z之间的字符，则不是终结符，而是非终结符
		if(ch >= 'A' && ch <= 'Z') {
			return false;
		}
		return true;
	}
	/**
	 * 获得终结符或非终结符对应的整数值
	 * @param ch 需要处理的字符
	 * @return 终结符或非终结符对应的整数值
	 */
	public int getNum(char ch) {
		//如果此字符是数字，则返回"ID"对应的整数值
		if(ch >= '0' && ch <= '9') {
			return map.get("ID");
		}
		//如果map中含有此字符，直接返回此字符对应的整数值
		if(map.containsKey(String.valueOf(ch)))
			return map.get(String.valueOf(ch));
		//否则返回-1，代表出错
		else
			return -1;
	}
	/**
	 * 通过符号常数，在map中找对应的符号
	 * @param num 符号常数
	 * @return 符号常数在map中对应的符号
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
	 * 给定状态和输入符号，求出应采取的动作或转向的下一状态
	 * @param table 要查的表
	 * @param cur_state 行号（状态）
	 * @param symbol 列号（指定符号）
	 * @return 应采取的动作或转向的下一状态
	 */
	public int Yy_next(int[][] table, int cur_state, int symbol) {
		//若表中指定行不为空
		if(table[cur_state] != null) {
			//指定行的第一个元素（序偶个数）赋给count
			int count = table[cur_state][0];
			//遍历指定行的所有序偶的第一个数（符号常数）
			for(int i = 1; i <= 2*count; i += 2) {
				//若存在匹配的符号常数，则返回序偶的第二个数（应采取的动作或转向的下一状态）
				if(symbol == table[cur_state][i]) {
					return table[cur_state][i+1];
				}
			}
		}
		//若表中指定行为空，或没有找到相应的动作或状态，返回255，代表出错
		return 255;
	}
	/**
	 * 打印当前步骤分析过程，包括步骤、状态栈内容、符号栈内容、余留符号串、分析动作
	 * @param arr 输入串字符数组
	 * @param index 正在注视的字符
	 * @param next action动作
	 */
	public void print(char[] arr, int index, int next) {
		System.out.print(state++ + "\t"); //当前步骤
		//遍历打印状态栈内容
		for(Integer x : statusStack) {
			System.out.print(x);
		}
		System.out.print("\t");
		//遍历打印符号栈内容
		for(Integer y : symbolStack) {
			System.out.print(getSymbol(y));
		}
		System.out.print("\t");
		//打印余留符号串
		for(int i = index; i < arr.length; i++) {
			System.out.print(arr[i]);
		}
		System.out.print("\t");
		//打印分析动作
		if(next == 255 ) {
			System.out.print("出错");
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
	 * 打印归约动作状态转移的新状态
	 * @param gotoStatus goto动作转移的新状态
	 */
	public void prints(int gotoStatus) {
		if(gotoStatus != 0) {
			System.out.println(gotoStatus);
		}
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Demo demo = new Demo();
		String input = in.next(); //输入符号串
		char[] arr = input.toCharArray(); //输入符号串转换为字符数组
		int index = 0; //字符数组下标
		boolean error = false; //报错标志位，若为true，跳出分析程序，直接报错
		/** 分析开始  **/
		statusStack.push(0); //状态栈压入状态0
		symbolStack.push(0); //符号栈压入符号0（“#”）
		System.out.println("步骤\t状态栈\t符号栈   余留符号串 分析动作\t下一状态");
		//如果要寻找的状态存在action，则进行判断
		while(demo.Yy_next(Yy_action, statusStack.peek(),
				demo.getNum(arr[index])) != 0) {		
			int gotoStatus = 0; //goto状态转移符号
			int cur_state = statusStack.peek(); //要寻找动作的状态，即行下标
			int symbol = demo.getNum(arr[index]); //要寻找动作的终结符
			//在map中没有找到此字符
			if(symbol == -1) {
				error = true;
				break;
			}
			int next = demo.Yy_next(Yy_action, cur_state, symbol); //分析出的动作
			demo.print(arr, index, next);
			if(next == 255) {
				//如果动作常数是255（出错），则设置报错标志位，并结束分析
				error = true;
				break;
			} else if(next > 0) {		
				//如果动作常数是正数（移进），则将状态移进状态栈，字符移进符号栈，同时下标加一
				statusStack.push(next);
				symbolStack.push(symbol);
				index++;
			} else if(next < 0) {
				//如果动作常数是负数（归约），则先取得产生式序号，再取得相应产生式右部长度和左部符号
				int rn = 0 - next;
				int rightLength = Yy_reduce[rn];
				int leftSymbol = Yy_lhs[rn] + 10;
				//将状态栈和符号栈弹出产生式右部长度个数元素
				for(int i = 0; i < rightLength; i++) {
					statusStack.pop();
					symbolStack.pop();
				}
				symbolStack.push(leftSymbol); //将产生式左部符号压入符号栈
				//先取得状态栈栈顶状态和符号栈栈顶元素对应的GOTO动作
				gotoStatus = demo.Yy_next(Yy_goto, statusStack.peek(),
						symbolStack.peek() - 10);
				statusStack.push(gotoStatus); //将计算好的GOTO状态压入状态栈
			}
			demo.prints(gotoStatus); //打印下一状态
		}
		if(error) {
			System.err.println("否");			
		} else {
			demo.print(arr, index, 0);
			System.err.println("是");				
		}
		in.close();
	}
}
