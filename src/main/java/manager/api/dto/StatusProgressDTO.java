package manager.api.dto;

import manager.business_logic.enums.HashStatus;

public class StatusProgressDTO
{
    public StatusProgressDTO(int progress)
    {
        this.progress=progress;
    }

    public int getProgress() { return progress; }

    public void setProgress(int progress) { this.progress= progress; }

    private int progress;
}
