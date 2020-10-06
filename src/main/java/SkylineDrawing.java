import java.util.*;

public class SkylineDrawing {

    /**
     * Represents either start or end of building
     */
    static class BuildingPoint implements Comparable<BuildingPoint> {
        int x;
        boolean isStart;
        int height;

        @Override
        public int compareTo(BuildingPoint o) {
            //first compare by x.
            //If they are same then use this logic
            //if two starts are compared then higher height building should be picked first
            //if two ends are compared then lower height building should be picked first
            //if one start and end is compared then start should appear before end
            if (this.x != o.x) {
                return this.x - o.x;
            } else {
                return (this.isStart ? -this.height : this.height) - (o.isStart ? -o.height : o.height);
            }
        }
    }

    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<int[]> buildingHeight=new ArrayList<>();

        for(int[] build: buildings){
            buildingHeight.add(new int[]{build[0],-build[2]});
            buildingHeight.add(new int[]{build[1],build[2]});
        }

        Collections.sort(buildingHeight, (a,b)-> (a[0]==b[0])?a[1]-b[1]:a[0]-b[0]);

        TreeMap<Integer, Integer> maxHeap=new TreeMap<>(Collections.reverseOrder());
        int previousMax=0;
        List<List<Integer>> result=new ArrayList<>();
        for(int[] building: buildingHeight){
            if(building[1]<0){
                maxHeap.putIfAbsent(-building[1], maxHeap.getOrDefault(-building[1],0)+1);
            } else{
                Integer count=maxHeap.get(building[1]);
                if(count==1){
                    maxHeap.remove(building[1]);
                } else{
                    maxHeap.put(building[1],count-1);
                }
            }

            int currentMax=maxHeap.firstKey();
            if(currentMax!=previousMax){
                previousMax=currentMax;
                result.add(java.util.Arrays.asList(building[0],currentMax));
            }
        }

        return result;

    }

    public static void main(String args[]) {
        int[][] buildings = {{1, 3, 4}, {3, 4, 4}, {2, 6, 2}, {8, 11, 4}, {7, 9, 3}, {10, 11, 2}};
        SkylineDrawing sd = new SkylineDrawing();
        List<List<Integer>> skyline = sd.getSkyline(buildings);
        //criticalPoints.forEach(cp -> System.out.println(cp[0] + " " + cp[1]));

    }
}
