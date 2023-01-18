package datacom;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class sv22r_project2 {
	
	public static void main(String[] args) throws FileNotFoundException {
		sv22r_project2 HMC= new sv22r_project2();
    Scanner file = new Scanner(new File("C:\\Users\\SHABARISH\\Downloads\\proj1_testsignal1 (1)"));
    Scanner file2 = new Scanner(new File("C:\\Users\\SHABARISH\\Downloads\\proj2_noisesignal1 (1)"));
    ArrayList<String> test_signal = new ArrayList<String>();
    while (file.hasNext()){
       test_signal.add(file.next());
    }
    ArrayList<String> noise_signal = new ArrayList<String>();
    while (file2.hasNext()){
       noise_signal.add(file2.next());
    }
    double[] test_signal_db = new double[test_signal.size()+1];
    int length=test_signal.size();
    for (int i = 0; i <length; i++) {
    	test_signal_db[i] = Double.parseDouble(test_signal.get(i));
    	   }
    double[] noise_signal_db = new double[noise_signal.size()+1];
    int length1=noise_signal.size();
    for (int i = 0; i <length; i++) {
    	noise_signal_db[i] = Double.parseDouble(noise_signal.get(i));
    	   }
    double[] combined_signal = new double[noise_signal.size()+1];
    
    for (int i = 0; i <length;i++) {
    	combined_signal[i]=test_signal_db[i]+noise_signal_db[i];
    	
    	   }
   
   
    Scanner sp= new Scanner(System.in);
    System.out.println("To learn the statistics, enter in the amount of noise samples");
    int nsp=sp.nextInt();
   
    double sum = 0;
    for (int i = 0; i <nsp; ++i) {
    	sum += combined_signal[i];
    	}
    	double mean=sum/nsp;
    	double standardDeviation = 0.0;
    	for (int i = 0; i <nsp ; i++) {
    	standardDeviation += Math.pow(combined_signal[i] - mean, 2);

    	}
    	int strt_sig=0;
    	double SD= Math.sqrt(standardDeviation/nsp);
    	System.out.println("Mean "+mean);

    	System.out.println("Standard Deviation "+SD);
    	double Threshold=(mean*8)+(SD*16);
    	System.out.println("Threshold "+Threshold);
    	for(int i=0;i<length;i++)
    	{
    	  if(combined_signal[i]>Threshold)
    	{
    	 strt_sig=i;
    	break;
    	}
    	 }
    	 System.out.println("strt_sig " +strt_sig);
    	 double[] subset_array = Arrays.copyOfRange(combined_signal, strt_sig, combined_signal.length);
    	 int count_hundred = 0;
    	 double current_value_high_threshold =0;
    	 int current_index_high_threshold = 0;
    	 ArrayList<Integer> indexValuesArray = new ArrayList<Integer>();
    	 for(int i=0; i <subset_array.length; i++) {
    		
    	  if(subset_array[i] > Threshold && subset_array[i] > current_value_high_threshold) {
    			current_value_high_threshold = combined_signal[i];
    			current_index_high_threshold = count_hundred;
    	       }
    	  count_hundred++;
    		if(count_hundred >=100) {
    			if(current_index_high_threshold==0)
    			{
    				break;
    			}
    		    indexValuesArray.add(current_index_high_threshold);
    			
    			count_hundred = 0;
    			current_value_high_threshold=0;
    			current_index_high_threshold=0;
    		}
    		
    	 }	
    	 ArrayList<Integer> binaryValues = new ArrayList<Integer>();
    	 for(int i=0; i <indexValuesArray.size(); i++) {
    	 if(indexValuesArray.get(i)<20 && indexValuesArray.get(i)!=0) {
    			binaryValues.add(0);
    		}
    	   else if(indexValuesArray.get(i)==0)
    	   {}
    	   else
    	   {
    			binaryValues.add(1);
    			}
    	}
    	 int Start_counter=8, Counter_Check=0;
    		ArrayList<Integer> Errored_Bits = new ArrayList<Integer>();
    		ArrayList<Integer> Corrected_Bits = new ArrayList<Integer>();
    		for (int i=	Start_counter; Start_counter<binaryValues.size();i++)

    		{
    			if(Counter_Check==6) 
    			{Errored_Bits.add(binaryValues.get(i));
    			Start_counter++;
    			HMC.Parity_Check_Matrix(Errored_Bits,Corrected_Bits);
    			Counter_Check=0;
    				Errored_Bits.clear();
    			}
    			else
    			{
    				Errored_Bits.add(binaryValues.get(i));
    				Counter_Check++;
    				Start_counter++;
    				
    			}
    		
    		
    		}	
    	int Skip_counter=0;
    	ArrayList<Integer> binaryValues2 = new ArrayList<Integer>();
    	for(int i=0; i<Corrected_Bits.size(); i++) {
    		try{
    			if(Skip_counter<4)
    	{
    		binaryValues2.add(Corrected_Bits.get(i));
    		Skip_counter++;
    	}
    	       else if(Skip_counter==4){
    		   i=i+2;
    		   Skip_counter=0;
    		
    	}
    	}
    		catch(IndexOutOfBoundsException e)
    		{
    		}

    	}
    	
    	char[] chars1 = new char[binaryValues2.size() / 8];
    	  for (int h = 0; h < chars1.length; ++h) {
    		    int counter2 = 0;
    		    for (int j = h * 8; j < (h + 1) * 8; ++j) {
    		    	counter2 = counter2 << 1;
    		    	counter2+= binaryValues2.get(j);
    		    }
    		    chars1[h] = (char)counter2;
    		}
    	    String s = new String(chars1);
    		System.out.println(s);
    	}
	
	int Parity_Check_Matrix(ArrayList<Integer> Errored_Bits,ArrayList<Integer> Corrected_Bits )
	{
		int Hamming_matrix[][] = {{1,0,1},{1,1,1},{1,1,0},{0,1,1},{1,0,0},{0,1,0},{0,0,1}};
		String[] parityStringMatrix = {"101", "111", "110", "011", "100", "010", "001"};
        int count=0;
		String Result="";
		int Syndrome_array[][]=new int[1][3];
		for(int i=0;i<1;i++)
		{
			for(int j=0; j<=2; j++)
			{
				Syndrome_array[i][j]=0;      
				for(int k=0;k<7;k++)      
				{  
					
					Syndrome_array[i][j]^=(int)Errored_Bits.get(k)* Hamming_matrix[k][j]; 
				
				}
				
				Result=Result+Syndrome_array[i][j];
			}
		}
		for(int l = 0; l < parityStringMatrix.length; l++) {
			if(Integer.parseInt(Result)==000)
			{
				
			}else
			{
        	if(Integer.parseInt(parityStringMatrix[l]) == Integer.parseInt(Result)) {
        		{
    				if(Errored_Bits.get(l)==0)
    			{
    					Errored_Bits.set(l,1);
    			}
    			else
    				{Errored_Bits.set(l,0);
    				}
    				}
        		
        		
        	}
        }}

		for(int i=0;i<Errored_Bits.size();i++)
		{
			Corrected_Bits.add(Errored_Bits.get(i));
		}
		Errored_Bits.clear();
		return 0;
	}
	}


