package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * Class specifying command to be executed during the training phase.
 */
@Embeddable
public class ExpectedCommand {

    @Column(name = "command")
    private String command;

    /**
     * Gets the value of the expected command
     *
     * @return value of the expected command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the value of the expected command
     *
     * @param command value of the expected command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpectedCommand that = (ExpectedCommand) o;
        return Objects.equals(getCommand(), that.getCommand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommand());
    }


    @Override
    public String toString() {
        return "ExpectedCommand{" +
                "command='" + command + '\'' +
                '}';
    }
}
