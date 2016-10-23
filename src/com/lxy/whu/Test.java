package com.lxy.whu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Random;

public class Test {
	public static final int BUFSIZE = 1024 * 8;
	static Student[] studentList = new Student[30];

	@SuppressWarnings("resource")
	// 合并名单文件和成绩文件得到merge文件
	public static void mergeFiles(String outFile, String[] files) {

		FileChannel outChannel = null;
		System.out.println("Merge " + Arrays.toString(files) + " into " + outFile);
		try {
			outChannel = new FileOutputStream(outFile).getChannel();
			// 对两个文件都建立buffer缓冲
			for (String f : files) {
				FileChannel fc = new FileInputStream(f).getChannel();

				ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
				while (fc.read(bb) != -1) {
					// flip方法刷新缓冲区
					bb.flip();
					// 将缓冲区的内容写入文件
					outChannel.write(bb);
					// 清空缓冲区
					bb.clear();
				}
				fc.close();
			}
			System.out.println("Merged!! ");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	// 读取merge文件并生成成绩、总分，生成student对象数组
	public static void ReadInMergeFile() {
		try {

			StringBuffer sb = new StringBuffer("");

			InputStreamReader reader = new InputStreamReader(new FileInputStream("merge.txt"), "UTF-8");
			BufferedReader br = new BufferedReader(reader);

			String str = null;
			int count = 0;
			Random random = new Random();
			// 按行获取文件里的string
			while ((str = br.readLine()) != null) {

				// 每获取一行，便将此行的内容以空格分开，成为一个保存学生信息的string数组，并创建一个student对象
				// 并将信息赋给此对象

				if (count > 0) {
					// 按照空格分开 并暂时存到studentInfoTemp数组里
					String[] studentInfoTemp = str.split("\\s+");
					// 存储每一个对象信息的数组
					String[] studentInfo = new String[6];
					// 将studentInfoTemp数组里的东西放到studentInfo数组的前两项里，剩余四项代码生成
					// 这里用excel表生成的txt文件
					// 开头第一个id竟然有个空格，我也不知道为什么，所以对于第一个id截取他的substring
					if (count == 1)
						studentInfo[0] = studentInfoTemp[0].substring(1, 2);
					else
						studentInfo[0] = studentInfoTemp[0];

					studentInfo[1] = studentInfoTemp[1];

					// 随机数生成符合正态分布的没各对象的语文、数学、英语成绩，这里要注意int类型转成string类型
					for (int i = 2; i <= 4; i++) {
						studentInfo[i] = (int) (Math.sqrt(64) * random.nextGaussian() + 75) + "";
					}

					// 求出每个student三科成绩的总分，作为studentInfo数组的最后一项
					int sum = Integer.valueOf(studentInfo[2]) + Integer.valueOf(studentInfo[3])
							+ Integer.valueOf(studentInfo[4]);
					studentInfo[5] = sum + "";

					Student student = new Student(studentInfo);
					// 将创建的student对象保存到数组里

					studentList[count - 1] = student;
//					if (studentList[count - 1] != null)
//						System.out.println(" not empty");
				}

				// count 用于标记是否是文件中的表头，如果是表头就不用创建student对象
				count++;
			}
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 将student对象写入文件final文件
	public static void WriteIntoFinalFile(Student[] al) {

		try {
			FileOutputStream ff = new FileOutputStream("final.txt");

			ff.write("ID\tName\tChi\tMath\tEng\tSum\r\n".getBytes());

			for (Student s : studentList) {
				ff.write(s.toString().getBytes());
			}
			// 调用average函数产生三科平均数，并写入final文件
			double[] avg = average(studentList);
			String out = "\t" + "平均数" + "\t" + avg[0] + "\t" + avg[1] + "\t" + avg[2];
			ff.write(out.getBytes());

		} catch (Exception ee) {
			System.out.println(ee);
		}
	}

	// 求三科平均数的函数
	public static double[] average(Student[] al) {
		int chiSum = 0;
		int mathSum = 0;
		int engSum = 0;

		double chiAvg = 0;
		double mathAvg = 0;
		double engAvg = 0;

		for (Student st : al) {
			chiSum += Integer.valueOf(st.getChinese());
			mathSum += Integer.valueOf(st.getMath());
			engSum += Integer.valueOf(st.getEng());
		}

		chiAvg = chiSum / al.length;
		mathAvg = mathSum / al.length;
		engAvg = engSum / al.length;

		double[] avg = { chiAvg, mathAvg, engAvg };
		return avg;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// 读取list文件 将list文件和score文件合并到merge
		mergeFiles("merge.txt", new String[] { "score.txt", "list.txt" });

		// 读取merge文件并生成成绩、总分，生成student对象数组
		ReadInMergeFile();

		// 计算每个对象的总分 并排序 这里就是按照sum 排序 要接口重写 compareTo方法
		Arrays.sort(studentList);

		// for (Student s : studentList) {
		// System.out.println(s);
		// }
		// 将student对象写入文件final文件
		WriteIntoFinalFile(studentList);

	}

}
