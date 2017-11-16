package us.codecraft.webmagic.samples;

import java.util.Map;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class TestPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultitems, Task task) {
        System.out.println("get page: " + resultitems.getRequest().getUrl());
        // 爬取的数据 是以 Map<K,V>的结构保存起来的。
        // 在此对爬取的数据 进行操作。
        for (Map.Entry<String, Object> entry : resultitems.getAll().entrySet()) {
            System.out.println(entry.getKey() + "---" + entry.getValue());
        }
    }
}
