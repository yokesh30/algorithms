import java.lang.reflect.Array;
import java.util.*;
import java.util.Arrays;

public class Matrix {
    public int[][] candyCrush(int[][] board) {
        int N = board.length, M = board[0].length;
        boolean found = true;
        while (found) {
            found = false;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    int val = Math.abs(board[i][j]);
                    if (val == 0) continue;
                    if (j < M - 2 && Math.abs(board[i][j + 1]) == val && Math.abs(board[i][j + 2]) == val) {
                        found = true;
                        int ind = j;
                        while (ind < M && Math.abs(board[i][ind]) == val) board[i][ind++] = -val;
                    }
                    if (i < N - 2 && Math.abs(board[i + 1][j]) == val && Math.abs(board[i + 2][j]) == val) {
                        found = true;
                        int ind = i;
                        while (ind < N && Math.abs(board[ind][j]) == val) board[ind++][j] = -val;
                    }
                }
            }
            if (found) { // move positive values to the bottom, then set the rest to 0
                for (int j = 0; j < M; j++) {
                    int storeInd = N - 1;
                    for (int i = N - 1; i >= 0; i--) {
                        if (board[i][j] > 0) {
                            board[storeInd--][j] = board[i][j];
                        }
                    }
                    for (int k = storeInd; k >= 0; k--) board[k][j] = 0;
                }
            }
        }
        return board;
    }

    public int networkDelayTime(int[][] times, int N, int K) {
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        for (int[] time : times) {
            map.putIfAbsent(time[0], new HashMap<>());
            map.get(time[0]).put(time[1], time[2]);
        }

        //distance, node into pq
        Queue<int[]> pq = new PriorityQueue<>((a, b) -> (a[0] - b[0]));

        pq.add(new int[]{0, K});

        boolean[] visited = new boolean[N + 1];
        int res = 0;

        while (!pq.isEmpty()) {
            int[] cur = pq.remove();
            int curNode = cur[1];
            int curDist = cur[0];
            if (visited[curNode]) continue;
            visited[curNode] = true;
            res = curDist;
            N--;
            if (map.containsKey(curNode)) {
                for (int next : map.get(curNode).keySet()) {
                    pq.add(new int[]{curDist + map.get(curNode).get(next), next});
                }
            }
        }
        return N == 0 ? res : -1;

    }

    public boolean carPooling(int[][] trips, int capacity) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int[] t : trips) {
            m.put(t[1], m.getOrDefault(t[1], 0) + t[0]);
            m.put(t[2], m.getOrDefault(t[2], 0) - t[0]);
        }
        for (int v : m.values()) {
            capacity -= v;
            if (capacity < 0) {
                return false;
            }
        }
        return true;
    }

    public int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int[][] cache = new int[matrix.length][matrix[0].length];
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int length = findSmallAround(i, j, matrix, cache, Integer.MAX_VALUE);
                max = Math.max(length, max);
            }
        }
        return max;
    }

    private int findSmallAround(int i, int j, int[][] matrix, int[][] cache, int pre) {
        // if out of bond OR current cell value larger than previous cell value.
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length || matrix[i][j] >= pre) {
            return 0;
        }
        // if calculated before, no need to do it again
        if (cache[i][j] > 0) {
            return cache[i][j];
        } else {
            int cur = matrix[i][j];
            int tempMax = 0;
            tempMax = Math.max(findSmallAround(i - 1, j, matrix, cache, cur), tempMax);
            tempMax = Math.max(findSmallAround(i + 1, j, matrix, cache, cur), tempMax);
            tempMax = Math.max(findSmallAround(i, j - 1, matrix, cache, cur), tempMax);
            tempMax = Math.max(findSmallAround(i, j + 1, matrix, cache, cur), tempMax);
            cache[i][j] = ++tempMax;
            return tempMax;
        }
    }

    public int longestLine(int[][] M) {
        if (M.length == 0 || M[0].length == 0) return 0;

        int[] col = new int[M[0].length];
        int[] diag = new int[M.length + M[0].length - 1];
        int[] antiDiag = new int[M.length + M[0].length - 1];
        int row = 0;
        int max = 0;
        for (int i = 0; i < M.length; i++) {
            row = 0;
            for (int j = 0; j < M[0].length; j++) {
                if (M[i][j] == 1) {
                    row++;
                    col[j]++;
                    antiDiag[j + i]++;
                    diag[M.length + j - i - 1]++;

                    max = Math.max(max, row);
                    max = Math.max(max, col[j]);
                    max = Math.max(max, antiDiag[j + i]);
                    max = Math.max(max, diag[M.length + j - i - 1]);
                } else {
                    row = 0;
                    col[j] = 0;
                    antiDiag[j + i] = 0;
                    diag[M.length + j - i - 1] = 0;

                }
            }
        }

        return max;
    }

    public int maxSumSubmatrix(int[][] matrix, int k) {
        //2D Kadane's algorithm + 1D maxSum problem with sum limit k
        //2D subarray sum solution

        //boundary check
        if (matrix.length == 0) return 0;

        int m = matrix.length, n = matrix[0].length;
        int result = Integer.MIN_VALUE;

        //outer loop should use smaller axis
        //now we assume we have more rows than cols, therefore outer loop will be based on cols
        for (int left = 0; left < n; left++) {
            //array that accumulate sums for each row from left to right
            int[] sums = new int[m];
            for (int right = left; right < n; right++) {
                //update sums[] to include values in curr right col
                for (int i = 0; i < m; i++) {
                    sums[i] += matrix[i][right];
                }

                //we use TreeSet to help us find the rectangle with maxSum <= k with O(logN) time
                TreeSet<Integer> set = new TreeSet<Integer>();
                //add 0 to cover the single row case
                set.add(0);
                int currSum = 0;

                for (int sum : sums) {
                    currSum += sum;
                    //we use sum subtraction (curSum - sum) to get the subarray with sum <= k
                    //therefore we need to look for the smallest sum >= currSum - k
                    Integer num = set.ceiling(currSum - k);
                    if (num != null) result = Math.max(result, currSum - num);
                    set.add(currSum);
                }
            }
        }

        return result;
    }

    public int numSubmatrixSumTarget(int[][] A, int target) {
        int res = 0, m = A.length, n = A[0].length;
        for (int i = 0; i < m; i++)
            for (int j = 1; j < n; j++)
                A[i][j] += A[i][j - 1];
        Map<Integer, Integer> counter = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                counter.clear();
                counter.put(0, 1);
                int cur = 0;
                for (int k = 0; k < m; k++) {
                    cur += A[k][j] - (i > 0 ? A[k][i - 1] : 0);
                    res += counter.getOrDefault(cur - target, 0);
                    counter.put(cur, counter.getOrDefault(cur, 0) + 1);
                }
            }
        }
        return res;
    }

    public int connectedComponents(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        int result = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mat[i][j] != -1)
                    result = Math.max(result, dfs(mat, i, j, 0, mat[i][j]));
            }
        }
        return result;
    }

    public int dfs(int[][] mat, int i, int j, int result, int val) {
        if (i < 0 || i > mat.length - 1 || j < 0 || j > mat[0].length - 1 || mat[i][j] != val) {
            return result;
        }

        result = result + 1;
        mat[i][j] = -1;
        result = dfs(mat, i + 1, j, result, val);
        result = dfs(mat, i - 1, j, result, val);
        result = dfs(mat, i, j + 1, result, val);
        result = dfs(mat, i, j - 1, result, val);
        return result;
    }

    int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public int minTotalDistance(int[][] grid) {
        int m = grid.length, n = grid[0].length;

        List<Integer> I = new ArrayList<Integer>();
        List<Integer> J = new ArrayList<Integer>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    I.add(i);
                }
            }
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                if (grid[i][j] == 1) {
                    J.add(j);
                }
            }
        }
        return minTotalDistance(I) + minTotalDistance(J);
    }

    public int minTotalDistance(List<Integer> grid) {
        int i = 0, j = grid.size() - 1, sum = 0;
        while (i < j) {
            sum += grid.get(j--) - grid.get(i++);
        }
        return sum;
    }

    public int assignBikes(int[][] workers, int[][] bikes) {
        PriorityQueue<node> pq = new PriorityQueue<node>((a, b) -> Integer.compare(a.cost, b.cost));
        HashSet<String> visited = new HashSet();
        pq.add(new node(0, 0, 0));
        while (!pq.isEmpty()) {
            node cur = pq.remove();
            if (cur.id == workers.length) {
                return cur.cost;
            }
            if (visited.contains(cur.id + "_" + cur.mask)) {
                continue;
            }
            visited.add(cur.id + "_" + cur.mask);
            for (int j = 0; j < bikes.length; j++) {
                int i = 1 << j;
                int i1Or = cur.mask | i;
                int i2And = cur.mask & i;
                if (i2And == 0) {
                    int id = cur.id + 1;
                    System.out.println("ID: " + id + ",cost: " + cur.cost + ",dist: " + get(workers, bikes, cur.id, j) + ",mask" + i1Or);
                    pq.offer(new node(cur.id + 1, cur.cost + get(workers, bikes, cur.id, j), i1Or));
                }
            }
        }
        return -1;
    }

    public int get(int[][] workers, int[][] bikes, int i, int j) {
        int i1 = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
        return i1;
    }

    class node {
        int id;
        int cost;
        int mask;

        public node(int id, int cost, int m) {
            this.id = id;
            this.cost = cost;
            mask = m;
        }
    }

    public List<List<String>> accountsMerge(List<List<String>> acts) {
        Map<String, String> owner = new HashMap<>();
        Map<String, String> parents = new HashMap<>();
        Map<String, TreeSet<String>> unions = new HashMap<>();
        for (List<String> a : acts) {
            for (int i = 1; i < a.size(); i++) {
                parents.put(a.get(i), a.get(i));
                owner.put(a.get(i), a.get(0));
            }
        }
        for (List<String> a : acts) {
            String p = find(a.get(1), parents);
            for (int i = 2; i < a.size(); i++)
                parents.put(find(a.get(i), parents), p);
        }
        for (List<String> a : acts) {
            String p = find(a.get(1), parents);
            if (!unions.containsKey(p))
                unions.put(p, new TreeSet<>());
            for (int i = 1; i < a.size(); i++)
                unions.get(p).add(a.get(i));
        }
        List<List<String>> res = new ArrayList<>();
        for (String p : unions.keySet()) {
            List<String> emails = new ArrayList(unions.get(p));
            emails.add(0, owner.get(p));
            res.add(emails);
        }
        return res;
    }

    private String find(String s, Map<String, String> p) {
        return p.get(s) == s ? s : find(p.get(s), p);
    }

    public boolean areSentencesSimilar(String[] words1, String[] words2, List<List<String>> pairs) {
        if (words1.length != words2.length) return false;

        Map<String, Set<String>> map = new HashMap();
        for (List<String> pair : pairs) {
            String a1 = pair.get(0);
            String a2 = pair.get(1);
            map.putIfAbsent(a1, new HashSet<String>());
            map.putIfAbsent(a2, new HashSet<String>());
            map.get(a1).add(a2);
            map.get(a2).add(a1);
        }
        for (int i = 0; i < words1.length; i++) {
            if (words1[i].equals(words2[i])) continue;
            if (map.containsKey(words1[i]) && map.get(words1[i]).contains(words2[i])) continue;
            if (map.containsKey(words2[i]) && map.get(words2[i]).contains(words1[i])) continue;
            return false;
        }
        return true;
    }

    public boolean areSentencesSimilarTwo(String[] a, String[] b, List<List<String>> pairs) {
        if (a.length != b.length) return false;
        Map<String, String> m = new HashMap<>();
        for (List<String> p : pairs) {
            String parent1 = find(m, p.get(0));
            String parent2 = find(m, p.get(1));
            if (!parent1.equals(parent2))
                m.put(parent1, parent2);
        }

        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(b[i]) && !find(m, a[i]).equals(find(m, b[i]))) return false;

        return true;
    }

    private String find(Map<String, String> m, String s) {
        if (!m.containsKey(s)) m.put(s, s);
        return s.equals(m.get(s)) ? s : find(m, m.get(s));
    }

    public static void main(String args[]) {
        Matrix matrix = new Matrix();
        List<List<String>> input = new ArrayList<>();
        List<List<String>> pairs = new ArrayList<>();
        pairs.add(java.util.Arrays.asList("great", "fine"));
        pairs.add(java.util.Arrays.asList("acting", "drama"));
        pairs.add(java.util.Arrays.asList("skills", "talent"));
        boolean b = matrix.areSentencesSimilar(new String[]{"great", "acting", "skills"},
                new String[]{"fine", "drama", "talent"}, pairs);

        boolean b1 = matrix.areSentencesSimilarTwo(new String[]{"great", "acting", "skills"},
                new String[]{"fine", "drama", "talent"}, pairs);

        input.add(Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"));
        input.add(Arrays.asList("John", "johnnybravo@mail.com"));
        input.add(Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"));
        input.add(Arrays.asList("Mary", "mary@mail.com"));

        List<List<String>> lists = matrix.accountsMerge(input);
        int i4 = matrix.assignBikes(new int[][]{{0, 0}, {2, 1}}, new int[][]{{1, 2}, {2, 3}});
//        int i3 = matrix.minTotalDistance(new int[][]{{1, 0, 0, 0, 1}, {0, 0, 0, 0, 0}, {0, 0, 1, 0, 0}});
//        int i2 = matrix.connectedComponents(new int[][]{{0, 0, 1, 2}, {0, 1, 2, 1}, {2, 1, 1, 1}});
////        int i2 = matrix.numSubmatrixSumTarget(new int[][]{{0, 5, 0}, {2, 3, 4}, {0, 2, 0}}, 0);
//        int i1 = matrix.maxSumSubmatrix(new int[][]{{1, 0, 1}, {0, -2, 3}}, 2);
//        int i = matrix.longestLine(new int[][]{{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 1}});
//        matrix.longestIncreasingPath(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}});
//        boolean b = matrix.carPooling(new int[][]{{2, 1, 5}, {3, 5, 7}}, 3);
//        matrix.networkDelayTime(new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2);
//        matrix.candyCrush(new int[][]{{110, 5, 112, 113, 114}, {210, 211, 5, 213, 214}, {310, 311, 3, 313, 314}, {410, 411, 412, 5, 414}, {5, 1, 512, 3, 3}, {610, 4, 1, 613, 614}, {710, 1, 2, 713, 714}, {810, 1, 2, 1, 1}, {1, 1, 2, 2, 2}, {4, 1, 4, 4, 1014}});

    }
}
