package cz.muni.ics.kypo.training.adaptive.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Represents basic info about Sandbox pools.
 */
public class SandboxPoolInfo {
    @NotNull(message = "{pool.id.NotNull.message}")
    private Long id;
    @NotNull(message = "{pool.definitionId.NotNull.message}")
    @JsonProperty(value = "definition_id")
    private Long definitionId;
    @NotNull(message = "{pool.size.NotNull.message}")
    private Long size;
    @NotNull(message = "{pool.maxSize.NotNull.message}")
    @JsonProperty(value = "max_size")
    private Long maxSize;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets definitionId.
     *
     * @return the definitionId
     */
    public Long getDefinitionId() {
        return definitionId;
    }

    /**
     * Sets definitionId.
     *
     * @param definitionId the definitionId
     */
    public void setDefinitionId(Long definitionId) {
        this.definitionId = definitionId;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Gets max size.
     *
     * @return the max size
     */
    public Long getMaxSize() {
        return maxSize;
    }

    /**
     * Sets max size.
     *
     * @param maxSize the max size
     */
    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public String toString() {
        return "SandboxPoolInfo{" +
                "id=" + id +
                ", definitionId=" + definitionId +
                ", size='" + size + '\'' +
                ", maxSize=" + maxSize +
                '}';
    }
}
