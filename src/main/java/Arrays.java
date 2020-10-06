import java.util.*;

class Solution {
    private static long N;
    private static long A;
    private static long B;

    public static long getMax(List<Integer> houses, long budget) {
        if (houses == null || houses.size() == 0) return 0;
        long result = 0;
        for (int i = 1; i < houses.size(); i++) {
            long tempResult = Long.MIN_VALUE;
            for (int j = 0; j < i; j++) {
                if (houses.get(i) + houses.get(j) < budget) {
                    tempResult = tempResult == Integer.MIN_VALUE ? 2 : tempResult++;
                }
            }
            result = Math.max(tempResult, result);
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        for (long i = 0; i < N; i++) {
            A = sc.nextInt();
            B = sc.nextInt();
            ArrayList<Integer> houses = new ArrayList<Integer>();
            for (long j = 0; j < A; j++) {
                houses.add(sc.nextInt());
            }
            long no_of_houses = getMax(houses, B);
            System.out.println("Case #" + (i + 1) + ": " + no_of_houses);

        }

        sc.close();
    }
}