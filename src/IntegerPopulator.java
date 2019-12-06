/**
 * A simple class that populates a Random Access File with integers
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class IntegerPopulator {
    private int totalIntegers;
    private String name;
    public IntegerPopulator(int n, String name){
        try{
            RandomAccessFile file = new RandomAccessFile(name, "rw");
            Random random = new Random();
            file.seek(0);
            for(int i = 0; i < n; i++){
                file.writeInt(random.nextInt());
            }
        }catch (IOException e){

        }

    }
}
