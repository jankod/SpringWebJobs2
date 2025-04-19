package hr.bioinfo.swj.job;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data

public class JobWorkflowContext {

    private final Map<String, Object> params = new HashMap<>();


    public Object getParam(String key) {
        return params.get(key);
    }

    public void setParam(String key, Object value) {
        params.put(key, value);
    }
}
