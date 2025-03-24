package manager.api.controller;
import manager.business_logic.services.CrackHashService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hash")
public class StatusCrackHashController
{
    public StatusCrackHashController(CrackHashService crackHashService) {
        this.crackHashService = crackHashService;
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(@RequestParam String requestId)
    {
        return ResponseEntity.ok(crackHashService.getStatus(requestId));
    }

    private final CrackHashService crackHashService;
}



