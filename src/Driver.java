import java.io.IOException;
import java.io.RandomAccessFile;

public class Driver {
    public static void main(String[] args) {
        try{
            String name = "Big.dat";
            int n = (int)Math.pow(2,24);
            int bufSize = (int)Math.pow(2,10);
            //next line only if file does not exist
            //IntegerPopulator integerPopulator = new IntegerPopulator(n, name);
            RandomAccessFile sourceFile = new RandomAccessFile(name, "rw");
            MergeSort sortingTool = new MergeSort(sourceFile, bufSize);
            long start = System.currentTimeMillis();
            sortingTool.sort();
            long end = System.currentTimeMillis();
            System.out.println("Sorting: " + name);
            System.out.println("Time: " + (end-start)/1000 + " s");
            printTailHead(sourceFile, 5 );

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /*
    * Prints the head and tail of a file.
    *
    * */
    public static void printTailHead(RandomAccessFile file, int n)throws IOException {
        int i = 0;
        file.seek(0L);
        //printing head
        System.out.println("HEAD");
        while(i++ < n){
            System.out.println(file.readInt());
        }
        file.seek(file.length() - 4*n);
        i = 0;
        System.out.println("TAIL");
        while(i++ < n){
            System.out.println(file.readInt());
        }

    }
}
