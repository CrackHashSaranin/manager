package manager.api.dto;


public class CrackHashRequestDTO
{
    private String hash;
    private int maxLength;

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public int getMaxLength() { return maxLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
}
