package cz.cyberrange.platform.training.adaptive.persistence.entity;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "participant_task_assignment")
public class ParticipantTaskAssignment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participantTaskAssignmentGenerator")
    @SequenceGenerator(name = "participantTaskAssignmentGenerator", sequenceName = "participant_task_assignment_seq")
    @Column(name = "participant_task_assignment_id", nullable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id")
    private AbstractPhase abstractPhase;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_run_id")
    private TrainingRun trainingRun;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbstractPhase getAbstractPhase() {
        return abstractPhase;
    }

    public void setAbstractPhase(AbstractPhase abstractPhase) {
        this.abstractPhase = abstractPhase;
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
        return "ParticipantTaskAssignment{" +
                "id=" + id +
                '}';
    }
}
