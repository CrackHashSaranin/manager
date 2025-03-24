package manager.api.dto;

public class CrackHashResponseDTO
{
    private String requestId;

    public CrackHashResponseDTO(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() { return requestId; }
}
