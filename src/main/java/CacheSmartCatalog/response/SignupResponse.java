package CacheSmartCatalog.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponse {

   private Long id;
   private String name;
   private String username;
   private String password;

}
