package manager.business_logic.entities;

import manager.api.dto.CrackHashRequestDTO;

public class CrackHashRequest
{

    public CrackHashRequest() {

    }

    public CrackHashRequest(String requestId, CrackHashRequestDTO crackHashRequestDTO) {
        this.requestId = requestId;
        this.crackHashRequestDTO = crackHashRequestDTO;
    }

    public String getRequestId() { return requestId; }
    public CrackHashRequestDTO getCrackHashRequestDTO() { return crackHashRequestDTO; }

    private String requestId;
    private CrackHashRequestDTO crackHashRequestDTO;
}
