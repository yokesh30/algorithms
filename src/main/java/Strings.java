import java.util.*;

public class Strings {
    public String minWindow(String S, String T) {
        if (S.length() == 0 || T.length() == 0) {
            return "";
        }

        /**
         * we can conduct two steps by using two pointers for this probelm:
         * 1. check feasibility from left to right
         * 2. check optimization from right to left
         * we can traverse from left to right, find a possible candidate until reach the first ending character of T
         * eg: for the string s = abcdebdde and t = bde, we should traverse s string until we find first e,
         * i.e. abcde, then traverse back from current "e" to find if we have other combination of bde with smaller
         * length.
         * @param right: fast pointer that always points the last character of T in S
         * @param left: slow pointer that used to traverse back when right pointer find the last character of T in S
         * @param tIndex: third pointer used to scan string T
         * @param minLen: current minimum length of subsequence
         * */
        int right = 0;
        int minLen = Integer.MAX_VALUE;
        String result = "";

        while (right < S.length()) {
            int tIndex = 0;
            // use fast pointer to find the last character of T in S
            while (right < S.length()) {
                if (S.charAt(right) == T.charAt(tIndex)) {
                    tIndex++;
                }
                if (tIndex == T.length()) {
                    break;
                }
                right++;
            }

            // if right pointer is over than boundary
            if (right == S.length()) {
                break;
            }

            // use another slow pointer to traverse from right to left until find first character of T in S
            int left = right;
            tIndex = T.length() - 1;
            while (left >= 0) {
                if (S.charAt(left) == T.charAt(tIndex)) {
                    tIndex--;
                }
                if (tIndex < 0) {
                    break;
                }
                left--;
            }
            // if we found another subsequence with smaller length, update result
            if (right - left + 1 < minLen) {
                minLen = right - left + 1;
                result = S.substring(left, right + 1);
            }
            // WARNING: we have to move right pointer to the next position of left pointer, NOT the next position
            // of right pointer
            right = left + 1;
        }
        return result;
    }

    public String licenseKeyFormatting(String S, int K) {
        StringBuilder sb = new StringBuilder();
        for (int i = S.length() - 1; i >= 0; i--)
            if (S.charAt(i) != '-')
                sb.append(sb.length() % (K + 1) == K ? '-' : "").append(S.charAt(i));
        return sb.reverse().toString().toUpperCase();
    }

    public int numUniqueEmails(String[] emails) {
        HashSet<String> hm = new HashSet<>();
        for (String email : emails) {
            String domain = email.split("@")[1];
            String address = email.split("@")[0];
            address = address.replace(".", "");
            address = address.contains("+") ? address.substring(0, address.indexOf("+")) : address;
            hm.add(address + "@" + domain);
        }
        return hm.size();
    }

    public boolean canMeasureWater(int x, int y, int z) {
        //limit brought by the statement that water is finallly in one or both buckets
        if (x + y < z) return false;
        //case x or y is zero
        if (x == z || y == z || x + y == z) return true;

        //get GCD, then we can use the property of Bézout's identity
        return z % GCD(x, y) == 0;
    }

    public int GCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public String nextClosestTime(String time) {
        char[] chars = time.toCharArray();
        Character[] digits = new Character[]{chars[0], chars[1], chars[3], chars[4]};
        TreeSet<Character> treeSet = new TreeSet<Character>(java.util.Arrays.asList(digits));

        chars[4] = next(treeSet, chars[4], '9');
        if (time.charAt(4) < chars[4]) return String.valueOf(chars);

        chars[3] = next(treeSet, chars[3], '5');
        if (time.charAt(3) < chars[3]) return String.valueOf(chars);

        chars[1] = next(treeSet, chars[1], chars[0] == '2' ? '3' : '9');
        if (time.charAt(1) < chars[1]) return String.valueOf(chars);

        chars[0] = next(treeSet, chars[0], '2');
        return String.valueOf(chars);

    }

    public char next(TreeSet<Character> treeSet, char current, char max) {
        Character high = treeSet.higher(current);
        return high == null || high > max ? treeSet.first() : high;
    }

    public int numMatchingSubseq(String S, String[] words) {
        Map<Character, Queue<String>> map = new HashMap<>();
        int count = 0;
        for (int i = 0; i < S.length(); i++) {
            map.putIfAbsent(S.charAt(i), new LinkedList<>());
        }
        for (String word : words) {
            char c = word.charAt(0);
            if (map.containsKey(c)) {
                map.get(c).offer(word);
            }
        }
        for (int i = 0; i < S.length(); i++) {
            char c = S.charAt(i);
            Queue<String> q = map.get(c);
            int size = q.size();
            for (int k = 0; k < size; k++) {
                String str = q.poll();
                if (str.length() == 1) {
                    count++;
                } else {
                    if (map.containsKey(str.charAt(1))) {
                        map.get(str.charAt(1)).add(str.substring(1));
                    }
                }
            }
        }
        return count;
    }

    public List<String> invalidTransactions(String[] transactions) {
        if (transactions == null || transactions.length == 0) return new ArrayList<>();

        HashMap<String, List<Transactions>> map = new HashMap<>();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < transactions.length; i++) {
            String[] data = transactions[i].split(",");
            String name = data[0];
            if (Integer.valueOf(data[2])>1000){
                result.add(transactions[i]);
                continue;
            }
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
                map.get(name).add(new Transactions(Integer.valueOf(data[1]), Integer.valueOf(data[2]), data[3]));

            } else {
                List<Transactions> trans = map.get(name);
                for (int j = 0; j < trans.size(); j++) {
                    Transactions t = trans.get(j);
                    if ((Integer.valueOf(data[1]) - t.time) <= 60 && !data[3].equals(t.city)) {
                        result.add(transactions[i]);
                        result.add(String.valueOf(name + "," + t.time + "," + t.amount + "," + t.city));
                    }
                }
            }
        }
        return result;
    }

    public String removeDuplicates(String s, int k) {
        StringBuilder sb=new StringBuilder();
        String temp=s.charAt(0)!=s.charAt(1)?"d":"";
        int count=k;
        int right=1;
        int left=0;

        while(right<s.length()-1 && s.length()>k){
            boolean isDelete=shouldDelete(s, right,k);
            if(isDelete){
                s=s.substring(0, right)+s.substring(right+k);
                right=0;
            } else{
                right++;
            }

        }

        return s;
    }

    private boolean shouldDelete(String str, int start, int k){
        for(int i=start;i<start+k-1;i++){
            if(str.charAt(i)!=str.charAt(i+1)){
                return false;
            }
        }
        return true;
    }

    public boolean validWordSquare(List<String> words) {
        if(words == null || words.size() == 0){
            return true;
        }
        int n = words.size();
        for(int i=0; i<n; i++){
            for(int j=0; j<words.get(i).length(); j++){
                if(j >= n || words.get(j).length() <= i || words.get(j).charAt(i) != words.get(i).charAt(j))
                    return false;
            }
        }
        return true;
    }

    public static void main(String args[]) {
        Strings s = new Strings();
        boolean b1 = s.validWordSquare(new ArrayList<String>(java.util.Arrays.asList("abcd", "bnrt", "crmy", "dtye")));
        String abc = s.removeDuplicates("yfttttfbbbbnnnnffbgffffgbbbbgssssgthyyyy", 4);
        List<String> strings = s.invalidTransactions(new String[]{"bob,689,1910,barcelona","alex,696,122,bangkok","bob,832,1726,barcelona","bob,820,596,bangkok","chalicefy,217,669,barcelona","bob,175,221,amsterdam"});
        int abcde = s.numMatchingSubseq("abcde", new String[]{"a", "bb", "acd", "ace"});
        String s3 = s.nextClosestTime("19:34");
        boolean b = s.canMeasureWater(3, 5, 4);
        int i = s.numUniqueEmails(new String[]{"test.email+alex@leetcode.com", "test.e.mail+bob.cathy@leetcode.com", "testemail+david@lee.tcode.com"});
        String s2 = s.licenseKeyFormatting("5F3Z-2e-9-w", 4);
        String s1 = s.minWindow("abcdebdde", "bde");
    }
}

class Transactions {
    int time;
    int amount;
    String city;

    public Transactions(int time, int amount, String city) {
        this.time = time;
        this.amount = amount;
        this.city = city;
    }
}
