package akka.first.app.mapreduce.messages;

import java.util.HashMap;

/**
 * Created by 98379720172 on 06/05/2016.
 */
public final class ReduceData {

    private final HashMap<String, Integer> reduceDataList;

    public ReduceData(HashMap<String, Integer> reduceDataList) {
        this.reduceDataList = reduceDataList;
    }

    public HashMap<String, Integer> getReduceDataList() {
        return reduceDataList;
    }
}
