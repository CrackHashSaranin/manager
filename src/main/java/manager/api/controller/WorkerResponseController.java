package manager.api.controller;

import manager.business_logic.services.CrackHashService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/api/manager/hash/crack")
public class WorkerResponseController {

    private final CrackHashService crackHashService;

    public WorkerResponseController(CrackHashService crackHashService) {
        this.crackHashService = crackHashService;
    }

    @PostMapping("/request")
    public ResponseEntity<Void> updateCrackResult(@RequestBody Map<String, Object> request) {
         System.out.println("found "+String.join("", (List<String>) request.get("data")));

         String requestId = (String) request.get("requestId");
         List<String> foundWords = (List<String>) request.get("data");

         if (requestId != null && foundWords != null) {
             crackHashService.updateStatusToReady(requestId,
                     String.join("", (List<String>) request.get("data")));
         }

         return ResponseEntity.ok().build();
    }
}
