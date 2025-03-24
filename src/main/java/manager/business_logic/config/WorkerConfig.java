package manager.business_logic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "worker")
public class WorkerConfig
{
    public WorkerConfig(Integer port, List<String> api, List<String> names) {
        this.port = port;
        this.api = api;
        this.names = names;


    }

    public List<String> getNames() {
        return names;
    }

    public Integer getPort() {
        return port;
    }

    public List<String> getApiPaths() {
        return api;
    }

//    public String getApiTask(Integer port)
//    {
//        return SECURITY+name+":"+port+api.get(0);
//    }
//
//    public String getApiStatus(Integer port)
//    {
//        return SECURITY+name+":"+port+api.get(1);
//    }

    public List<String> getApiTaskList()
    {
        List<String> urls = new ArrayList<>();

        for (String name: names)
        {
            urls.add(SECURITY+name+":"+port+api.get(0));
        }

        return urls;
    }

    public List<String> getApiStatusList()
    {
        List<String> urls = new ArrayList<>();

        for (String name: names)
        {
            urls.add(SECURITY+name+":"+port+api.get(1));
        }

        return urls;
    }


    private List<String> names;
    private Integer port;
    private List<String> api;

    private final String SECURITY="http://" ;
}