package manager.business_logic.services;

import manager.api.dto.CrackHashRequestDTO;
import manager.api.dto.HashStatusDTO;
import manager.api.dto.StatusProgressDTO;
import manager.business_logic.config.RabbitMQConfig;
import manager.business_logic.config.WorkerConfig;
import manager.business_logic.entities.CrackHashRequest;
import manager.business_logic.enums.HashStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;

@Service
public class CrackHashService
{
    public CrackHashService(WorkerConfig workerConfig,RabbitTemplate rabbitTemplate) {
        this.workerConfig = workerConfig;
        this.rabbitTemplate = rabbitTemplate;
    }

    public UUID processCrackRequest(CrackHashRequestDTO request)
    {
        UUID requestId = UUID.randomUUID();

        requestStatuses.put(requestId.toString(),new HashStatusDTO(HashStatus.IN_PROGRESS,null,0));

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                new CrackHashRequest(requestId.toString(),request));

        return requestId;

    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processCrackRequest(CrackHashRequest message)
    {
        int i = 0;
        for (String url: workerConfig.getApiTaskList())
        {
            sendTaskToWorker(url,
                    message.getCrackHashRequestDTO().getHash(),
                    message.getCrackHashRequestDTO().getMaxLength(), i, workerConfig.getApiTaskList().size(),
                    message.getRequestId());
            i++;
        }
    }

    private void sendTaskToWorker(String url,String hash, int maxLength, int partNumber, int partCount, String requestId)
    {
        Map<String, Object> taskRequest = new HashMap<>();
        taskRequest.put("requestId", requestId);
        taskRequest.put("hash", hash);
        taskRequest.put("maxLength", maxLength);
        taskRequest.put("partNumber", partNumber);
        taskRequest.put("partCount", partCount);

        try {
            restTemplate.postForEntity(url, taskRequest, Void.class);
            System.out.println("Done: " + url);
        } catch (Exception e) {
            System.out.println("Undone: " + url);
            System.out.println("error: " + e.getMessage());
        }
    }

    public HashStatusDTO getStatus(String requestId)
    {
        if (!requestStatuses.containsKey(requestId)) {
            return new HashStatusDTO(HashStatus.NOT_FOUND, null, 0);
        }

        HashStatusDTO hashStatusDTO = requestStatuses.get(requestId);

        if (hashStatusDTO.getStatus()==HashStatus.READY || hashStatusDTO.getStatus()==HashStatus.PARTIAL)
        {
            return hashStatusDTO;
        }

        List<String> workerUrls = workerConfig.getApiStatusList();

        //List<String> workerUrls = workerConfig.getUrls();
        List<StatusProgressDTO> workerStatuses = new ArrayList<>();
        int totalProgress = 0;
        int workerCount = 0;

        for (String workerUrl: workerUrls) {
            try {
                String url = workerUrl + "?id=" + requestId;
                ResponseEntity<StatusProgressDTO> response = restTemplate.getForEntity(
                        url, StatusProgressDTO.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
                {
                    StatusProgressDTO workerStatus = response.getBody();

                    System.out.println(workerUrl+" process "+workerStatus.getProgress()+"%");

                    workerStatuses.add(workerStatus);
                    totalProgress += workerStatus.getProgress();
                    workerCount++;
                }
            } catch (Exception e) {
                System.err.println("Ошибка при запросе статуса у воркера " + workerUrl + ": " + e.getMessage());
            }
        }

        if (workerCount==0)
        {
            hashStatusDTO.setStatus(HashStatus.PARTIAL);
            return hashStatusDTO;
        }

        int avgProgress = (workerCount > 0) ? totalProgress / workerCount : 0;
        hashStatusDTO.setProgress(avgProgress);

        return hashStatusDTO;
    }

    public void updateStatusToReady(String requestId, String foundWords)
    {
        if (requestStatuses.containsKey(requestId))
        {
            requestStatuses.put(requestId, new HashStatusDTO(HashStatus.READY, foundWords,100));
        }
        else
        {
            System.out.println(requestId+" is already done");
        }
    }

//    private void scheduleTimeoutCheck(String requestId) {
//        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
//            if (requestStatuses.containsKey(requestId)
//                    && requestStatuses.get(requestId).getStatus()==HashStatus.IN_PROGRESS)
//            {
//
//                requestStatuses.put(requestId, new HashStatusDTO(HashStatus.PARTIAL, null,50));
//            }
//        }, TIMEOUT, TimeUnit.SECONDS);
//    }

    private final ConcurrentHashMap<String, HashStatusDTO> requestStatuses = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();

    private final long TIMEOUT = 30;

    private final WorkerConfig workerConfig;

    private final RabbitTemplate rabbitTemplate;
}