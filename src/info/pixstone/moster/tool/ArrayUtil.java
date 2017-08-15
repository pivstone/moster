package info.pixstone.moster.tool;
import java.util.*;
public class ArrayUtil{


public static int sortUtil(int head,int tail,int[] array){

	int keyValue=array[head];
	while(tail>head){
		while(array[tail]>=keyValue&&tail>head)
			tail--;
		int obj=array[head];
		array[head]=array[tail];
		array[tail]=obj;
		while(array[head]<=keyValue&&tail>head)
			head++;
		int obj1=array[head];
		array[head]=array[tail];
		array[tail]=obj1;		
	}
	array[tail] = keyValue;
	return head;
}

public static void sort(int head,int tail,int[] array){
	if(head>=tail) {
		return ;
	}
/* 	int key=(int)(Math.random() *(tail-head))+head;
	int temp=array[head];
	array[head]=array[key];
	array[key]=temp; */
	
	int index= sortUtil(0,array.length-1,array);
	sort(head,index-1,array);
	sort(index+1,tail-1,array);
}

public static void quickSort(int[] array){
	sort(0,array.length-1,array);
}

public static void swap(int left,int right){
	int temp= left;
	left=right;
	right=temp;
}
public static void main(String[] agrs){
		int[] pic={39,29,65,46,38,62,10};
		quickSort(pic);
		for(int i=0;i<pic.length;i++)
		System.out.println(pic[i]);
	}
}