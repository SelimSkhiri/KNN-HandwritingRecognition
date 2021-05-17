package projet_java;


public class KNN {
	public static void main(String[] args) {
	byte b1 = 40; // 00101000
	byte b2 = 20; // 00010100
	byte b3 = 10; // 00001010
	byte b4 = 5; // 00000101

	// [00101000 | 00010100 | 00001010 | 00000101] = 672401925
	int result = extractInt(b1, b2, b3, b4);
	System.out.println(result);

	String bits = "10000001";
	System.out.println("La séquence de bits " + bits + "\n\tinterprète comme byte non signé donne "
			+ Helpers.interpretUnsigned(bits) + "\n\tinterprète comme byte signé donne "
			+ Helpers.interpretSigned(bits));
	}


	public static int extractInt(byte b31ToB24, byte b23ToB16, byte b15ToB8, byte b7ToB0) {
        byte[] bytes = new byte[]  {b31ToB24,b23ToB16,b15ToB8,b7ToB0};
        int res = 0;
		
        for (int i = 0; i < 4 && i+0<bytes.length; i++) {
        	res <<= 8;
        	res |= (int)bytes[i] & 0xFF;
        }		
        return (res);
	}
    
	
	public static byte[][][] parseIDXimages(byte[] data) {
	int magicNumber = extractInt(data[0],data[1], data[2], data[3]);
	
	if (magicNumber != 2051) {
		System.out.println("ERREUR : fichier ne contenant pas les images");
           	return(null);}
	else {
		int nombre_image = extractInt(data[4],data[5], data[6], data[7]);
		int height = extractInt(data[8],data[9], data[10], data[11]);
		int width = extractInt(data[12],data[13], data[14], data[15]);
                byte[][][] image=new byte[nombre_image][height][width];
                int i=16;
                do {
                    for (int j = 0; j < nombre_image; j++) {
                    	for (int k = 0; k < height; k++) {
                       		for (int p = 0; p < width; p++) {
                        		if (data[i]==0)
                        			image[j][k][p] = (byte)((data[i]&0xff)-128);
                        		else
                        			image[j][k][p] = (byte)((data[i]&0xff+127));
                        		i++; 
                        }           
                }}
                } while(i < data.length);
                return (image);                
	}}
	
	
	public static byte[] parseIDXlabels(byte[] data) {	
	int magicNumber = extractInt(data[0],data[1], data[2], data[3]);
	
	if (magicNumber != 2049) {
		System.out.println("ERREUR : fichier ne contenant pas des étiquettes");
                return null;}
	else {
		int nbLabel = extractInt(data[4],data[5], data[6], data[7]);
                byte[] labels = new byte[nbLabel];
                for(int i = 0; i < nbLabel; i++)
                	labels[i] = data[i+8];
                return (labels);
        }}	
	
	
	public static float squaredEuclideanDistance(byte[][] a, byte[][] b) {
        int height = a.length;
        int width  = (a[0]).length;
        float E    = 0;
        
	for(int i = 0; i < height; i++) {       
		for(int j = 0; j < width; j++) {
              		E += (a[i][j]-b[i][j])*(a[i][j]-b[i][j]);
	    }}
	return (E);
	}	
	
	
  	public static float average (byte[][] a) {
        float average = 0;
        int lenI = a.length, lenJ = a[0].length;
        
        for (int i = 0; i < lenI; i++)
            for (int j = 0; j < lenJ; j++)
                average += a[i][j];
        return average / (lenI * lenJ);
	}
    
        
    	public static float invertedSimilarity(byte[][] a, byte[][] b) {
        float auxA = 0, auxB = 0;  
        float denominator,numerator = 0;
        float tempA,tempB;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                tempA = a[i][j] - average(a);
                tempB = b[i][j] - average(b);
                numerator += (tempA * tempB); 
                auxA += Math.pow(tempA,2);
                auxB += Math.pow(tempB,2);
            }}
        denominator = (float) Math.sqrt(auxA * auxB);
        if (denominator == 0 ) {return 2;}
        else {return (float) 1 - (numerator/denominator) ;}
	}

	
	public static int[] quicksortIndices(float[] values) {         
	int[] indices = new int[values.length];
		
        for (int i = 0; i < values.length; i++) {
        	indices[i] = i;
           }
        quicksortIndices(values,indices,0,(values.length)-1);
        return (indices);
	}


	public static int[] quicksortIndices(float[] values, int[] indices, int low, int high) {
	int l = low;
        int h = high;
        float pivot = values[low];
        
        while ( l <= h) {
        	if (values[l] < pivot)
                	l++;
             	else if (values[h] > pivot)
                	h--;
             	else {
                	swap(l,h,values,indices);
                	l++;
                	h--;  
        	}}
    	if (low<h)
  		quicksortIndices(values,indices,low,h);
     	if (high>l)
       		quicksortIndices(values,indices,l,high);
     	return (indices);
	}
      

	public static void swap(int i, int j, float[] values, int[] indices) {   
	float aux1 = values[i];
        int aux2   = indices[i];        
        values[i]  = values[j];
       	values[j]  = aux1;
     	indices[i] = indices[j];
     	indices[j] = aux2;
	}

	
	public static int firstOcc(int[]array,int max) {
		for (int i = 0; i < array.length; i++) {
			if(array[i] == max)
				return (i);
           	    }
       	return -1;
	}
	
	
   	public static int indexOfMax(int[] array) {
	int max = array[0];
	int pos = 0;
          
	for (int i = 0; i < array.length; i++) {
		if (array[i] > max)
			{ max = array[i];  
                    	pos = i;
     	    }}
         	if (firstOcc(array,max) != -1)
            		return pos;
          	else
            		return (firstOcc(array,max));
	}


	public static byte electLabel(int[] sortedIndices, byte[] labels, int k) {
	int[] T = new int[10];
		
        for (int i = 0; i < k; i++) {
        	T[labels[sortedIndices[i]]]++;
            }
        return (byte)(indexOfMax(T));  
	}

	
	public static byte knnClassify(byte[][] image, byte[][][] trainImages, byte[] trainLabels, int k) {
        float[]distances=new float[trainImages.length]; 
		
	for (int i = 0; i < trainImages.length; i++) {
		float distance1 = squaredEuclideanDistance(image, trainImages[i]);
            	distances[i] = distance1;
            }
        byte indices=electLabel(quicksortIndices(distances),trainLabels,k);
        return indices;
	}


	public static double accuracy(byte[] predictedLabels, byte[] trueLabels) {
    	double n = predictedLabels.length;
        double a = 0;
            
	for (int i = 0; i < n; i++) {
		if (predictedLabels[i] == (trueLabels[i]))
			a += (1/n);
            }
        return (a);
	}
}
