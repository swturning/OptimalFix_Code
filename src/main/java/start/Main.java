package start;
import java.util.*;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();


        HashMap<Integer,Integer> hashMap = new HashMap<>();

        for (int i = 0; i < n; i++) {

            int x = scanner.nextInt();

            if (hashMap.containsKey(x)){
                int a = hashMap.get(x);
                a++;
                hashMap.put(x,a);
            }else {
                hashMap.put(x,1);
            }


        }

        List<Integer> sortedKeys = new ArrayList<>(hashMap.keySet());
        Collections.sort(sortedKeys);
        int lastTime = sortedKeys.get(0);
        int sum = 0;




        for (int i=0;i<sortedKeys.size();i++) {
            if (i!=sortedKeys.size()-1){
                if (sortedKeys.get(i+1)>lastTime){
                    sum+= Math.max(lastTime-sortedKeys.get(i), 0)*hashMap.get(sortedKeys.get(i));

                    lastTime = sortedKeys.get(i)+m;

                }else {
                    sum+= Math.max(lastTime-sortedKeys.get(i), 0)*hashMap.get(sortedKeys.get(i));

                }
            }else {
                sum+= Math.max(lastTime-sortedKeys.get(i), 0)*hashMap.get(sortedKeys.get(i));
            }

        }
       // System.out.println(sum);


    }


}