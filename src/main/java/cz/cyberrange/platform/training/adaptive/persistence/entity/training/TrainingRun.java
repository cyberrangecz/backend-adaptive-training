package cz.cyberrange.platform.training.adaptive.persistence.entity.training;

import cz.cyberrange.platform.training.adaptive.persistence.entity.SolutionInfo;
import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.InfoPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TRState;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Class represents Training run.
 * Training runs can be created based on instances.
 * Training runs are accessed by trainees
 */
@Entity
@Table(name = "training_run")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "TrainingRun.findAllParticipantRef",
                attributeNodes = @NamedAttributeNode(value = "participantRef")
        ),
        @NamedEntityGraph(
                name = "TrainingRun.findByIdParticipantRefTrainingInstanceCurrentPhase",
                attributeNodes = {
                        @NamedAttributeNode(value = "participantRef"),
                        @NamedAttributeNode(value = "trainingInstance"),
                        @NamedAttributeNode(value = "currentPhase")
                }
        )
})
@NamedQueries({
        @NamedQuery(
                name = "TrainingRun.findRunningTrainingRunOfUser",
                query = "SELECT tr FROM TrainingRun tr " +
                        "JOIN FETCH tr.trainingInstance ti " +
                        "JOIN FETCH tr.participantRef pr " +
                        "JOIN FETCH tr.currentPhase " +
                        "WHERE ti.accessToken = :accessToken AND pr.userRefId = :userRefId AND tr.sandboxInstanceRefId IS NOT NULL AND tr.state NOT LIKE 'FINISHED'"
        ),
        @NamedQuery(
                name = "TrainingRun.findByIdWithPhase",
                query = "SELECT tr FROM TrainingRun tr " +
                        "JOIN FETCH tr.currentPhase " +
                        "JOIN FETCH tr.trainingInstance ti " +
                        "JOIN FETCH ti.trainingDefinition " +
                        "WHERE tr.id= :trainingRunId"
        ),
        @NamedQuery(
                name = "TrainingRun.deleteTrainingRunsByTrainingInstance",
                query = "DELETE FROM TrainingRun tr WHERE tr.trainingInstance.id = :trainingInstanceId"
        ),
        @NamedQuery(
                name = "TrainingRun.existsAnyForTrainingInstance",
                query = "SELECT (COUNT(tr) > 0) FROM TrainingRun tr INNER JOIN tr.trainingInstance ti WHERE ti.id = :trainingInstanceId"
        ),
        @NamedQuery(
                name = "TrainingRun.findAllByParticipantRefId",
                query = "SELECT tr FROM TrainingRun tr " +
                        "INNER JOIN tr.participantRef pr " +
                        "INNER JOIN tr.trainingInstance ti " +
                        "INNER JOIN ti.trainingDefinition " +
                        "WHERE pr.userRefId = :userRefId"
        ),
        @NamedQuery(
                name = "TrainingRun.findAllByTrainingDefinitionIdAndParticipantUserRefId",
                query = "SELECT tr FROM TrainingRun tr " +
                        "INNER JOIN tr.participantRef pr " +
                        "INNER JOIN tr.trainingInstance ti " +
                        "INNER JOIN ti.trainingDefinition td " +
                        "WHERE td.id = :trainingDefinitionId AND pr.userRefId = :userRefId"
        ),
        @NamedQuery(
                name = "TrainingRun.findAllActiveByTrainingInstanceId",
                query = "SELECT tr FROM TrainingRun tr " +
                        "INNER JOIN tr.trainingInstance ti " +
                        "WHERE ti.id = :trainingInstanceId AND tr.state <> 'ARCHIVED'"
        ),
        @NamedQuery(
                name = "TrainingRun.findAllInactiveByTrainingInstanceId",
                query = "SELECT tr FROM TrainingRun tr " +
                        "INNER JOIN tr.trainingInstance ti " +
                        "WHERE ti.id = :trainingInstanceId AND tr.state = 'ARCHIVED'"
        ),
        @NamedQuery(
                name = "TrainingRun.findAllByTrainingDefinitionId",
                query = "SELECT tr FROM TrainingRun tr " +
                        "INNER JOIN tr.trainingInstance ti " +
                        "INNER JOIN ti.trainingDefinition td " +
                        "WHERE td.id = :trainingDefinitionId"
        )
})
public class TrainingRun implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trainingRunGenerator")
    @SequenceGenerator(name = "trainingRunGenerator")
    @Column(name = "training_run_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    @Column(name = "state", length = 128, nullable = false)
    @Enumerated(EnumType.STRING)
    private TRState state;
    @Column(name = "incorrect_answer_count", nullable = false)
    private int incorrectAnswerCount;
    @Column(name = "solution_taken", nullable = false)
    private boolean solutionTaken;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "current_phase_id")
    private AbstractPhase currentPhase;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_task_id")
    private Task currentTask;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "training_instance_id")
    private TrainingInstance trainingInstance;
    @Column(name = "sandbox_instance_ref_id", length = 36)
    private String sandboxInstanceRefId;
    @Column(name = "sandbox_instance_allocation_id")
    private Integer sandboxInstanceAllocationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User participantRef;
    @Column(name = "phase_answered")
    private boolean phaseAnswered;
    @Column(name = "previous_sandbox_instance_ref_id", length = 36)
    private String previousSandboxInstanceRefId;
    @ElementCollection(targetClass = SolutionInfo.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "solution_info", joinColumns = @JoinColumn(name = "training_run_id"))
    private Set<SolutionInfo> solutionInfoList = new HashSet<>();

    /**
     * Gets unique identification number of Training run
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique identification number of Training run
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets initiation time of Training run
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets initiation time of Training run
     *
     * @param startTime the start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets finish time of Training run
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets finish time of Training run
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets completion state of Training run
     * States are RUNNING, FINISHED, ARCHIVED
     *
     * @return the state
     */
    public TRState getState() {
        return state;
    }

    /**
     * Sets completion state of Training run
     * States are RUNNING, FINISHED, ARCHIVED
     *
     * @param state the state
     */
    public void setState(TRState state) {
        this.state = state;
    }

    /**
     * Gets phase that is currently being displayed to the trainee
     *
     * @return the current phase
     */
    public AbstractPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Sets phase that is currently being displayed to the trainee
     * Sets default data about phase to training run
     *
     * @param currentPhase the current phase
     */
    public void setCurrentPhase(AbstractPhase currentPhase) {
        this.phaseAnswered = currentPhase instanceof InfoPhase;
        this.solutionTaken = false;
        this.currentPhase = currentPhase;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    /**
     * Gets Training instance associated to Training run
     *
     * @return the training instance
     */
    public TrainingInstance getTrainingInstance() {
        return trainingInstance;
    }

    /**
     * Sets Training instance associated to Training run
     *
     * @param trainingInstance the training instance
     */
    public void setTrainingInstance(TrainingInstance trainingInstance) {
        this.trainingInstance = trainingInstance;
    }

    /**
     * Gets id of sandbox instance associated with Training run
     *
     * @return the sandbox instance reference id
     */
    public String getSandboxInstanceRefId() {
        return sandboxInstanceRefId;
    }

    /**
     * Sets id of sandbox instance associated with Training run
     *
     * @param sandboxInstanceRefId the sandbox instance reference id
     */
    public void setSandboxInstanceRefId(String sandboxInstanceRefId) {
        this.sandboxInstanceRefId = sandboxInstanceRefId;
    }

    /**
     * Gets sandbox instance allocation id associated with Training run
     *
     * @return the sandbox instance allocation id
     */
    public Integer getSandboxInstanceAllocationId() {
        return sandboxInstanceAllocationId;
    }

    /**
     * Sets sandbox instance allocation id associated with Training run
     * @param sandboxInstanceAllocationId the sandbox instance allocation id
     */
    public void setSandboxInstanceAllocationId(Integer sandboxInstanceAllocationId) {
        this.sandboxInstanceAllocationId = sandboxInstanceAllocationId;
    }

    /**
     * Gets number of failed attempts by trainee to submit correct answer on current phase
     *
     * @return the incorrect answer count
     */
    public int getIncorrectAnswerCount() {
        return incorrectAnswerCount;
    }

    /**
     * Sets number of failed attempts trainee can submit on current phase
     *
     * @param incorrectAnswerCount the incorrect answer count
     */
    public void setIncorrectAnswerCount(int incorrectAnswerCount) {
        this.incorrectAnswerCount = incorrectAnswerCount;
    }

    /**
     * Gets solution was taken on current phase
     *
     * @return the boolean
     */
    public boolean isSolutionTaken() {
        return solutionTaken;
    }

    /**
     * Sets solution was taken on current phase
     *
     * @param solutionTaken the solution taken
     */
    public void setSolutionTaken(boolean solutionTaken) {
        this.solutionTaken = solutionTaken;
    }

    /**
     * Gets DB reference of trainee
     *
     * @return the participant ref
     */
    public User getParticipantRef() {
        return participantRef;
    }

    /**
     * Sets DB reference of trainee
     *
     * @param participantRef the participant ref
     */
    public void setParticipantRef(User participantRef) {
        this.participantRef = participantRef;
    }

    /**
     * Gets if phase was answered
     *
     * @return the boolean
     */
    public boolean isPhaseAnswered() {
        return phaseAnswered;
    }

    /**
     * Sets if phase was answered
     *
     * @param phaseAnswered the phase answered
     */
    public void setPhaseAnswered(boolean phaseAnswered) {
        this.phaseAnswered = phaseAnswered;
    }

    /**
     * Gets id of previous sandbox instance ref assigned by training run.
     *
     * @return the id of previous sandbox instance ref
     */
    public String getPreviousSandboxInstanceRefId() {
        return previousSandboxInstanceRefId;
    }

    /**
     * Sets previous sandbox instance ref ID
     *
     * @param previousSandboxInstanceRefId the id of previous sandbox instance ref
     */
    public void setPreviousSandboxInstanceRefId(String previousSandboxInstanceRefId) {
        this.previousSandboxInstanceRefId = previousSandboxInstanceRefId;
    }

    public Set<SolutionInfo> getSolutionInfoList() {
        return solutionInfoList;
    }

    public void setSolutionInfoList(Set<SolutionInfo> solutionInfoList) {
        this.solutionInfoList = solutionInfoList;
    }

    public void addSolutionInfo(SolutionInfo solutionInfo) {
        this.solutionInfoList.add(solutionInfo);
    }

    @Override
    public String toString() {
        return "TrainingRun{" +
                "id=" + this.getId() +
                ", startTime=" + this.getStartTime() +
                ", endTime=" + this.getEndTime() +
                ", state=" + this.getState() +
                ", incorrectAnswerCount=" + this.getIncorrectAnswerCount() +
                ", solutionTaken=" + this.isSolutionTaken() +
                ", sandboxInstanceRefId=" + this.getSandboxInstanceRefId() +
                ", sandboxInstanceAllocationId=" + this.getSandboxInstanceAllocationId() +
                ", phaseAnswered=" + this.isPhaseAnswered() +
                '}';
    }
}
