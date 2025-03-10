package cz.cyberrange.platform.training.adaptive.persistence.entity;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import cz.cyberrange.platform.training.adaptive.persistence.enums.SubmissionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "submissionGenerator")
    @SequenceGenerator(name = "submissionGenerator", sequenceName = "submission_seq")
    @Column(name = "submission_id")
    private Long id;
    @Column(name = "provided", nullable = false)
    private String provided;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SubmissionType type;
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
    @JoinColumn(name = "phase_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AbstractPhase phase;
    @JoinColumn(name = "task_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;
    @JoinColumn(name = "training_run_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingRun trainingRun;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvided() {
        return provided;
    }

    public void setProvided(String provided) {
        this.provided = provided;
    }

    public SubmissionType getType() {
        return type;
    }

    public void setType(SubmissionType type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public AbstractPhase getPhase() {
        return phase;
    }

    public void setPhase(AbstractPhase phase) {
        this.phase = phase;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TrainingRun getTrainingRun() {
        return trainingRun;
    }

    public void setTrainingRun(TrainingRun trainingRun) {
        this.trainingRun = trainingRun;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "provided='" + provided + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", ipAddress='" + ipAddress + '\'' +
                "} " + super.toString();
    }
}
