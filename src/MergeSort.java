/**
 * Buffered MergeSort implementation sorting integers from a file.
 *
 */

import java.io.IOException;
import java.io.RandomAccessFile;

public class MergeSort {
    private RandomAccessFile sourceF;
    private Buffer sourceBuf;
    private Buffer leftBuf;
    private Buffer rightBuf;
    private int bufSize;
    private int maxIter;
    private int totalN;
    public MergeSort(RandomAccessFile sourceF, int bufSize){
        this.bufSize = bufSize;
        this.sourceF = sourceF;
        //Initializing contents of leftFile and RightFile
        try {
            //Creating RA files
            RandomAccessFile rightF = new RandomAccessFile("right", "rw");
            RandomAccessFile leftF = new RandomAccessFile("left", "rw");
            totalN = (int)sourceF.length()/4;
            maxIter = (int)Math.ceil(Math.log(totalN)/Math.log(2)); //Maximum number of passes in MergeSort
            //Initializing buffers
            this.sourceBuf = new Buffer(sourceF, bufSize, "sourceBuf");
            this.leftBuf = new Buffer(leftF, bufSize, "leftBuf");
            this.rightBuf = new Buffer(rightF, bufSize, "rightBuf");
            //Populating the left / right files with dummy data though the buffers.
            for(int i = 0; i < totalN/2; i++){
                leftBuf.append(0);
                rightBuf.append(0);
            }
            //Flushing the buffers in case anything is left
            leftBuf.writeToFile();
            rightBuf.writeToFile();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void sort() throws IOException {
        for(int pass = 0, groupSize = 1; pass < maxIter; pass++, groupSize *=2)
        {
            split(sourceBuf, leftBuf, rightBuf, groupSize);
            merge(sourceBuf, leftBuf, rightBuf, groupSize);
        }
    }

    public void split(Buffer sourceBuf, Buffer leftBuf, Buffer rightBuf, int groupSize) throws IOException{
        //We seek to the beginning of each file and we clear the buffers in case there is data.
        sourceBuf.reset();
        sourceBuf.clear();
        leftBuf.reset();
        leftBuf.clear();
        rightBuf.reset();
        rightBuf.clear();
        /*
        The number of lists of size group size is totalN/groupSize. However, because we have two files
        There is half the lists on the left and the other half in the right.
         */
        for(int i = 0; i < totalN/(2*groupSize); i++){
            for(int j = 0; j < groupSize; j++){
                int a = sourceBuf.read();
                leftBuf.append(a);
            }
            for(int k = 0; k < groupSize; k++){
                int a = sourceBuf.read();
                rightBuf.append(a);
            }
        }

        //There might be something in the buffers left.
        leftBuf.writeToFile();
        rightBuf.writeToFile();
    }

    public void merge(Buffer sourceBuf, Buffer leftBuf, Buffer rightBuf, int groupSize) throws  IOException{
        //Similar as in split, we seek to the beginning of the file and clear the buffers.
        sourceBuf.reset();
        sourceBuf.clear();
        leftBuf.reset();
        leftBuf.clear();
        rightBuf.reset();
        rightBuf.clear();
        int left, right;
        int leftCount = 0, rightCount = 0;
        for(int i = 0; i < totalN/(2*groupSize); i++){
            leftCount  = rightCount = groupSize;
            left = leftBuf.read();
            right = rightBuf.read();
            while((leftCount != 0) && (rightCount != 0)){
                /*
                * Current iteration ends with appending to the buffer and saving the NEXT stage of the variables
                * left and right.
                * Hence, before leaving to the next iteration, we check if the next stage is even possible. We call
                * this condition C1
                * */
                if(left<right){
                    sourceBuf.append(left);
                    if(--leftCount != 0){ //Checking condition C1
                        left = leftBuf.read();
                    }
                }else{
                    sourceBuf.append(right);
                    if(--rightCount != 0){ //Checking condition C1
                        right = rightBuf.read();
                    }
                }
            }
            //We apply the same logic for the remaining lists. Only one of them must be empty.
            if(leftCount != 0){
               while (leftCount-- != 0){
                   sourceBuf.append(left);
                   if(leftCount != 0){//Condition C1
                       left = leftBuf.read();
                   }
               }
            }else{
               while(rightCount-- != 0){
                   sourceBuf.append(right);
                   if(rightCount != 0){//Condition C1
                       right = rightBuf.read();
                   }
               }
            }
        }//end of for. We might want to flush the source buffer
        sourceBuf.writeToFile();
    }
}
