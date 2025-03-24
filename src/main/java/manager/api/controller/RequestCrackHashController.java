package manager.api.controller;

import manager.api.dto.CrackHashRequestDTO;
import manager.api.dto.CrackHashResponseDTO;
import manager.business_logic.config.RabbitMQConfig;
import manager.business_logic.services.CrackHashService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hash")
public class RequestCrackHashController
{
    public RequestCrackHashController(CrackHashService crackHashService) {
        this.crackHashService = crackHashService;
    }

    @PostMapping("/crack")
    public ResponseEntity<CrackHashResponseDTO> crackHash(@RequestBody CrackHashRequestDTO request)
    {
        UUID requestId=crackHashService.processCrackRequest(request);
        return ResponseEntity.ok(new CrackHashResponseDTO(requestId.toString()));
    }

    private final CrackHashService crackHashService;
}