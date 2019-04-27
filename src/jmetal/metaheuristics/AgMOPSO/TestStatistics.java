package jmetal.metaheuristics.AgMOPSO;

public class TestStatistics {
	private double[] inputData;
	public TestStatistics(double[] inputData){
		this.inputData = inputData;
	}

//		 public static void main(String[] args) {
//		              double [] testData=new double[]{1,2,3,4,5,6,7,8,9};
//		              System.out.println("最大值："+getMax(testData));
//		              System.out.println("最小值："+getMin(testData));
//		              System.out.println("计数："+getCount(testData));
//		              System.out.println("求和："+getSum(testData));
//		              System.out.println("求平均："+getAverage(testData));
//		              System.out.println("方差："+getVariance(testData));
//		              System.out.println("标准差："+getStandardDiviation(testData));
//		              
//		 }

		 /**
		  * 求给定双精度数组中值的最大值
		  * 
		  * @param inputData
		  *            输入数据数组
		  * @return 运算结果,如果输入值不合法，返回为-1
		  */
		 public double getMax() {
		  if (inputData == null || inputData.length == 0)
		   return -1;
		  int len = inputData.length;
		  double max = inputData[0];
		  for (int i = 0; i < len; i++) {
		   if (max < inputData[i])
		    max = inputData[i];
		  }
		  return max;
		 }

		 /**
		  * 求求给定双精度数组中值的最小值
		  * 
		  * @param inputData
		  *            输入数据数组
		  * @return 运算结果,如果输入值不合法，返回为-1
		  */
		 public double getMin() {
		  if (inputData == null || inputData.length == 0)
		   return -1;
		  int len = inputData.length;
		  double min = inputData[0];
		  for (int i = 0; i < len; i++) {
		   if (min > inputData[i])
		    min = inputData[i];
		  }
		  return min;
		 }

		 /**
		  * 求给定双精度数组中值的和
		  * 
		  * @param inputData
		  *            输入数据数组
		  * @return 运算结果
		  */
		 public double getSum() {
		  if (inputData == null || inputData.length == 0)
		   return -1;
		  int len = inputData.length;
		  double sum = 0;
		  for (int i = 0; i < len; i++) {
		   sum = sum + inputData[i];
		  }

		  return sum;

		 }

		 /**
		  * 求给定双精度数组中值的数目
		  * 
		  * @param input
		  *            Data 输入数据数组
		  * @return 运算结果
		  */
		 public int getCount() {
		  if (inputData == null)
		   return -1;

		  return inputData.length;
		 }

		 /**
		  * 求给定双精度数组中值的平均值
		  * 
		  * @param inputData
		  *            输入数据数组
		  * @return 运算结果
		  */
		 public double getAverage() {
		  if (inputData == null || inputData.length == 0)
		   return -1;
		  int len = inputData.length;
		  double result;
		  result = getSum() / len;
		  
		  return result;
		 }

		 /**
		  * 求给定双精度数组中值的平方和
		  * 
		  * @param inputData
		  *            输入数据数组
		  * @return 运算结果
		  */
		 public double getSquareSum() {
		  if(inputData==null||inputData.length==0)
		      return -1;
		     int len=inputData.length;
		  double sqrsum = 0.0;
		  for (int i = 0; i <len; i++) {
		   sqrsum = sqrsum + inputData[i] * inputData[i];
		  }

		  
		  return sqrsum;
		 }

		 /**
		  * 求给定双精度数组中值的方差
		  * 
		  * @param inputData
		  *            输入数据数组
		  * @return 运算结果
		  */
		 public double getVariance() {
		  int count = getCount();
		  double sqrsum = getSquareSum();
		  double average = getAverage();
		  double result;
		  result = (sqrsum - count * average * average) / count;

		     return result; 
		 }

		 /**
		  * 求给定双精度数组中值的标准差
		  * 
		  * @param inputData
		  *            输入数据数组
		  * @return 运算结果
		  */
		 public double getStandardDiviation() {
		  double result;
		  //绝对值化很重要
		  result = Math.sqrt(Math.abs(getVariance()));
		  
		  return result;

		 }
}
