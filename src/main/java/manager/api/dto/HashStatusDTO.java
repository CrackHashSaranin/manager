package manager.api.dto;

import manager.business_logic.enums.HashStatus;

public class HashStatusDTO
{
    public HashStatusDTO(HashStatus status, String data,int progress)
    {
        this.status=status;
        this.data = data;
        this.progress=progress;
    }

    public HashStatus getStatus() { return status; }
    public String getData() { return data; }
    public int getProgress() { return progress; }

    public void setStatus(HashStatus status) { this.status= status; }
    public void setProgress(int progress) { this.progress= progress; }

    private HashStatus status;
    private String data;
    private int progress;
}
