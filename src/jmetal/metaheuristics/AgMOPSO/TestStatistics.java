package jmetal.metaheuristics.AgMOPSO;

public class TestStatistics {
	private double[] inputData;
	public TestStatistics(double[] inputData){
		this.inputData = inputData;
	}

//		 public static void main(String[] args) {
//		              double [] testData=new double[]{1,2,3,4,5,6,7,8,9};
//		              System.out.println("���ֵ��"+getMax(testData));
//		              System.out.println("��Сֵ��"+getMin(testData));
//		              System.out.println("������"+getCount(testData));
//		              System.out.println("��ͣ�"+getSum(testData));
//		              System.out.println("��ƽ����"+getAverage(testData));
//		              System.out.println("���"+getVariance(testData));
//		              System.out.println("��׼�"+getStandardDiviation(testData));
//		              
//		 }

		 /**
		  * �����˫����������ֵ�����ֵ
		  * 
		  * @param inputData
		  *            ������������
		  * @return ������,�������ֵ���Ϸ�������Ϊ-1
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
		  * �������˫����������ֵ����Сֵ
		  * 
		  * @param inputData
		  *            ������������
		  * @return ������,�������ֵ���Ϸ�������Ϊ-1
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
		  * �����˫����������ֵ�ĺ�
		  * 
		  * @param inputData
		  *            ������������
		  * @return ������
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
		  * �����˫����������ֵ����Ŀ
		  * 
		  * @param input
		  *            Data ������������
		  * @return ������
		  */
		 public int getCount() {
		  if (inputData == null)
		   return -1;

		  return inputData.length;
		 }

		 /**
		  * �����˫����������ֵ��ƽ��ֵ
		  * 
		  * @param inputData
		  *            ������������
		  * @return ������
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
		  * �����˫����������ֵ��ƽ����
		  * 
		  * @param inputData
		  *            ������������
		  * @return ������
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
		  * �����˫����������ֵ�ķ���
		  * 
		  * @param inputData
		  *            ������������
		  * @return ������
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
		  * �����˫����������ֵ�ı�׼��
		  * 
		  * @param inputData
		  *            ������������
		  * @return ������
		  */
		 public double getStandardDiviation() {
		  double result;
		  //����ֵ������Ҫ
		  result = Math.sqrt(Math.abs(getVariance()));
		  
		  return result;

		 }
}
