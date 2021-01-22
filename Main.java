// Aidan Gray
import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Main {
    // Aidan Gray
    static int[][] adjacencyMatrix;
    static ArrayList<Platform> maxes, mins, platforms;

    static class Platform implements Comparable<Platform>{
        boolean maximal, minimal;
        BigInteger val;
        ArrayList<Platform> ins, outs;
        int inDegree, outDegree, index;

        Platform(BigInteger val){
            outDegree = 0;
            inDegree = 0;
            this.val = val;
        }

        public void initialize(ArrayList<Platform> plats, ArrayList<Platform> mins, ArrayList<Platform> maxes){
            ins = new ArrayList<Platform>();
            outs = new ArrayList<Platform>();
            index = plats.indexOf(this);

            if(mins.contains(this))
                minimal = true;
            if(maxes.contains(this))
                maximal = true;

            for(int i = 0; i < plats.size(); i++){
                if(adjacencyMatrix[index][i] == 1){
                    outs.add( plats.get(i) );
                    outDegree++;
                }
                if(adjacencyMatrix[i][index] == 1){
                    ins.add( plats.get(i) );
                    inDegree++;
                }
            }
        }

        public void zero_Out(){
            platforms.remove(this);
            maxes.remove(this);
            mins.remove(this);
            for(int i = 0; i < platforms.size(); i++){
                if( platforms.get(i).ins.contains(this)){
                    platforms.get(i).ins.remove(this);
                }
                if( platforms.get(i).outs.contains(this)){
                    platforms.get(i).outs.remove(this);
                }
            }
        }

        @Override
        public String toString(){
            return val.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Platform platform = (Platform) o;

            return val.equals(platform.val);
        }

        @Override
        public int hashCode() {
            return val.hashCode();
        }

        @Override
        public int compareTo(Platform p){
            return this.val.compareTo(p.val);
        }

        public static boolean canTraverse(Platform a, Platform b){
            return a.val.compareTo(b.val) == -1 && !a.val.gcd(b.val).equals(BigInteger.ONE);
        }

    }

    public static void takePaths() throws IOException{
        FileWriter f = new FileWriter("output.txt");
        BufferedWriter bF = new BufferedWriter(f);
        for(int i = 0; i < mins.size(); i++){
            if( mins.get(i).maximal/*maxes.contains(mins.get(i))*/){
                bF.write("1 "+mins.get(i)+" \n");
                mins.get(i).zero_Out();
            }
        }
        while(!mins.isEmpty()){
            String p = "";
            p = path(p, platforms.get(0));
            if(!p.equals(""))
                bF.write(p+" \n");
        }
        bF.close();
        f.close();
    }

    public static int containsMaximal(ArrayList<Platform> list){
        //returns index of maximal child with fewest parents, -1 if no maximal child
        sortListInDegree(list);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).maximal/*maxes.contains(list.get(i))*/){
                return i;
            }
        }
        return -1;
    }

    public static String path(String str, Platform p){
        if( /*maxes.contains(p)*/ p.maximal ){
            p.zero_Out();
            return str+" "+p;
        }
        if(containsMaximal(p.outs) != -1){
            str = str+" "+p;
            p.zero_Out();
            return path(str, p.outs.get(containsMaximal(p.outs)));
        }
        sortListOutDegree(p.outs);
        if(!p.equals(new Platform(BigInteger.ONE))){
            str = str+" "+p;
            p.zero_Out();
        }else{
            str = p.toString();
        }
        if(!p.outs.isEmpty()){
            return path(str, p.outs.get(0));
        }else return "";
    }

    public static void sortListOutDegree(ArrayList<Platform> p){
        for(int i = 0; i < p.size(); i++){
            int mindex = i;
            for(int j = i+1; j < p.size(); j++){
                if( p.get(i).outDegree < p.get(j).outDegree ){
                    mindex = j;
                }
            }
            Platform temp = p.get( mindex );
            p.set(mindex, p.get(i));
            p.set(i, temp);
        }
    }

    public static void sortListInDegree(ArrayList<Platform> p){
        for(int i = 0; i < p.size(); i++){
            int mindex = i;
            for(int j = i+1; j < p.size(); j++){
                if( p.get(i).inDegree < p.get(j).inDegree ){
                    mindex = j;
                }
            }
            Platform temp = p.get( mindex );
            p.set(mindex, p.get(i));
            p.set(i, temp);
        }
    }

    public static void computeMaximals(ArrayList<Platform> plats, ArrayList<Platform> maximals){
        ArrayList<Platform> maxTemps = (ArrayList<Platform>)plats.clone();
        maxTemps.remove(0);
        for(int i = 0; i < maxTemps.size(); i++){
            for(int j = i+1; j < maxTemps.size(); j++){
                if(maxTemps.get(j).val.gcd(maxTemps.get(i).val).compareTo(BigInteger.ONE) == 1){
                    maxTemps.remove(i--);
                    break;
                }
            }
        }
        maximals.addAll(maxTemps);
        for(int i = 0; i < plats.size(); i++){
            if(maximals.contains(plats.get(i)))
                plats.get(i).maximal = true;
        }
    }

    public static void computeMinimals(ArrayList<Platform> plats, ArrayList<Platform> minimals){
        ArrayList<Platform> minTemps = (ArrayList<Platform>)plats.clone();
        minTemps.remove(0);
        for(int i = minTemps.size() - 1; i > 0; i--){
            for(int j  = i -1; j >= 0; j--){
                if(minTemps.get(i).val.gcd(minTemps.get(j).val).compareTo(BigInteger.ONE) == 1){
                    minTemps.remove(i);
                    break;
                }
            }
        }
        minimals.addAll(minTemps);
        for(int i = 0; i < plats.size(); i++){
            if(minimals.contains(plats.get(i)))
                plats.get(i).minimal = true;
        }
    }

    public static void populateMatrix(ArrayList<Platform> plats, ArrayList<Platform> mins){
        for(int i = 1; i < plats.size(); i++){
            for(int j = 1; j < plats.size(); j++){
                if(Platform.canTraverse(plats.get(i), plats.get(j))){
                    adjacencyMatrix[i][j] = 1;
                }else adjacencyMatrix[i][j] = 0;
            }
        }
        for(int i = 0; i < plats.size(); i++){
            if(mins.contains(plats.get(i))){
                adjacencyMatrix[0][i] = 1;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        FileReader f = new FileReader("input.txt");
        BufferedReader reader = new BufferedReader(f);
        platforms = new ArrayList<Platform>();
        while(reader.ready()){ platforms.add(new Platform(new BigInteger(reader.readLine().trim()))); }
        maxes = new ArrayList<Platform>();
        mins = new ArrayList<Platform>();
        platforms.add(0, new Platform(BigInteger.ONE));
        Collections.sort(platforms);
        computeMaximals(platforms, maxes);
        computeMinimals(platforms, mins);
        adjacencyMatrix = new int[platforms.size()][platforms.size()];
        populateMatrix(platforms, mins);
        for(int i = 0; i < platforms.size(); i++){ platforms.get(i).initialize(platforms, mins, maxes); }
        for(int i = 0; i < platforms.size(); i++){ sortListOutDegree(platforms.get(i).ins); }
        takePaths();
    }
}
