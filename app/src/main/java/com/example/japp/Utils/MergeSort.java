package com.example.japp.Utils;

import com.example.japp.model.User;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {

    // Merge two subarrays L and M into arr
    public void merge(List<User> users, int p, int q, int r) {

        // Create L ← A[p..q] and M ← A[q+1..r]
        int n1 = q - p + 1;
        int n2 = r - q;
        List<User> L = new ArrayList<User>(n1);
        List<User> M = new ArrayList<>(n2);

        for (int i = 0; i < n1; i++)
            L.set(i, users.get(p + i));
        for (int j = 0; j < n2; j++)
            M.set(j, users.get(q + 1 + j));

        // Maintain current index of sub-arrays and main array
        int i, j, k;
        i = 0;
        j = 0;
        k = p;

        // Until we reach either end of either L or M, pick larger among
        // elements L and M and place them in the correct position at A[p..r]
        while (i < n1 && j < n2) {
            if (L.get(i).getMatching() <= M.get(j).getMatching()) {
                users.set(k, L.get(i));
                i++;
            } else {
                users.set(k, M.get(j));
                j++;
            }
            k++;
        }

        // When we run out of elements in either L or M,
        // pick up the remaining elements and put in A[p..r]
        while (i < n1) {
            users.set(k, L.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            users.set(k, M.get(j));
            j++;
            k++;
        }
    }

    // Divide the array into two subarrays, sort them and merge them
    public void mergeSort(List<User> arr, int l, int size) {
        if (l < size) {

            // m is the point where the array is divided into two subarrays
            int m = (l + size) / 2;

            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, size);

            // Merge the sorted subarrays
            merge(arr, l, m, size);
        }
    }

    // Print the array
    public static void printArray(List<User> users) {
        int n = users.size();
        for (int i = 0; i < n; ++i)
            System.out.print(users.get(i).getMatching() + " ");
        System.out.println();
    }

    // Driver program
//    public static void main(String args[]) {
//        StudentModel.DataList[] arr = {
//                new StudentModel.DataList("12", "sds", "sfsfs", "01099628366", false, "", "", "", "lat", "long", 1, 5, 0),
//                new StudentModel.DataList("12", "sds", "sfsfs", "01099628366", false, "", "", "", "lat", "long", 1, 5, 0),
//                new StudentModel.DataList("12", "sds", "sfsfs", "01099628366", false, "", "", "", "lat", "long", 1, 5, 0),
//                new StudentModel.DataList("12", "sds", "sfsfs", "01099628366", false, "", "", "", "lat", "long", 1, 5, 0),
//                new StudentModel.DataList("12", "sds", "sfsfs", "01099628366", false, "", "", "", "lat", "long", 1, 5, 0),
//                new StudentModel.DataList("12", "sds", "sfsfs", "01099628366", false, "", "", "", "lat", "long", 1, 5, 0)};
//
//        arr[0].distance = 5;
//        arr[1].distance = 3;
//        arr[2].distance = 5;
//        arr[3].distance = 7;
//        arr[4].distance = 2;
//        arr[5].distance = 1;
//
//        //size is 6
//
//        MergeSort ob = new MergeSort();
//        ob.mergeSort(arr, 0, arr.length - 1);
//
//        System.out.println("Sorted array:");
//        printArray(arr);
//    }
}