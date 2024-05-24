package vn.edu.iuh.fit.orderservice.storage;

import io.github.bucket4j.Bucket;
import lombok.Data;
import lombok.experimental.Accessors;
import vn.edu.iuh.fit.orderservice.models.reposiotory.RtUser;

@Data
@Accessors(chain = true)
public class RateLimitData {

  private RtUser user;
  private Bucket bucket;
}
