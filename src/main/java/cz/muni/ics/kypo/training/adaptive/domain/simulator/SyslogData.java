package cz.muni.ics.kypo.training.adaptive.domain.simulator;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class SyslogData {
   private String severity;
   private String host;
   private String programname;
   private String type;
   private String facility;

   private String timegenerated;
   private String procid;
   @JsonAlias({"@timestamp"})
   private String timestamp;
   @JsonAlias({"@version"})
   private String version;
   @JsonAlias({"fromhost-ip"})
   private String fromHostIp;

}
