package akka.first.app.mapreduce.messages;

import java.util.List;

/**
 * Created by 98379720172 on 06/05/2016.
 */
public class MapData {

    private final List<WordCount> dataList;

    public MapData(List<WordCount> dataList) {
        this.dataList = dataList;
    }

    public List<WordCount> getDataList() {
        return dataList;
    }
}
