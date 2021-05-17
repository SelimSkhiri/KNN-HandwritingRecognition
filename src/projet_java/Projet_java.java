
package projet_java;

import java.util.Scanner;
import static projet_java.KNN.knnClassify;
import static projet_java.KNN.parseIDXimages;
import static projet_java.KNN.parseIDXlabels;


public class Projet_java {

    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int TESTS = 0, K = 0, size = -10;
        
        do{System.out.println("Please enter the number of tests ]0..100000[ ");
        	TESTS=scan.nextInt();
        }while((TESTS<=0)||(TESTS>100000));
        do{System.out.println("Please enter the number of Neighbours ]0..10[");
        	K=scan.nextInt();
        }while((K<=0)||(K>10));
        do{System.out.println("Please choose the size of the dataset: 10,100,1000 or 5000?");
        	size=scan.nextInt();
        }while((!(size==10))&&(!(size==100))&&(!(size==1000))&&(!(size==5000)));
        scan.close();      
        
        byte[][][] trainImages = parseIDXimages( Helpers. readBinaryFile( "datasets\\"+size+"-per-digit_images_train")) ; 
        byte[]  trainLabels = 
        parseIDXlabels(Helpers.readBinaryFile("datasets\\"+size+"-per-digit_labels_train")); 
        System.out.println("We will be training our model on "+size+"-per-digit dataset and then testing it on 10k images and labels");
        byte[][][] testImages = 
     	parseIDXimages( Helpers. readBinaryFile( "datasets\\10k_images_test")) ;
        byte[]  testLabels = 
        parseIDXlabels(Helpers.readBinaryFile("datasets\\10k_labels_test")); 
        byte[] predictions = new byte[TESTS]; 
        for ( int i = 0; i < TESTS; i++) { predictions[i] = knnClassify(testImages[i], trainImages, trainLabels, K); } 
        Helpers.show("Test", testImages, predictions, testLabels, 20,35);
        double accuracy=KNN.accuracy(predictions, testLabels); 
        System.out.println("Accuracy: "+accuracy);

    }    
}
