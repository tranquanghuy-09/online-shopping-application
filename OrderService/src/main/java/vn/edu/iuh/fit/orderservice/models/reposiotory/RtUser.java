package vn.edu.iuh.fit.orderservice.models.reposiotory;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RtUser {

  private String id;
  private String username;
  private String apiKey;
  private UserPlan userPlan;
}
