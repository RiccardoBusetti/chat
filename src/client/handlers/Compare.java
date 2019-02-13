package client.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.util.Pair;

public class Compare {

    public static List<String> compare(Pair<String, Chat>[] arr, int n)
    {
        if (n == -1)
            n = arr.length;

        List<String> out = new ArrayList<>();

        // Comparator to sort the pair according to second element
        Arrays.sort(arr, (p1, p2) -> (int) (p1.getValue().getLatestMessageTimestamp() - p2.getValue().getLatestMessageTimestamp()));

        for (int i = 0; i < n; i++) {
            out.add(arr[i].getKey());
        }

        return out;
    }
}