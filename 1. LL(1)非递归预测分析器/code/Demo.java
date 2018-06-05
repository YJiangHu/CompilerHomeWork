package test2;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @author Huyuanjiang
 * @function 非递归预测分析器
 * @date 2018/5/1
 */
public class Demo {
	
	//map用于存放文法符号。key为常数值，value为对应的文法符号
	private Map<Integer, String> map = new TreeMap<Integer, String>();	
	//Yy_pushtab[][]用于存放文法产生式。每列逆序存放该产生式右部各符号的常数值。
	private int[][] Yy_pushtab = {{257, 1, 258, 0},
		{256, 0}, {0}, {260, 259, 0}, {0}, {260, 259, 2, 0},
		{0}, {262, 261, 0}, {262, 261, 2, 0}, {0}, {5, 258, 4, 0}, {6, 0}, {256, 0}};	
	//Yy_d[]用于存放文法LL(1)分析表
	private int[][] Yy_d = {{-1, 0, -1, -1, 0, -1, 0},
			{2, 1, -1, -1, 1, -1, 1}, {-1, 4, -1, -1, 3, 4, 3},
			{-1, -1, -1, -1, 7, -1, 7}, {-1, 6, 5, -1, -1, 6, -1},
			{-1, -1, -1, -1, 10, -1, 11}, {-1, 9, 9, 8, -1, 9, -1},
			{-1, 12, -1, -1, 12, -1, 12}		
	};	
	//stack作为分析栈
	private Stack<Integer> stack = new Stack<Integer>();
	
	public Demo() {
		//在构造对象时初始化map的值
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
	 * 分析失败
	 */
	public void error() {
		System.out.println("");
		System.err.println("否");
	}
	
	/**
	 * 分析成功
	 */
	public void success() {
		System.err.println("是");
	}
	
	/**
	 * 将指定输入字符进行处理，获取其代表的文法符号的常数值
	 */
	public int deal(char ch) {
		String val; //val代表此字符对应的文法符号
		//如果此字符是数字，则val=Num（属于Num）
		if(ch >= '0' && ch <= '9') {
			val = "Num";
		} else {
			//否则val=字符本身
			val = String.valueOf(ch);
		}
		//以val(文法符号）为value， 去map中找对应的key(常数)
		return getKey(val);
	}
	
	/**
	 * 通过指定value，从map中获取对应的key
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
	 * 判断指定字符是否为该文法中文法符号
	 */
	public boolean isMember(char ch) {
		//若指定字符不在文法符号集中，返回false
		if(deal(ch) == -1) {
			return false;
		}
		return true;
	}
	/**
	 * 获取Yy_d的行标号，由于非终结符为行标，且从256开始，则减去256就为数组下标
	 */
	public int column(int col) {
		return col - 256;
	}
	
	/**
	 * 格式化打印字符串，其中的数值是每一列的最大字符数，数值-length就是此行为对齐还需打印的空格数
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
	 * 分析器总控程序
	 */
	public boolean analyze(char[] in) {
		int index = 0; //in数组下标
		stack.push(263);
		System.out.printf("%-28s%-18s%-20s%s", "栈(符号)", "栈(常数)", "输入串", "what_to_do");
		System.out.println("");
		while(!stack.isEmpty()) {
			int what_to_do = -2; //what_to_do，默认为-2（为-2代表栈顶为非终结符，不需要what_to_do）
			//打印分析栈中的文法符号
			print(in, index);
			/******分析程序*******/
			//若正在注视的字符不是此文法中的文法符号，则分析失败
			if(!isMember(in[index])) {
				error();
				return false;
			}
			if(stack.peek() > 0 && stack.peek() <= 6) {
				if(stack.peek() != deal(in[index])) {
					//语法错误，分析失败，直接退出
					error();
					return false;
				} else {
					stack.pop();
					index++;
				}
			} else {
				what_to_do = Yy_d[column(stack.peek())][deal(in[index])];
				if(what_to_do == -1) {
					//语法错误，分析失败，直接退出
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
			//打印what_to_do的值
			if(what_to_do != -2) {
				//如果本次分析过程有what_to_do，即栈顶为非终结符，就打印what_to_do的值
				System.out.println(what_to_do);
			} else {
				//如果本次分析没有what_to_do，即栈顶为终结符，就打印空白
				System.out.println(" ");
			}
		}
		//分析成功
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
