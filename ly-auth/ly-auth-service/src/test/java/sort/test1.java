package sort;

import org.junit.Test;

import java.util.ArrayList;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class test1 {

    @Test
    public void bubbleSort() {
        int[] arr = {2,6,3,7,1,9,8};
        for(int i =1;i<arr.length;i++) {
            for(int j=0;j<arr.length-i;j++) {
                if(arr[j]>arr[j+1]) {
                    int temp = arr[j];

                    arr[j]=arr[j+1];

                    arr[j+1]=temp;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    @Test
    public void bubbleSort2() {
        int[] arr = {2,6,3,7,1,9,8};

        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i; j++) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    @Test
    public void SelectSort() {
        int[] arr = {2,6,3,7,1,9,8};

        for (int i = 0; i < arr.length -1; i++) {
            int min = i;
            for (int j = 1+i; j < arr.length; j++) {
                if (arr[min] > arr[j]){
                    min = j;
                }
            }
            if (min != i) {

                int tmp = arr[min];
                arr[min] = arr[i];
                arr[i] = tmp;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }


    @Test
    public void SelectSort2() {
        int[] arr = {2,6,3,7,1,9,8};

        for (int i = 0; i < arr.length -1; i++) {
            for (int j = 1+i; j < arr.length; j++) {
                if (arr[i] > arr[j]){
                    int t = arr[i];
                    arr[i] = arr[j];
                    arr[j] = t;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }


    @Test
    public void tt() {
        ArrayList arrayList = new ArrayList();
        int a =1;

    }

}
