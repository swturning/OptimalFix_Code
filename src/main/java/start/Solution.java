package start;

public class Solution {

    public static void main(String[] args) {
        int [] nums = new int [] {3,2,2,3};

        removeElement(nums,3);


    }


    public static  int removeElement(int[] nums, int val) {
        int i =0;
        for(int j=0;j<nums.length;j++){
            if(nums[j]!=val){
                nums[i] = nums[j];
                i++;
            }
        }
        return i+1;


    }


    }
















